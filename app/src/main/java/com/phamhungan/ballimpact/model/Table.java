package com.phamhungan.ballimpact.model;

import android.graphics.Bitmap;

/**
 * Created by MrAn PC on 18-Feb-16.
 */
public class Table {
    private int tableId;
    private int locked;
    private Bitmap tableBitmap;
    private int isSelected;

    public Table() {
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public Bitmap getTableBitmap() {
        return tableBitmap;
    }

    public void setTableBitmap(Bitmap tableBitmap) {
        this.tableBitmap = tableBitmap;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
