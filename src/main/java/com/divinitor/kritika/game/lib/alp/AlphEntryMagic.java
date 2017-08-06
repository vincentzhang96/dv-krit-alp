package com.divinitor.kritika.game.lib.alp;

import java.util.NoSuchElementException;

public enum AlphEntryMagic {

    DGKP(0x504B4744),
    FGKP(0x504b4746);

    final int hex;

    AlphEntryMagic(int hex) {
        this.hex = hex;
    }

    public int value() {
        return hex;
    }

    public static AlphEntryMagic valueOf(int value) {
        for (AlphEntryMagic alphEntryMagic : AlphEntryMagic.values()) {
            if (alphEntryMagic.hex == value) {
                return alphEntryMagic;
            }
        }

        throw new NoSuchElementException("No entry type with ID " + value);
    }
}
