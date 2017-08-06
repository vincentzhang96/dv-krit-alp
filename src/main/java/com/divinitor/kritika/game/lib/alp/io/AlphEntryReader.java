package com.divinitor.kritika.game.lib.alp.io;

import com.divinitor.kritika.game.lib.alp.*;
import com.divinitor.kritika.util.KritUtilities;
import com.divinitor.kritika.util.crypto.RotatingXor;
import com.divinitor.kritika.util.io.KritStringUtils;
import com.divinitor.kritika.util.io.StructReadException;
import com.divinitor.kritika.util.io.StructReader;
import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.NoSuchElementException;

public class AlphEntryReader implements StructReader<AlphEntry> {

    private static final AlphEntryReader INSTANCE = new AlphEntryReader();

    public static AlphEntryReader getInstance() {
        return INSTANCE;
    }

    public AlphEntryReader() {
    }

    @Override
    public AlphEntry read(InputStream inputStream, int offset) throws IOException, StructReadException {
        LittleEndianDataInputStream dis = KritUtilities.ensureLittleEndian(inputStream);

        AlphEntry ret = new AlphEntry();
        ret.setEntrySize(dis.readInt());
        offset += 4;

        byte[] entryData = new byte[ret.getEntrySize()];
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        dis.readFully(entryData);
        xor.xor(entryData);

        readEntry(ret, entryData, offset);

        if (ret.getTypeEnum() == AlphEntryType.DIRECTORY) {
            AlphDirEntry dirInfo = ret.getBodyAsDir();

            offset += ret.getEntrySize();
            int count = dirInfo.getSubfolderCount() + dirInfo.getSubfileCount();
            for (int i = 0; i < count; i++) {
                AlphEntry child = this.read(dis, offset);
                child.setParentEntry(ret);
                dirInfo.getChildren().add(child);
                offset += 4 + child.getEntrySize();
            }
        }

        return ret;
    }

    @Override
    public AlphEntry read(ByteBuffer buf) throws StructReadException {
        buf.order(ByteOrder.LITTLE_ENDIAN);

        AlphEntry ret = new AlphEntry();
        ret.setEntrySize(buf.getInt());
        int offset = buf.position();

        byte[] entryData = new byte[ret.getEntrySize()];
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        buf.get(entryData);
        xor.xor(entryData);

        readEntry(ret, entryData, offset);

        if (ret.getTypeEnum() == AlphEntryType.DIRECTORY) {
            AlphDirEntry dirInfo = ret.getBodyAsDir();

            int count = dirInfo.getSubfolderCount() + dirInfo.getSubfileCount();
            for (int i = 0; i < count; i++) {
                AlphEntry child = this.read(buf);
                child.setParentEntry(ret);
                dirInfo.getChildren().add(child);
            }
        }

        return ret;
    }

    private void readEntry(AlphEntry entry, byte[] data, int baseOffset) {
        ByteBuffer buf = ByteBuffer.wrap(data);
        buf.order(ByteOrder.LITTLE_ENDIAN);

        entry.setMagic(buf.getInt());
        entry.setType(buf.getShort());

        AlphEntryType type;

        try {
            type = entry.getTypeEnum();
        } catch (NoSuchElementException nsee) {
            throw new StructReadException("Invalid enum: " + entry.getType(), baseOffset + 4, "type");
        }

        switch (type) {
            case DIRECTORY: {
                AlphDirEntry body = new AlphDirEntry();
                body.setSubfolderCount(buf.getInt());
                body.setSubfileCount(buf.getInt());
                body.setName(KritStringUtils.readSPString(buf));
                entry.setBody(body);
                break;
            }
            case FILE: {
                AlphFileEntry body = new AlphFileEntry();
                body.setCompressionMode(buf.get());
                try {
                    body.getCompressionModeEnum();
                } catch (NoSuchElementException nsee) {
                    throw new StructReadException("Invalid enum: " + body.getCompressionMode(), baseOffset + 6, "compressionMode");
                }

                byte[] compressParam = new byte[AlphFileEntry.SZ_COMPRESSION_DATA];
                buf.get(compressParam);
                body.setCompressionParam(compressParam);
                body.setOffset(buf.getInt());
                body.setRawSize(buf.getInt());
                body.setDiskSize(buf.getInt());
                body.setTimestamp(buf.getInt());
                body.setCrcA(buf.getInt());
                body.setCrcB(buf.getInt());
                body.setName(KritStringUtils.readSPString(buf));
                entry.setBody(body);
                break;
            }
        }
    }
}
