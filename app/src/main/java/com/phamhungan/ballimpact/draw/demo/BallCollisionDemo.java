package com.phamhungan.ballimpact.draw.demo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.util.ResizeBitmap;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;


/**
 * Created by Mr An on 05/03/2016.
 */
public class BallCollisionDemo extends View {
    Bitmap ballRed;
    Bitmap ballBlue;
    float screenWidth;
    float screenHeight;
    PVector[] position = new PVector[2];
    PVector[] velocity = new PVector[2];
    double[] weight = new double[2];
    double[] RADIUS = new double[2];
    double[] div = new double[2];
    double[] divTemp = new double[2];
    double[] speed = new double[2];
    private Activity activity;
    boolean isCondition = false;
    public static List<Ball> listBall = new ArrayList<>();
    private boolean ballRun = false;


    public BallCollisionDemo(Context context,Activity activity) {
        super(context);
        this.activity = activity;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w-1;
        screenHeight = h-1;

        RADIUS[0] = screenWidth/8;
        RADIUS[1] = screenWidth/24;
        weight[0] = (RADIUS[0]*.1d);
        weight[1] = (RADIUS[1]*.1d);
        speed[0]=3;
        speed[1]=5;
        divTemp[0] =1d/((speed[0]*10)*(int) weight[0]);
        divTemp[1] =1d/((speed[1]*10)*(int) weight[1]);
        div[0] = 1;
        div[1] = 1;
        ballRed = ResizeBitmap.resize(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ball_red), screenWidth / 8);
        ballBlue = ResizeBitmap.resize(BitmapFactory.decodeResource(activity.getResources(), R.drawable.ball_blue), screenWidth / 8);
        position[0] = new PVector((float)(screenWidth/16 + 800 * Math.random()),(float)(screenHeight/16 + 1500 * Math.random()));
        position[1] = new PVector((float)(screenWidth/16 + 800 * Math.random()),(float)(screenHeight/16 + 1500 * Math.random()));
        velocity[0] = new PVector((float)(10 * Math.random() - 5),(float)(10 * Math.random() - 5));
        velocity[1] = new PVector((float)(10 * Math.random() - 5),(float)(10 * Math.random() - 5));
        velocity[0].mult((float) speed[0]);
        velocity[1].mult((float) speed[1]);

        listBall.clear();

        listBall.add(new Ball(ballRed,position[0],velocity[0],weight[0],RADIUS[0],div[0],divTemp[0],speed[0],screenWidth,screenHeight));
        listBall.add(new Ball(ballBlue,position[1],velocity[1],weight[1],RADIUS[1],div[1],divTemp[1],speed[1],screenWidth,screenHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Ball ball : listBall){
            ball.draw(canvas);
        }

        update();
        invalidate();
    }

    private void update() {
        for (Ball ball : listBall){
            ball.update();
        }
        for (Ball ball : listBall){
            ball.checkCollisiton();
        }
    }

//    private void runCondition() {
//        div0 -= divTemp0;
//        div1 -= divTemp1;
//        if(div0 >0){
//            velocity[1].mult((float) div0);
//        }else {
//            velocity[1].set(0,0,0);
//        }
//        if(div1 >0){
//            velocity[0].mult((float) div1);
//        }else {
//            velocity[0].set(0,0,0);
//        }
//    }

    void checkCollision() {
        for (int i = 1;i<position.length;i++){
            // get distances between the balls components
            PVector bVect = PVector.sub(position[i], position[0]);

            // calculate magnitude of the vector separating the balls
            float bVectMag = bVect.mag();

            if (bVectMag < RADIUS[0] + RADIUS[1]) {
                // get angle of bVect
                float theta  = bVect.heading2D();
                // precalculate trig values
                float sine = (float)Math.sin(theta);
                float cosine = (float)Math.cos(theta);

      /* bTemp will hold rotated ball positions. You
       just need to worry about bTemp[1] position*/
                PVector[] bTemp = {
                        new PVector(), new PVector()
                };

        /* this ball's position is relative to the other
         so you can use the vector between them (bVect) as the
         reference point in the rotation expressions.
         bTemp[0].position.x and bTemp[0].position.y will initialize
         automatically to 0.0, which is what you want
         since b[1] will rotate around b[0] */
                bTemp[1].x  = cosine * bVect.x + sine * bVect.y;
                bTemp[1].y  = cosine * bVect.y - sine * bVect.x;

                // rotate Temporary velocities
                PVector[] vTemp = {
                        new PVector(), new PVector()
                };

                vTemp[0].x  = cosine * velocity[0].x + sine * velocity[0].y;
                vTemp[0].y  = cosine * velocity[0].y - sine * velocity[0].x;
                vTemp[1].x  = cosine * velocity[1].x + sine * velocity[1].y;
                vTemp[1].y  = cosine * velocity[1].y - sine * velocity[1].x;

      /* Now that velocities are rotated, you can use 1D
       conservation of momentum equations to calculate
       the final velocity along the x-axis. */
                PVector[] vFinal = {
                        new PVector(), new PVector()
                };

                // final rotated velocity for b[0]
                vFinal[0].x = (float)(((weight[0] - weight[1]) * vTemp[0].x + 2 * weight[1] * vTemp[1].x) / (weight[0] + weight[1]));
                vFinal[0].y = vTemp[0].y;

                // final rotated velocity for b[0]
                vFinal[1].x = (float)(((weight[1] - weight[0]) * vTemp[1].x + 2 * weight[0] * vTemp[0].x) / (weight[0] + weight[1]));
                vFinal[1].y = vTemp[1].y;

                // hack to avoid clumping
                bTemp[0].x += vFinal[0].x;
                bTemp[1].x += vFinal[1].x;

      /* Rotate ball positions and velocities back
       Reverse signs in trig expressions to rotate
       in the opposite direction */
                // rotate balls
                PVector[] bFinal = {
                        new PVector(), new PVector()
                };

                bFinal[0].x = cosine * bTemp[0].x - sine * bTemp[0].y;
                bFinal[0].y = cosine * bTemp[0].y + sine * bTemp[0].x;
                bFinal[1].x = cosine * bTemp[1].x - sine * bTemp[1].y;
                bFinal[1].y = cosine * bTemp[1].y + sine * bTemp[1].x;

                // update balls to screen position
//            position[1].x = position[0].x + bFinal[1].x;
//            position[1].y = position[0].y + bFinal[1].y;
//
//            position[0].add(bFinal[0]);

                // update velocities
                velocity[0].x = cosine * vFinal[0].x - sine * vFinal[0].y;
                velocity[0].y = cosine * vFinal[0].y + sine * vFinal[0].x;
                velocity[1].x = cosine * vFinal[1].x - sine * vFinal[1].y;
                velocity[1].y = cosine * vFinal[1].y + sine * vFinal[1].x;
                isCondition=true;
            }
        }
    }
}
