package com.divinitor.kritika.game.lib.alp;

public class Alph {

    public static final int MAGIC_NUMBER = 0x41504B47;

    int magicNumber;
    short version;
    int indexOffset;
    AlphEntry root;

    public Alph() {
    }

    public void setMagicNumber(int magicNumber) {
        this.magicNumber = magicNumber;
    }

    public void setVersion(int version) {
        this.setVersion((short) version);
    }

    public void setVersion(short version) {
        this.version = version;
    }

    public void setIndexOffset(int indexOffset) {
        this.indexOffset = indexOffset;
    }

    public AlphEntry getRoot() {
        return root;
    }

    public void setRoot(AlphEntry root) {
        this.root = root;
    }

    public int getMagicNumber() {
        return magicNumber;
    }

    public int getVersion() {
        return version;
    }

    public int getIndexOffset() {
        return indexOffset;
    }
}
