package com.divinitor.kritika.game.lib.alp.io;

import com.divinitor.kritika.game.lib.alp.AlphFileCompressionMode;
import com.divinitor.kritika.game.lib.alp.AlphFileEntry;
import com.divinitor.kritika.game.lib.alp.AlphLightweightFileEntry;
import com.divinitor.kritika.game.lib.alp.Constants;
import com.divinitor.kritika.util.crypto.RotatingXor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AlpDataExtractor {

    public void getData(AlphFileEntry info, RandomAccessFile raf, byte[] out, byte[] scratch)
            throws IOException {
        assert scratch.length >= info.getDiskSize();
        assert out.length >= info.getRawSize();

        int offset = info.getOffset();
        AlphFileCompressionMode compMode = info.getCompressionModeEnum();
        byte[] compParam = info.getCompressionParam();

        extract(raf, out, scratch, offset, compMode, compParam);
    }

    public void getData(AlphLightweightFileEntry info, RandomAccessFile raf, byte[] out, byte[] scratch)
            throws IOException {
        assert scratch.length >= info.getDiskSize();
        assert out.length >= info.getRawSize();

        int offset = info.getFileOffset();
        AlphFileCompressionMode compMode = info.getCompressionMode();
        byte[] compParam = info.getCompressionParam();

        extract(raf, out, scratch, offset, compMode, compParam);
    }

    private void extract(
            RandomAccessFile raf,
            byte[] out,
            byte[] scratch,
            int offset,
            AlphFileCompressionMode compMode,
            byte[] compParam)
            throws IOException {
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        xor.setOffset(Integer.remainderUnsigned(offset, 4));
        raf.seek(Integer.toUnsignedLong(offset));

        raf.readFully(scratch);
        xor.xor(scratch);

        if (compMode == AlphFileCompressionMode.LZ4) {
            LZ4FastDecompressor decomp = LZ4Factory.fastestInstance().fastDecompressor();
            decomp.decompress(scratch, out);
        } else if (compMode == AlphFileCompressionMode.LZMA) {
            int i = compParam.length;
            //  TODO
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }


}
