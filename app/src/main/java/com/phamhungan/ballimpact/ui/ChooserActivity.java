package com.phamhungan.ballimpact.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.util.EventUtil;
import com.phamhungan.ballimpact.util.MyAnimation;
import com.phamhungan.ballimpact.util.ResizeBitmap;
import com.phamhungan.ballimpact.util.ScreenUtil;

/**
 * Created by MrAn PC on 13-Feb-16.
 */
public class ChooserActivity extends AppCompatActivity{
    private final String TAG = "ChooserActivity";
    private ImageView imgContent;
    private ImageView imgPlay;
    private ImageView imgOptions;
    private ImageView imgExit;
    private LinearLayout lnChoose;
    private LinearLayout lnImgSplash;
    private float screenWidth;
    private final Activity chooserActivity = this;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screenWidth = ScreenUtil.getScreenWidth(getWindowManager());
        initLayout();
        setOnClick();
    }

    private void setOnClick() {
        imgOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ChooserActivity.this, OptionActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        imgExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventUtil.backPressExitApp(chooserActivity);
            }
        });

        imgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(ChooserActivity.this, GameActivity.class);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    private void initLayout() {
        imgContent = (ImageView)findViewById(R.id.imgContent);
        imgPlay = (ImageView)findViewById(R.id.imgPlay);
        imgOptions = (ImageView)findViewById(R.id.imgOption);
        imgExit = (ImageView)findViewById(R.id.imgExit);
        lnChoose = (LinearLayout)findViewById(R.id.lnChoose);
        lnImgSplash = (LinearLayout)findViewById(R.id.lnImgSplash);
        startAnimation();
    }

    private void startAnimation() {
        imgContent.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.splash_screen), screenWidth));
        lnImgSplash.startAnimation(MyAnimation.fadeIn(chooserActivity));
        Animation animation = MyAnimation.sliceInToTopLong(chooserActivity);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lnChoose.clearAnimation();
                Log.d(TAG,"Clear Animations");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        lnChoose.startAnimation(animation);
        imgPlay.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.background_label), screenWidth / 2));
        imgOptions.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.background_label), screenWidth * 4 / 10));
        imgExit.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.background_label), screenWidth * 3 / 10));
    }

    @Override
    public void onBackPressed() {
        EventUtil.backPressExitApp(this);
    }
}
