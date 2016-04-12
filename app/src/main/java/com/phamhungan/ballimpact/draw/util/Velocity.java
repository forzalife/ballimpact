package com.phamhungan.ballimpact.draw.util;

import com.phamhungan.ballimpact.draw.DrawBall;

import processing.core.PVector;

/**
 * Created by Mr An on 10/03/2016.
 */
public class Velocity {
    private static PVector [] pVectorArray = new PVector[2];
    private static PVector pVector1 = new PVector();
    private static PVector pVector2 = new PVector();
    private static PVector[] bFinal = {new PVector(), new PVector()};
    private static PVector[] vFinal = {new PVector(), new PVector()};
    private static PVector[] vTemp = {new PVector(), new PVector()};
    private static PVector[] bTemp = {new PVector(), new PVector()};
    private static PVector bVect;
    private static float bVectMag;
    private static float theta;
    private static float sine;
    private static float cosine;

    public static PVector[] getNewVelocity(DrawBall ball1,DrawBall ball2){
        bVect = PVector.sub(ball1.getPosition(), ball2.getPosition());

        // calculate magnitude of the vector separating the balls
        bVectMag = bVect.mag();
        // get angle of bVect
        theta  = bVect.heading2D();
        // precalculate trig values
        sine = (float)Math.sin(theta);
        cosine = (float)Math.cos(theta);

      /* bTemp will hold rotated ball positions. You
       just need to worry about bTemp[1] position*/

        /* this ball's position is relative to the other
         so you can use the vector between them (bVect) as the
         reference point in the rotation expressions.
         bTemp[0].position.x and bTemp[0].position.y will initialize
         automatically to 0.0, which is what you want
         since b[1] will rotate around b[0] */
        bTemp[1].x  = cosine * bVect.x + sine * bVect.y;
        bTemp[1].y  = cosine * bVect.y - sine * bVect.x;

        // rotate Temporary velocities

        vTemp[0].x  = cosine * ball1.getVelocity().x + sine * ball1.getVelocity().y;
        vTemp[0].y  = cosine * ball1.getVelocity().y - sine * ball1.getVelocity().x;
        vTemp[1].x  = cosine * ball2.getVelocity().x + sine * ball2.getVelocity().y;
        vTemp[1].y  = cosine * ball2.getVelocity().y - sine * ball2.getVelocity().x;

      /* Now that velocities are rotated, you can use 1D
       conservation of momentum equations to calculate
       the final velocity along the x-axis. */

        // final rotated velocity for b[0]
        vFinal[0].x = (float)(((ball1.getWeight() - ball2.getWeight()) * vTemp[0].x + 2 * ball2.getWeight() * vTemp[1].x)
                / (ball1.getWeight() + ball2.getWeight()));
        vFinal[0].y = vTemp[0].y;

        // final rotated velocity for b[0]
        vFinal[1].x = (float)(((ball2.getWeight() - ball1.getWeight()) * vTemp[1].x + 2 * ball1.getWeight() * vTemp[0].x)
                / (ball1.getWeight() + ball2.getWeight()));
        vFinal[1].y = vTemp[1].y;

        // hack to avoid clumping
        bTemp[0].x += vFinal[0].x;
        bTemp[1].x += vFinal[1].x;

      /* Rotate ball positions and velocities back
       Reverse signs in trig expressions to rotate
       in the opposite direction */
        // rotate balls

        bFinal[0].x = cosine * bTemp[0].x - sine * bTemp[0].y;
        bFinal[0].y = cosine * bTemp[0].y + sine * bTemp[0].x;
        bFinal[1].x = cosine * bTemp[1].x - sine * bTemp[1].y;
        bFinal[1].y = cosine * bTemp[1].y + sine * bTemp[1].x;

//        // update balls to screen position
//        position[1].x = position[0].x + bFinal[1].x;
//        position[1].y = position[0].y + bFinal[1].y;
//
//        position[0].add(bFinal[0]);

        // update velocities
        pVector1.x = cosine * vFinal[0].x - sine * vFinal[0].y;
        pVector1.y = cosine * vFinal[0].y + sine * vFinal[0].x;
        pVector2.x = cosine * vFinal[1].x - sine * vFinal[1].y;
        pVector2.y = cosine * vFinal[1].y + sine * vFinal[1].x;

        pVectorArray[0] = pVector1;
        pVectorArray[1] = pVector2;

        return pVectorArray;
    }
}
