package com.phamhungan.ballimpact.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.model.Ball;
import com.phamhungan.ballimpact.model.Config;
import com.phamhungan.ballimpact.model.Line;
import com.phamhungan.ballimpact.model.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn PC on 18-Feb-16.
 */
public class DataUtil {
    public static final int SELECTED = 1;
    public static final int NOT_SELECTED = 0;
    public static final int LOCKED = 1;
    public static final int UNLOCKED = 0;
    public static Config config;
    public static List<Integer> listBallId = new ArrayList<>();
    public static List<Integer> listTableId = new ArrayList<>();
    public static List<Integer> listLineId = new ArrayList<>();
    public static List<Ball> listBall = new ArrayList<>();
    public static List<Table> listTable = new ArrayList<>();
    public static List<Line> listLine = new ArrayList<>();

    public static void addListBall(Ball ball){
        listBall.add(ball);
    }

    public static void addListTable(Table table){
        listTable.add(table);
    }

    public static void addListLine(Line line){
        listLine.add(line);
    }

    public static Bitmap drawTable(Context context,int colorId,float width,float height) {
        Bitmap output = Bitmap.createBitmap((int)width, (int)height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paintLine = new Paint();
        paintLine.setAntiAlias(true);
        paintLine.setColor(Color.WHITE);
        paintLine.setStyle(Paint.Style.STROKE);
        paintLine.setStrokeJoin(Paint.Join.ROUND);
        paintLine.setStrokeWidth(canvas.getWidth() / 50);

        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.FILL);
        paintCircle.setColor(Color.WHITE);

        Paint paintBackground = new Paint();
        int myColor = context.getResources().getColor(colorId);
        paintBackground.setColor(myColor);
        float circleCenterSize = canvas.getWidth()/25;


        drawBackground(canvas, paintBackground);
        drawBorderTable(canvas, paintLine);
        drawCircleCenter(canvas, circleCenterSize, paintCircle);

        return output;
    }

    private static void drawCircleCenter(Canvas canvas,float circleCenterSise,Paint paintCircle) {
        canvas.drawCircle(canvas.getWidth() / 2, canvas.getHeight() / 2, circleCenterSise, paintCircle);
    }

    private static void drawBackground(Canvas canvas,Paint paintBackground) {
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), paintBackground);
    }

    private static void drawBorderTable(Canvas canvas,Paint paintLine) {
        //Line Top
        canvas.drawLine(0, 0, canvas.getWidth(), 0, paintLine);
        //Line Left
        canvas.drawLine(0, 0, 0, canvas.getHeight(), paintLine);
        //Line Bottom
        canvas.drawLine(0, canvas.getHeight(), canvas.getWidth(), canvas.getHeight(), paintLine);
        //Line Right
        canvas.drawLine(canvas.getWidth(), 0, canvas.getWidth(), canvas.getHeight(), paintLine);
        //Line Center
        paintLine.setStrokeWidth(canvas.getWidth()/100);
        canvas.drawLine(0, canvas.getHeight() / 2, canvas.getWidth(), canvas.getHeight() / 2, paintLine);
    }

    public static void updateSelectedListTable(int tableId){
        for (Table table : listTable){
            if(table.getTableId()==tableId)
                table.setIsSelected(SELECTED);
            else {
                table.setIsSelected(NOT_SELECTED);
            }
        }
    }

    public static void updateSelectedListLine(int lineId){
        for (Line line : listLine){
            if(line.getLineId()==lineId)
                line.setIsSelected(SELECTED);
            else {
                line.setIsSelected(NOT_SELECTED);
            }
        }
    }

    public static void updateSelectedListBall(int ballId,int p1Value,int p2Value){
        for (Ball ball : listBall){
            if(ball.getBallId()==ballId){
                if(p1Value==SELECTED){
                    ball.setIsP1Selected(p1Value);
                }else if(p2Value==SELECTED){
                    ball.setIsP2Selected(p2Value);
                }
            }else {
                if(p1Value==SELECTED){
                    ball.setIsP1Selected(NOT_SELECTED);
                }else if(p2Value==SELECTED){
                    ball.setIsP2Selected(NOT_SELECTED);
                }
            }
        }
    }

    public static List<Integer> getListBallId(){
        listBallId.clear();
        listBallId.add(R.drawable.ball_blue);
        listBallId.add(R.drawable.ball_red);
        listBallId.add(R.drawable.ball_yellow);
        listBallId.add(R.drawable.ball_purple);
        listBallId.add(R.drawable.ball_brown);
        listBallId.add(R.drawable.ball_pink);
        listBallId.add(R.drawable.ball_orange);
        listBallId.add(R.drawable.ball_black);

        return listBallId;
    }

    public static List<Integer> getListTableId(){
        listTableId.clear();
        listTableId.add(R.color.green_700);
        listTableId.add(R.color.blue_700);
        listTableId.add(R.color.brown_700);
        listTableId.add(R.color.grey_400);
        listTableId.add(R.color.orange_700);
        listTableId.add(R.color.pink_700);
        listTableId.add(R.color.purple_700);
        listTableId.add(R.color.red_700);
        listTableId.add(R.color.yellow_600);

        return listTableId;
    }

    public static List<Integer> getListLineId(){
        listLineId.clear();
        listLineId.add(R.color.green_700);
        listLineId.add(R.color.blue_700);
        listLineId.add(R.color.brown_700);
        listLineId.add(R.color.grey_400);
        listLineId.add(R.color.orange_700);
        listLineId.add(R.color.pink_700);
        listLineId.add(R.color.purple_700);
        listLineId.add(R.color.red_700);
        listLineId.add(R.color.yellow_600);

        return listTableId;
    }
}
