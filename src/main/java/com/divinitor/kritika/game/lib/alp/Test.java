package com.divinitor.kritika.game.lib.alp;

import com.divinitor.kritika.util.KritUtilities;
import com.divinitor.kritika.util.crypto.RotatingXor;
import com.google.common.io.LittleEndianDataInputStream;
import com.google.common.primitives.Bytes;
import com.google.common.primitives.Ints;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Test {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter path to file:");
        String pathStr = scanner.nextLine().replace("\"", "");
        Path path = Paths.get(pathStr);

        SeekableByteChannel seekableByteChannel = Files.newByteChannel(path);
        ByteBuffer buf = ByteBuffer.allocateDirect((int) seekableByteChannel.size());
        seekableByteChannel.read(buf);
        buf.flip();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        int magic = buf.getInt();
        if (magic != 0x41504b47) {
            System.out.printf("Warning: header mismatch, expected GKPA, got %s\n",
                    new String(Ints.toByteArray(magic)));
        }

        int version = Short.toUnsignedInt(buf.getShort());
        if (version != 4) {
            System.out.printf("Warning: Ran into version %s\n", version);
        }

        int indexOffset = buf.getInt();
        buf.position(indexOffset);
        readTable(buf, "");
    }

    private static void readTable(ByteBuffer buf, String path) {
        int size = buf.getInt();
        int tableStartPos = buf.position();

        RotatingXor xor = new RotatingXor(0xdf976f03);
        byte[] entryMagic = new byte[4];
        buf.get(entryMagic);
        xor.xor(entryMagic);
        String entryMagicStr = new String(entryMagic);
        System.out.printf("Entry type is %s\n", entryMagicStr);

        int type = Short.toUnsignedInt(xor.xor(buf.getShort()));
        System.out.printf("Type %d: ", type);
        if (type == 1) {
            System.out.println("Folder");
        } else if (type == 4) {
            System.out.println("Files");
        } else {
            System.out.println("Unknown");
        }

        int numFolders = 0;
        int numFiles = 0;

        if ("DGKP".equals(entryMagicStr)) {
            numFolders = xor.xor(buf.getInt());
            numFiles = xor.xor(buf.getInt());
            System.out.printf("%,d folders, %,d files\n", numFolders, numFiles);
            int nameSize = Short.toUnsignedInt(xor.xor(buf.getShort())) * 2;
            byte[] nameBuf = new byte[nameSize];
            buf.get(nameBuf);
            xor.xor(nameBuf);
            String name = new String(nameBuf, StandardCharsets.UTF_16LE);
            System.out.printf("Name: %s\n", name);
            path = path + name + "/";
            System.out.printf("Path: %s\n", path);
        } else if ("FGKP".equals(entryMagicStr)) {
            int compressionType = Byte.toUnsignedInt(xor.xor(buf.get()));
            if (compressionType == 0) {
                System.out.println("LZMA compressed");
                byte[] data = new byte[5];
                buf.get(data);
                xor.xor(data);
                System.out.println(KritUtilities.printHexDump(data));
            } else if (compressionType == 1) {
                System.out.println("LZ4 compressed");
                buf.getInt();
            } else {
                throw new UnsupportedOperationException("compType " + compressionType);
            }

            int skp = buf.position() % 2;
            buf.position(buf.position() + skp);

            if (skp == 1) {
                xor.xor((byte) 0);
            }

            int offset = xor.xor(buf.getInt());
            int rawSize = xor.xor(buf.getInt());
            int diskSize = xor.xor(buf.getInt());
            int timestamp = xor.xor(buf.getInt());
            int crc1 = xor.xor(buf.getInt());
            int crc2 = xor.xor(buf.getInt());
            int nameSize = Short.toUnsignedInt(xor.xor(buf.getShort())) * 2;
            byte[] nameBuf = new byte[nameSize];
            buf.get(nameBuf);
//            System.out.println(KritUtilities.printHexDump(nameBuf));
            xor.xor(nameBuf);
//            System.out.println(KritUtilities.printHexDump(nameBuf));
            String name = new String(nameBuf, StandardCharsets.UTF_16LE);
            System.out.printf("off 0x%08X rsize %,d dsize %,d time %d crc1 0x%08X crc2 0x%08X name %s\n",
                    offset, rawSize, diskSize, timestamp, crc1, crc2, name);
        } else {
            System.out.println("Unknown type");
            return;
        }

        buf.position(tableStartPos + size);

        for (int i = 0; i < numFolders; i++) {
            readTable(buf, path);
        }

        for (int i = 0; i < numFiles; i++) {
            readTable(buf, path);
        }
    }

}
