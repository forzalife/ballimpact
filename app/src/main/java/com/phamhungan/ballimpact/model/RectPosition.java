package com.phamhungan.ballimpact.model;

/**
 * Created by Mr An on 27/02/2016.
 */
public class RectPosition {
    private float left;
    private float top;
    private float right;
    private float bottom;

    public RectPosition(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    public RectPosition(float left, float top, float right, float bottom) {
        this.left = (int)left;
        this.top = (int)top;
        this.right = (int)right;
        this.bottom = (int)bottom;
    }

    public RectPosition(Double left, Double top, Double right, Double bottom) {
        this.left = left.intValue();
        this.top = top.intValue();
        this.right = right.intValue();
        this.bottom = bottom.intValue();
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }
}
