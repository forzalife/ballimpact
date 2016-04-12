package com.phamhungan.ballimpact.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.phamhungan.ballimpact.R;

/**
 * Created by MrAn PC on 21-Feb-16.
 */
public class MyHolder extends RecyclerView.ViewHolder {
    public ImageView imgBall;
    public ImageView imgCheck;
    public MyHolder(View view){
        super(view);
        imgBall = (ImageView)view.findViewById(R.id.imgBall);
        imgCheck = (ImageView)view.findViewById(R.id.imgCheck);
    }
}
