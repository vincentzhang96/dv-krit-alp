package com.divinitor.kritika.game.lib.alp;

public abstract class AlphEntryBody {

    protected transient AlphEntry enclosingEntry;
    protected String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (this.enclosingEntry != null) {
            this.enclosingEntry.invalidateCached();
        }
    }


    public AlphEntry getEnclosingEntry() {
        return enclosingEntry;
    }

    public void setEnclosingEntry(AlphEntry enclosingEntry) {
        this.enclosingEntry = enclosingEntry;
    }
}
