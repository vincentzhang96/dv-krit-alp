package com.divinitor.kritika.game.lib.alp.io;

import com.divinitor.kritika.game.lib.alp.AlphFileCompressionMode;
import com.divinitor.kritika.game.lib.alp.AlphFileEntry;
import com.divinitor.kritika.game.lib.alp.Constants;
import com.divinitor.kritika.util.crypto.RotatingXor;
import com.google.common.primitives.Ints;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.tukaani.xz.LZMA2InputStream;
import org.tukaani.xz.LZMAInputStream;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

public class AlpDataExtractor {

    public void getData(AlphFileEntry info, RandomAccessFile alpFile, byte[] out) throws IOException {
        getData(info, alpFile, out, null);
    }

    public void getData(AlphFileEntry info, RandomAccessFile alpFile, byte[] out, byte[] scratch)
            throws IOException {
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        alpFile.seek(Integer.toUnsignedLong(info.getOffset()));

        if (scratch == null) {
            scratch = new byte[info.getDiskSize()];
        }

        assert scratch.length >= info.getDiskSize();
        assert out.length >= info.getRawSize();

        alpFile.readFully(scratch);
        xor.xor(scratch);

        if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZ4) {
            LZ4FastDecompressor decomp = LZ4Factory.fastestInstance().fastDecompressor();
            decomp.decompress(scratch, out, info.getRawSize());
        } else if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZMA) {
            try (LZMAInputStream inputStream = new LZMAInputStream(new ByteArrayInputStream(scratch))) {
                byte[] buf = new byte[8192];
                int read;
                int pos = 0;
                while ((read = inputStream.read(buf)) != -1) {
                    System.arraycopy(buf, 0, out, pos, read);
                    pos += read;
                }
            }
        }
    }
    public void getData(AlphFileEntry info, RandomAccessFile alpFile, Path outFile) throws IOException {
        getData(info, alpFile, outFile, null);
    }

    public void getData(AlphFileEntry info, RandomAccessFile alpFile, Path outFile, byte[] scratch) throws IOException {
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        alpFile.seek(Integer.toUnsignedLong(info.getOffset()));

        if (scratch == null) {
            scratch = new byte[info.getDiskSize()];
        }

        assert scratch.length >= info.getDiskSize();

        alpFile.readFully(scratch);
        xor.xor(scratch);

        if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZ4) {
            byte[] out = new byte[info.getRawSize()];
            LZ4FastDecompressor decomp = LZ4Factory.fastestInstance().fastDecompressor();
            decomp.decompress(scratch, out);
            Files.write(outFile, out, TRUNCATE_EXISTING, CREATE);
        } else if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZMA) {
            int param = Integer.reverseBytes(Ints.fromByteArray(info.getCompressionParam()));
            System.out.printf("0x%08X %s\n", param, info.getEnclosingEntry().getPath());
            try (LZMA2InputStream inputStream = new LZMA2InputStream(new ByteArrayInputStream(scratch), param)) {
                byte[] buf = new byte[8192];
                int read;
                try (BufferedOutputStream out = new BufferedOutputStream(
                    Files.newOutputStream(outFile, TRUNCATE_EXISTING, CREATE))) {
                    while ((read = inputStream.read(buf)) != -1) {
                        out.write(buf, 0, read);
                    }
                    out.flush();
                }
            }
        }
    }
}
