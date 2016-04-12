package com.phamhungan.ballimpact.model;

import android.graphics.Bitmap;

/**
 * Created by Mr An on 27/02/2016.
 */
public class Line {
    private int lineId;
    private int isSelected;
    private Bitmap lineBitmap;

    public Bitmap getLineBitmap() {
        return lineBitmap;
    }

    public void setLineBitmap(Bitmap lineBitmap) {
        this.lineBitmap = lineBitmap;
    }

    public int getLineId() {
        return lineId;
    }

    public void setLineId(int lineId) {
        this.lineId = lineId;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }
}
