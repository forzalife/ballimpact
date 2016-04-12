package com.phamhungan.ballimpact.draw.demo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import processing.core.PVector;

/**
 * Created by Mr An on 14/03/2016.
 */
public class Ball {
    Bitmap ballBitmap;
    Rect rect;
    PVector position = new PVector();
    PVector velocity = new PVector();
    double weight;
    double RADIUS;
    double div;
    double divTemp;
    double speed;
    float screenWidth;
    float screenHeight;

    public Ball(Bitmap ballBitmap, PVector position, PVector velocity, double weight, double RADIUS, double div, double divTemp, double speed,float screenWidth,float screenHeight) {
        this.ballBitmap = ballBitmap;
        this.position = position;
        this.velocity = velocity;
        this.weight = weight;
        this.RADIUS = RADIUS;
        this.div = div;
        this.divTemp = divTemp;
        this.speed = speed;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    public void draw (Canvas canvas){
        rect = new Rect((int)(position.x-RADIUS),(int)(position.y-RADIUS),(int)(position.x+RADIUS),(int)(position.y+RADIUS));
        canvas.drawBitmap(ballBitmap, null, rect, null);
    }

    public void update(){
        ballRun();
        runInFrame();
    }

    public void checkCollisiton() {
        for (final Ball ball : BallCollisionDemo.listBall){
            if(ball.hashCode()!=hashCode()){
                // get distances between the balls components
                PVector bVect = PVector.sub(position, ball.position);

                // calculate magnitude of the vector separating the balls
                float bVectMag = bVect.mag();

                if (bVectMag < RADIUS + ball.RADIUS) {
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

                    vTemp[0].x  = cosine * velocity.x + sine * velocity.y;
                    vTemp[0].y  = cosine * velocity.y - sine * velocity.x;
                    vTemp[1].x  = cosine * ball.velocity.x + sine * ball.velocity.y;
                    vTemp[1].y  = cosine * ball.velocity.y - sine * ball.velocity.x;

      /* Now that velocities are rotated, you can use 1D
       conservation of momentum equations to calculate
       the final velocity along the x-axis. */
                    PVector[] vFinal = {
                            new PVector(), new PVector()
                    };

                    // final rotated velocity for b[0]
                    vFinal[0].x = (float)(((weight - ball.weight) * vTemp[0].x + 2 * ball.weight * vTemp[1].x) / (weight + ball.weight));
                    vFinal[0].y = vTemp[0].y;

                    // final rotated velocity for b[0]
                    vFinal[1].x = (float)(((ball.weight - weight) * vTemp[1].x + 2 * weight * vTemp[0].x) / (weight + ball.weight));
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
                    velocity.x = cosine * vFinal[0].x - sine * vFinal[0].y;
                    velocity.y = cosine * vFinal[0].y + sine * vFinal[0].x;
                    ball.velocity.x = cosine * vFinal[1].x - sine * vFinal[1].y;
                    ball.velocity.y = cosine * vFinal[1].y + sine * vFinal[1].x;
                }
            }
        }
    }

    private void ballRun() {
        position.add(velocity);
    }

    private void runInFrame() {
        for (int i = 0;i<3;i++){
            if (position.x > screenWidth-RADIUS) {
                position.x = (float)(screenWidth-RADIUS);
                velocity.x *= -1;
            }
            else if (position.x < RADIUS) {
                position.x = (float)RADIUS;
                velocity.x *= -1;
            }
            else if (position.y > screenHeight-RADIUS) {
                position.y = (float)(screenHeight-RADIUS);
                velocity.y *= -1;
            }
            else if (position.y < RADIUS) {
                position.y = (float)RADIUS;
                velocity.y *= -1;
            }
        }
    }
}
