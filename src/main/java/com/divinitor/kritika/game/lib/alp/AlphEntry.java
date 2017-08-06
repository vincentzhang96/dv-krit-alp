package com.divinitor.kritika.game.lib.alp;

public class AlphEntry {

    protected AlphEntry parentEntry;
    protected int entrySize;
    protected int magic;
    protected short type;
    protected AlphEntryBody body;

    public AlphEntry() {
    }

    public AlphEntry getParentEntry() {
        return parentEntry;
    }

    public void setParentEntry(AlphEntry parentEntry) {
        this.parentEntry = parentEntry;
    }

    public int getEntrySize() {
        return entrySize;
    }

    public int getMagic() {
        return magic;
    }

    public short getType() {
        return type;
    }

    public AlphEntryMagic getMagicEnum() {
        return AlphEntryMagic.valueOf(magic);
    }

    public AlphEntryType getTypeEnum() {
        return AlphEntryType.valueOf(type);
    }

    public void setEntrySize(int entrySize) {
        this.entrySize = entrySize;
    }

    public void setMagic(int magic) {
        this.magic = magic;
    }

    public void setMagic(AlphEntryMagic magic) {
        this.magic = magic.hex;
    }

    public void setType(short type) {
        this.type = type;
    }

    public void setType(int type) {
        this.setType((short) type);
    }

    public void setType(AlphEntryType type) {
        this.setType(type.value());
    }

    public AlphEntryBody getBody() {
        return body;
    }

    public AlphFileEntry getBodyAsFile() {
        if (this.type != AlphEntryType.FILE.value()) {
            throw new IllegalStateException("Not a file");
        }
        return (AlphFileEntry) body;
    }

    public AlphDirEntry getBodyAsDir() {
        if (this.type != AlphEntryType.DIRECTORY.value()) {
            throw new IllegalStateException("Not a directory");
        }
        return (AlphDirEntry) body;
    }

    public void setBody(AlphEntryBody body) {
        this.body = body;
    }
}
