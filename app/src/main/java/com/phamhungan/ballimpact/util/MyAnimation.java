package com.phamhungan.ballimpact.util;

import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.phamhungan.ballimpact.R;

/**
 * Created by MrAn PC on 27-Jan-16.
 */
public class MyAnimation {

    public static Animation sliceInToTopLong(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_in_bottom_to_top_long);
    }

    public static Animation sliceOutToBottom(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_out_top_to_bottom);
    }

    public static Animation sliceOutToTop(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_out_bottom_to_top);
    }

    public static Animation sliceInToTop(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_in_bottom_to_top);
    }

    public static Animation sliceOutToLeft(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_out_right_to_left);
    }

    public static Animation sliceOutToRight(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_out_left_to_right);
    }

    public static Animation sliceInToLeft(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_in_right_to_left);
    }

    public static Animation sliceInToRight(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.slide_in_left_to_right);
    }

    public static Animation fadeOut(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fade_out_long);
    }

    public static Animation fadeIn(Activity activity){
        return AnimationUtils.loadAnimation(activity.getApplicationContext(), R.anim.fade_in_long);
    }
}
