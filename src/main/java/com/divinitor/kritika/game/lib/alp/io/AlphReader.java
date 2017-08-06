package com.divinitor.kritika.game.lib.alp.io;

import com.divinitor.kritika.game.lib.alp.Alph;
import com.divinitor.kritika.util.KritUtilities;
import com.divinitor.kritika.util.io.StructReadException;
import com.divinitor.kritika.util.io.StructReader;
import com.google.common.io.LittleEndianDataInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class AlphReader implements StructReader<Alph> {

    private static final AlphReader INSTANCE = new AlphReader();

    public static AlphReader getInstance() {
        return INSTANCE;
    }

    public AlphReader() {
    }

    @Override
    public Alph read(InputStream inputStream, int bytesRead) throws IOException, StructReadException {
        return this.read(inputStream);
    }

    @Override
    public Alph read(InputStream inputStream) throws IOException, StructReadException {
        LittleEndianDataInputStream dis = KritUtilities.ensureLittleEndian(inputStream);

        Alph ret = new Alph();
        ret.setMagicNumber(dis.readInt());
        ret.setVersion(dis.readShort());
        ret.setIndexOffset(dis.readInt());

        //  Skip forward
        int bytesToSkip = ret.getIndexOffset() - (4 + 2 + 4);
        while (bytesToSkip > 0) {
            bytesToSkip -= dis.skipBytes(bytesToSkip);
        }

        AlphEntryReader reader = AlphEntryReader.getInstance();
        ret.setRoot(reader.read(inputStream, ret.getIndexOffset()));

        return ret;
    }

    @Override
    public Alph read(ByteBuffer buf) throws StructReadException {
        buf.order(ByteOrder.LITTLE_ENDIAN);

        Alph ret = new Alph();
        ret.setMagicNumber(buf.getInt());
        ret.setVersion(buf.getShort());
        ret.setIndexOffset(buf.getInt());

        buf.position(ret.getIndexOffset());

        AlphEntryReader reader = AlphEntryReader.getInstance();
        ret.setRoot(reader.read(buf));

        return ret;
    }
}
