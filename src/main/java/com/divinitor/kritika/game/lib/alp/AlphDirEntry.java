package com.divinitor.kritika.game.lib.alp;

import java.util.ArrayList;
import java.util.List;

public class AlphDirEntry extends AlphEntryBody {

    int subfolderCount;
    int subfileCount;

    protected final List<AlphEntry> children;

    public AlphDirEntry() {
        children = new ArrayList<>();
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

    public List<AlphEntry> getChildren() {
        return children;
    }
}
