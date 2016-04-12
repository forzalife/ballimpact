package com.phamhungan.ballimpact.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by MrAn PC on 14-Feb-16.
 */
public class EventUtil {
    public static void backPressExitApp(Activity activity){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(a);
    }

    public static void makeToast(Context context,String message){
        Boast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
