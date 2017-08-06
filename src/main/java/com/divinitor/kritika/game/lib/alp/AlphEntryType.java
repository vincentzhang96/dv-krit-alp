package com.divinitor.kritika.game.lib.alp;

import java.util.NoSuchElementException;

public enum AlphEntryType {

    DIRECTORY(1),
    FILE(4);

    final short value;

    AlphEntryType(int value) {
        this.value = (short) value;
    }

    public int value() {
        return value;
    }

    public static AlphEntryType valueOf(int value) {
        return valueOf((short) value);
    }

    public static AlphEntryType valueOf(short value) {
        for (AlphEntryType alphEntryType : values()) {
            if (alphEntryType.value == value) {
                return alphEntryType;
            }
        }

        throw new NoSuchElementException("No entry type with ID " + value);
    }
}
