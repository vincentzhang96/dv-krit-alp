package com.divinitor.kritika.game.lib.alp;

/**
 * Lightweight version of AlphFileEntry which contains no external references, unlike AlphFileEntry which maintains
 * references to its parent. It is completely independent of the original parsed entry.
 *
 * @see AlphFileEntry
 */
public final class AlphLightweightFileEntry {

    private String name;
    private String path;
    private int fileOffset;
    private int rawSize;
    private int diskSize;
    private int timestampSec;
    private int crcA;
    private int crcB;
    private AlphFileCompressionMode compressionMode;
    private byte[] compressionParam;

    public AlphLightweightFileEntry() {
    }

    public AlphLightweightFileEntry(AlphFileEntry entry) {
        name = entry.getName();
        path = entry.getEnclosingEntry().getPath();
        fileOffset = entry.getOffset();
        rawSize = entry.getRawSize();
        diskSize = entry.getDiskSize();
        timestampSec = entry.getTimestamp();
        crcA = entry.getCrcA();
        crcB = entry.getCrcB();
        compressionMode = entry.getCompressionModeEnum();
        compressionParam = new byte[AlphFileEntry.SZ_COMPRESSION_DATA];
        System.arraycopy(entry.getCompressionParam(), 0, compressionParam, 0, AlphFileEntry.SZ_COMPRESSION_DATA);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getFileOffset() {
        return fileOffset;
    }

    public void setFileOffset(int fileOffset) {
        this.fileOffset = fileOffset;
    }

    public int getRawSize() {
        return rawSize;
    }

    public void setRawSize(int rawSize) {
        this.rawSize = rawSize;
    }

    public int getDiskSize() {
        return diskSize;
    }

    public void setDiskSize(int diskSize) {
        this.diskSize = diskSize;
    }

    public int getTimestampSec() {
        return timestampSec;
    }

    public void setTimestampSec(int timestampSec) {
        this.timestampSec = timestampSec;
    }

    public int getCrcA() {
        return crcA;
    }

    public void setCrcA(int crcA) {
        this.crcA = crcA;
    }

    public int getCrcB() {
        return crcB;
    }

    public void setCrcB(int crcB) {
        this.crcB = crcB;
    }

    public AlphFileCompressionMode getCompressionMode() {
        return compressionMode;
    }

    public void setCompressionMode(AlphFileCompressionMode compressionMode) {
        this.compressionMode = compressionMode;
    }

    public byte[] getCompressionParam() {
        return compressionParam;
    }

    public void setCompressionParam(byte[] compressionParam) {
        this.compressionParam = compressionParam;
    }
}
