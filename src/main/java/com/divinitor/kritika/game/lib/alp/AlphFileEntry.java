package com.divinitor.kritika.game.lib.alp;

import java.time.Instant;

public class AlphFileEntry extends AlphEntryBody {

    public static final int SZ_COMPRESSION_DATA = 5;

    protected transient AlphEntry enclosingEntry;

    byte compressionMode;
    byte[] compressionParam;

    int offset;
    int rawSize;
    int diskSize;
    int timestamp;
    transient Instant timestampInstant;
    int crcA;
    int crcB;
    String name;

    public AlphFileEntry() {
    }

    public byte getCompressionMode() {
        return compressionMode;
    }

    public void setCompressionMode(byte compressionMode) {
        this.compressionMode = compressionMode;
    }

    public byte[] getCompressionParam() {
        return compressionParam;
    }

    public void setCompressionParam(byte[] compressionParam) {
        this.compressionParam = compressionParam;
    }

    public AlphFileCompressionMode getCompressionModeEnum() {
        return AlphFileCompressionMode.valueOf(compressionMode);
    }

    public void setCompressionMode(AlphFileCompressionMode mode) {
        this.compressionMode = (byte) mode.value();
    }

    public AlphEntry getEnclosingEntry() {
        return enclosingEntry;
    }

    public void setEnclosingEntry(AlphEntry enclosingEntry) {
        this.enclosingEntry = enclosingEntry;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
        this.timestampInstant = Instant.ofEpochSecond(timestamp);
    }

    public Instant getTimestampInstant() {
        if (timestampInstant == null) {
            timestampInstant = Instant.ofEpochSecond(this.timestamp);
        }
        return timestampInstant;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = (int) timestamp.getEpochSecond();
        this.timestampInstant = timestamp;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
