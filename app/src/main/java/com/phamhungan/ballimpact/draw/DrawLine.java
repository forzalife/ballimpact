package com.phamhungan.ballimpact.draw;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.phamhungan.ballimpact.model.RectPosition;
import com.phamhungan.ballimpact.util.DataUtil;

import processing.core.PVector;

/**
 * Created by Mr An on 27/02/2016.
 */
public class DrawLine implements Draw {
    public static SparseArray<PointF> LineRed;
    private Paint paintLine;
    private boolean redFlag =false;
    private float xStart;
    private float yStart;
    private int pointerIndex;
    private int pointerId;
    private int maskedAction;
    public PointF point;
    public static PointF oppositePoint;
    private float changeInX;
    private float changeInY;
    private double distanceCto1;
    private double distanceRatio;
    private double xValue;
    private double yValue;

    public DrawLine(Activity activity){
        oppositePoint = new PointF();
        LineRed = new SparseArray<>();
        paintLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        int myColor = activity.getResources().getColor(DataUtil.config.getLineId());
        paintLine.setColor(myColor);
        paintLine.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void draw(Canvas canvas) {
        paintLine.setStrokeWidth(canvas.getWidth() / 200);
        for (int size = LineRed.size(), i = 0; i < size; i++) {
            point = LineRed.valueAt(i);
            if (point != null)
            {
                getOppositePoint(xStart, yStart, point, 10);
                canvas.drawLine(xStart, yStart, point.x, point.y, paintLine);
            }
        }
    }

    private void getOppositePoint(float xStart,float yStart,PointF pointBegin,double distance){
        changeInX  = pointBegin.x - xStart;
        changeInY  = pointBegin.y - yStart;
        distanceCto1 = Math.pow(
                (Math.pow(changeInX,2.0) + Math.pow(changeInY,2.0))
                ,0.5);
        distanceRatio = distance/distanceCto1;
        xValue = distanceRatio * changeInX;
        yValue = distanceRatio * changeInY;
        oppositePoint.set((float)(xStart-xValue),(float)(yStart-yValue));
    }

    @Override
    public void update() {

    }

    public void update(MotionEvent event){
        pointerIndex = event.getActionIndex();
        pointerId = event.getPointerId(pointerIndex);
        maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_MOVE: {
                updateNewPoint(event);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                if(redFlag){
                    removeLine();
                    break;
                }
            }
        }
    }

    public void update(PVector pVector,MotionEvent event){
        xStart = pVector.x;
        yStart = pVector.y;
        pointerIndex = event.getActionIndex();
        pointerId = event.getPointerId(pointerIndex);
        maskedAction = event.getActionMasked();
        switch (maskedAction) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:{
                redFlag =true;
                point = new PointF();
                point.x = event.getX(pointerIndex);
                point.y = event.getY(pointerIndex);
                LineRed.put(pointerId, point);
                break;
            }
            case MotionEvent.ACTION_MOVE: { // a pointer was moved
                updateNewPoint(event);
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:{
                if(redFlag){
                    removeLine();
                    break;
                }
            }
        }
    }

    private void removeLine(){
        LineRed.remove(pointerId);
        redFlag =false;
    }

    private void updateNewPoint(MotionEvent event){
        if(redFlag)
        {
            for (int size = event.getPointerCount(), i = 0; i < size; i++)
            {
                point = LineRed.get(event.getPointerId(i));
                if (point != null)
                {
                    point.x = event.getX(i);
                    point.y = event.getY(i);
                }
            }
        }
    }
}
