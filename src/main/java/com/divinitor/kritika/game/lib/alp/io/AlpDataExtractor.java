package com.divinitor.kritika.game.lib.alp.io;

import com.divinitor.kritika.game.lib.alp.AlphFileCompressionMode;
import com.divinitor.kritika.game.lib.alp.AlphFileEntry;
import com.divinitor.kritika.game.lib.alp.Constants;
import com.divinitor.kritika.util.crypto.RotatingXor;
import net.jpountz.lz4.LZ4Decompressor;
import net.jpountz.lz4.LZ4Factory;
import net.jpountz.lz4.LZ4FastDecompressor;
import org.apache.commons.compress.compressors.lzma.LZMAUtils;

import java.io.IOException;
import java.io.RandomAccessFile;

public class AlpDataExtractor {

    public void getData(AlphFileEntry info, RandomAccessFile raf, byte[] out, byte[] scratch)
        throws IOException {
        RotatingXor xor = new RotatingXor(Constants.xorKey());
        xor.setOffset(Integer.remainderUnsigned(info.getOffset(), 4));
        raf.seek(Integer.toUnsignedLong(info.getOffset()));

        assert scratch.length >= info.getDiskSize();
        assert out.length >= info.getRawSize();

        raf.readFully(scratch);
        xor.xor(scratch);

        if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZ4) {
            LZ4FastDecompressor decomp = LZ4Factory.fastestInstance().fastDecompressor();
            decomp.decompress(scratch, out);
        } else if (info.getCompressionModeEnum() == AlphFileCompressionMode.LZMA) {
            //  TODO
            throw new UnsupportedOperationException("Not yet implemented");
        }
    }


}
