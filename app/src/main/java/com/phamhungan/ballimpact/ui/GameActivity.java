package com.phamhungan.ballimpact.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.draw.demo.BallCollisionDemo;
import com.phamhungan.ballimpact.draw.DrawMain;
import com.phamhungan.ballimpact.util.DataUtil;
import com.phamhungan.ballimpact.util.MyAnimation;

/**
 * Created by Mr An on 26/02/2016.
 */
public class GameActivity extends AppCompatActivity {
    public static final int PLAYER1 = 1;
    public static final int PLAYER2 = 2;
    private LinearLayout lnMain;
    private TextView txtPlayer1Name;
    private TextView txtPlayer2Name;
    public static ProgressBar pbPowerP1;
    public static ProgressBar pbPowerP2;
    private boolean isFullScreen = false;
    private DrawMain drawMain;
    private static Thread pbThread;
    private static final String TAG = "GameActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideSystemUI();
        setContentView(R.layout.activity_game);
        lnMain = (LinearLayout)findViewById(R.id.lnMain);
        txtPlayer1Name = (TextView)findViewById(R.id.txtPlayer1Name);
        txtPlayer2Name = (TextView)findViewById(R.id.txtPlayer2Name);
        pbPowerP1 = (ProgressBar)findViewById(R.id.pbPowerP1);
        pbPowerP2 = (ProgressBar)findViewById(R.id.pbPowerP2);
        initLayout();
    }

    private void initLayout() {
        txtPlayer1Name.setText(DataUtil.config.getPlayer1Name());
        txtPlayer2Name.setText(DataUtil.config.getPlayer2Name());
//        drawMain = new DrawMain(getApplicationContext(),this);
//        lnMain.addView(drawMain);

        BallCollisionDemo bouncingBallView = new BallCollisionDemo(getApplicationContext(),this);
        lnMain.addView(bouncingBallView);
    }

    @Override
    protected void onStop() {
        super.onStop();
        drawMain = null;
    }

    public static void updateProgressBar(int player,boolean isStart){
        if(isStart){
            if(player==PLAYER1){
                pbThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int i=0;
                            boolean revert = false;
                            while (true){
                                Thread.sleep(15);
                                if(i==100){
                                    revert = true;
                                }
                                if(i==0){
                                    revert=false;
                                }
                                if(revert)
                                    i--;
                                else i++;
                                pbPowerP1.setProgress(i);
                            }
                        }catch (Exception e){
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                pbThread.start();
            }else {
                pbThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d(TAG,"ProgressBar running");
                            int i=0;
                            boolean revert = false;
                            while (true){
                                Thread.sleep(15);
                                if(i==100){
                                    revert = true;
                                }
                                if(i==0){
                                    revert=false;
                                }
                                if(revert)
                                    i--;
                                else i++;
                                pbPowerP2.setProgress(i);
                            }
                        }catch (Exception e){
                            Thread.currentThread().interrupt();
                        }
                    }
                });
                pbThread.start();
            }
        }else {
            stopPbThread();
        }
    }

    private static synchronized void stopPbThread() {
        if (pbThread != null) {
            pbThread.interrupt();
            pbThread = null;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(!isFullScreen){
                        setFullScreen(true);
                    }else {
                        setFullScreen(false);
                    }
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    if(!isFullScreen){
                        setFullScreen(true);
                    }else {
                        setFullScreen(false);
                    }
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private void setFullScreen(boolean fullScreen){
        if(fullScreen){
            hideSystemUI();
            isFullScreen = true;
        }
        else {
            showSystemUI();
            isFullScreen = false;
        }
    }

    // This snippet hides the system bars.
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getWindow().getDecorView().setAnimation(MyAnimation.fadeOut(this));
    }

    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().getDecorView().setAnimation(MyAnimation.fadeIn(this));
    }
}
