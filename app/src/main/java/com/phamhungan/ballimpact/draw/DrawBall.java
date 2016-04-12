package com.phamhungan.ballimpact.draw;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import com.phamhungan.ballimpact.draw.util.Velocity;

import processing.core.PVector;

/**
 * Created by Mr An on 26/02/2016.
 */
public class DrawBall implements Draw {
    private Bitmap ball;
    private Rect rect;
    private PVector position;
    private float speed = 0;
    private double xDes, yDes;
    private PVector velocity;
    private double RADIUS;
    private double weight;
    private String owner;
    private double div;
    private double divTemp;

    public DrawBall(Bitmap ball, PVector position, String owner) {
        this.ball = ball;
        this.position = position;
        this.owner = owner;
        RADIUS = ball.getWidth() / 2;
        velocity = new PVector();
        weight = RADIUS * .1;
    }

    @Override
    public void draw(Canvas canvas) {
        rect = new Rect((int) (position.x - RADIUS), (int) (position.y - RADIUS),
                (int) (position.x + RADIUS), (int) (position.y + RADIUS));
        canvas.drawBitmap(ball, null, rect, null);
    }

    public boolean clickMe(MotionEvent event) {
        if ((event.getX() >= (position.x - RADIUS) && event.getX() <= (position.x + RADIUS))
                && (event.getY() >= (position.y - RADIUS) && event.getY() <= (position.y + RADIUS))) {
            return true;
        }
        return false;
    }

    public String getOwner() {
        return owner;
    }

    public PVector getPosition() {
        return position;
    }

    public double getRADIUS() {
        return RADIUS;
    }

    public void prepareLineBallRun(float speed) {
        this.speed = speed / 6;
        xDes = (int) DrawLine.oppositePoint.x;
        yDes = (int) DrawLine.oppositePoint.y;
        velocity.set((float) (xDes - position.x), (float) (yDes - position.y), 0);
        velocity.mult(this.speed);
        div=1;
        divTemp = 1d/(speed);
    }

    public void setCollisionRun(float speed) {
        this.div = 1;
        this.divTemp = 1d/(speed*6);;
    }

    public PVector getVelocity() {
        return velocity;
    }

    public void setVelocity(float x,float y){
        velocity.set(x,y,0);
    }

    public void checkCollisiton() {
        for (DrawBall ball2 : DrawMain.listBall){
            if(this!=ball2){
                // Get distances between the balls components
                PVector bVect = PVector.sub(getPosition(), ball2.getPosition());

                // Calculate magnitude of the vector separating the balls
                float bVectMag = bVect.mag();

                if (bVectMag < getRADIUS() + ball2.getRADIUS()) {
                    Log.d("Dung","Roi");
                    // Get angle of bVect
                    float theta = bVect.heading2D();
                    // Precalculate trig values
                    float sine = (float) Math.sin(theta);
                    float cosine = (float) Math.cos(theta);

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
                    bTemp[1].x = cosine * bVect.x + sine * bVect.y;
                    bTemp[1].y = cosine * bVect.y - sine * bVect.x;

                    // rotate Temporary velocities
                    PVector[] vTemp = {
                            new PVector(), new PVector()
                    };

                    vTemp[0].x = cosine * getVelocity().x + sine * getVelocity().y;
                    vTemp[0].y = cosine * getVelocity().y - sine * getVelocity().x;
                    vTemp[1].x = cosine * ball2.getVelocity().x + sine * ball2.getVelocity().y;
                    vTemp[1].y = cosine * ball2.getVelocity().y - sine * ball2.getVelocity().x;

      /* Now that velocities are rotated, you can use 1D
       conservation of momentum equations to calculate
       the final velocity along the x-axis. */
                    PVector[] vFinal = {
                            new PVector(), new PVector()
                    };

                    // final rotated velocity for b[0]
                    vFinal[0].x = (float) (((getWeight() - ball2.getWeight()) * vTemp[0].x + 2 * ball2.getWeight() * vTemp[1].x) / (getWeight() + ball2.getWeight()));
                    vFinal[0].y = vTemp[0].y;

                    // final rotated velocity for b[0]
                    vFinal[1].x = (float) (((ball2.getWeight() - getWeight()) * vTemp[1].x + 2 * getWeight() * vTemp[0].x) / (getWeight() + ball2.getWeight()));
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

                    // update velocities
                    setVelocity(cosine * vFinal[0].x - sine * vFinal[0].y,cosine * vFinal[0].y + sine * vFinal[0].x);
                    ball2.setVelocity(cosine * vFinal[1].x - sine * vFinal[1].y, cosine * vFinal[1].y + sine * vFinal[1].x);
                }
            }
        }
    }

    public Bitmap getBitmap() {
        return ball;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public void update() {
//        div =div-divTemp;
//        if(div>0){
//            velocity.mult((float)div);
            position.add(velocity);
//        }
    }

    public float getSpeed() {
        return speed;
    }

    public double getDiv() {
        return div;
    }

    public double getDivTemp() {
        return divTemp;
    }
}
