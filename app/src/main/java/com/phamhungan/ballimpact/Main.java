package com.phamhungan.ballimpact;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.phamhungan.ballimpact.database.Database;
import com.phamhungan.ballimpact.model.Ball;
import com.phamhungan.ballimpact.model.Config;
import com.phamhungan.ballimpact.model.Line;
import com.phamhungan.ballimpact.model.Table;
import com.phamhungan.ballimpact.util.DataUtil;
import com.phamhungan.ballimpact.util.EventUtil;
import com.phamhungan.ballimpact.ui.ChooserActivity;
import com.phamhungan.ballimpact.util.MyAnimation;
import com.phamhungan.ballimpact.util.ResizeBitmap;
import com.phamhungan.ballimpact.util.ScreenUtil;

import java.util.List;

public class Main extends AppCompatActivity {
    private final String TAG = "Main";
    final Handler handler = new Handler();
    private ImageView imgSplash;
    private LinearLayout lnMain;
    private final Activity main = this;
    private Database database = new Database(this);
    private float screenWidth;
    private Thread thread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.splash_screen);
        screenWidth = ScreenUtil.getScreenWidth(getWindowManager());
        imgSplash = (ImageView)findViewById(R.id.imgSplash);
        lnMain = (LinearLayout)findViewById(R.id.lnMain);
        imgSplash.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.splash_screen),
                ScreenUtil.getScreenWidth(getWindowManager())));
        imgSplash.startAnimation(MyAnimation.fadeIn(main));
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getConfig();
            }
        });
        thread.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = MyAnimation.fadeOut(main);
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        lnMain.clearAnimation();
                        lnMain.removeAllViews();
                        Intent in = new Intent(Main.this, ChooserActivity.class);
                        startActivity(in);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                lnMain.startAnimation(animation);
            }
        }, 1500);
    }

    private void getConfig() {
        database.open();
        int isDBExist = database.getConfigCount();
        if(isDBExist==0){
            Log.d(TAG,"Not config yet,create new");
            createNewConfig();
        }else {
            Log.d(TAG,"Have been config");
            Config config = database.getConfig(getResources().getString(R.string.configId));
            DataUtil.config = config;
            insertListBall(false);
            insertListTable(false);
            insertListLine(false);
        }
        database.close();
    }

    private Config createNewConfig(){
        Config config = new Config();
        config.setConfigId(getResources().getString(R.string.configId));
        config.setBallPlayer1Id(R.drawable.ball_red);
        config.setBallPlayer2Id(R.drawable.ball_blue);
        config.setTableId(R.color.green_700);
        config.setLineId(R.color.grey_400);
        config.setPlayer1Name(getResources().getString(R.string.player1));
        config.setPlayer2Name(getResources().getString(R.string.player2));
        database.insertConfig(config);
        DataUtil.config = config;
        Log.d(TAG,"New config");
        insertListBall(true);
        insertListTable(true);
        insertListLine(true);
        return config;
    }

    private void insertListLine(boolean insertDB) {
        float lineWidth = screenWidth/10;
        float lineHeight = screenWidth/10;
        List<Integer> listLine = DataUtil.getListLineId();
        for (Integer i : listLine){
            Line line = new Line();
            line.setLineId(i);
            if(i==DataUtil.config.getLineId()){
                line.setIsSelected(1);
            }
            else {
                line.setIsSelected(0);
            }
            int myColor = getResources().getColor(i);
            Bitmap image = Bitmap.createBitmap((int) lineWidth, (int) lineHeight, Bitmap.Config.ARGB_8888);
            image.eraseColor(myColor);
            line.setLineBitmap(image);
            if(insertDB){
                database.insertLine(line);
            }
            DataUtil.addListLine(line);
        }
        Log.d(TAG, "Insert line success");
    }

    private void insertListBall(boolean insertDB) {
        List<Integer> listBall = DataUtil.getListBallId();
        for (Integer i : listBall){
            Ball ball = new Ball();
            ball.setBallId(i);
            ball.setLocked(0);
            if(i==DataUtil.config.getBallPlayer1Id()){
                ball.setIsP1Selected(1);
                ball.setIsP2Selected(0);
            }
            if(i==DataUtil.config.getBallPlayer2Id()){
                ball.setIsP1Selected(0);
                ball.setIsP2Selected(1);
            }
            Bitmap bpTemp = ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(),i),screenWidth/8);
            ball.setBallBitmap(bpTemp);
            if(insertDB){
                database.insertBall(ball);
            }
            DataUtil.addListBall(ball);
        }
        Log.d(TAG,"Insert ball success");
    }

    private void insertListTable(boolean insertDB) {
        float tableWidth = screenWidth/8;
        float tableHeight = screenWidth/8*3/2;
        List<Integer> listTable = DataUtil.getListTableId();
        for (Integer i : listTable){
            Table table = new Table();
            table.setTableId(i);
            table.setLocked(0);
            if(i==DataUtil.config.getTableId()){
                table.setIsSelected(1);
            }
            else {
                table.setIsSelected(0);
            }
            Bitmap bpTemp = DataUtil.drawTable(getApplicationContext(),i,tableWidth, tableHeight);
            table.setTableBitmap(bpTemp);
            if(insertDB){
                database.insertTable(table);
            }
            DataUtil.addListTable(table);
        }
        Log.d(TAG, "Insert table success");
    }

    @Override
    public void onBackPressed() {
        EventUtil.backPressExitApp(this);
    }
}
