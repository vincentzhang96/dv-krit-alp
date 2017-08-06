package com.divinitor.kritika.game.lib.alp;

import java.util.ArrayList;
import java.util.List;

public class AlphDirEntry extends AlphEntryBody {

    protected transient AlphEntry enclosingEntry;
    int subfolderCount;
    int subfileCount;
    String name;

    protected final List<AlphEntry> children;

    public AlphDirEntry() {
        children = new ArrayList<>();
    }

    public AlphEntry getEnclosingEntry() {
        return enclosingEntry;
    }

    public void setEnclosingEntry(AlphEntry enclosingEntry) {
        this.enclosingEntry = enclosingEntry;
    }

    public int getSubfolderCount() {
        return subfolderCount;
    }

    public void setSubfolderCount(int subfolderCount) {
        this.subfolderCount = subfolderCount;
    }

    public int getSubfileCount() {
        return subfileCount;
    }

    public void setSubfileCount(int subfileCount) {
        this.subfileCount = subfileCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AlphEntry> getChildren() {
        return children;
    }
}
