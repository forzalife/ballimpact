package com.phamhungan.ballimpact.draw;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.util.DataUtil;

/**
 * Created by MrAn PC on 07-Feb-16.
 */
public class DrawTable implements Draw{
    private Activity activity;
    public DrawTable(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(DataUtil.drawTable(activity.getApplicationContext(),DataUtil.config.getTableId(),canvas.getWidth(),canvas.getHeight()),
                null,new Rect(0,0,canvas.getWidth(),canvas.getHeight()),null);
    }

    @Override
    public void update() {

    }
}
