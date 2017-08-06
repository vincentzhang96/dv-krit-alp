package com.divinitor.kritika.game.lib.alp;

import java.util.NoSuchElementException;

public enum AlphFileCompressionMode {

    LZMA(0),
    LZ4(1);

    final int value;

    AlphFileCompressionMode(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }


    public static AlphFileCompressionMode valueOf(byte value) {
        return valueOf(Byte.toUnsignedInt(value));
    }

    public static AlphFileCompressionMode valueOf(int value) {
        for (AlphFileCompressionMode compressionMode : values()) {
            if (compressionMode.value == value) {
                return compressionMode;
            }
        }

        throw new NoSuchElementException("No compression type with ID " + value);
    }
}
