package com.phamhungan.ballimpact.model;

import android.graphics.Bitmap;

/**
 * Created by MrAn PC on 18-Feb-16.
 */
public class Ball {
    private int ballId;
    private int locked;
    private Bitmap ballBitmap;
    private int isP1Selected;
    private int isP2Selected;

    public Ball() {
    }

    public int getIsP1Selected() {
        return isP1Selected;
    }

    public void setIsP1Selected(int isP1Selected) {
        this.isP1Selected = isP1Selected;
    }

    public int getIsP2Selected() {
        return isP2Selected;
    }

    public void setIsP2Selected(int isP2Selected) {
        this.isP2Selected = isP2Selected;
    }

    public Bitmap getBallBitmap() {
        return ballBitmap;
    }

    public void setBallBitmap(Bitmap ballBitmap) {
        this.ballBitmap = ballBitmap;
    }

    public int getBallId() {
        return ballId;
    }

    public void setBallId(int ballId) {
        this.ballId = ballId;
    }

    public int getLocked() {
        return locked;
    }

    public void setLocked(int locked) {
        this.locked = locked;
    }
}
