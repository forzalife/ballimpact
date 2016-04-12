package com.phamhungan.ballimpact.draw;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.ui.GameActivity;
import com.phamhungan.ballimpact.util.DataUtil;
import com.phamhungan.ballimpact.util.EventUtil;
import com.phamhungan.ballimpact.util.ResizeBitmap;
import com.phamhungan.ballimpact.util.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import processing.core.PVector;

/**
 * Created by MrAn PC on 07-Feb-16.
 */
public class DrawMain extends View {
    public static final int P1_TURN = 1;
    public static final int P2_TURN = 2;
    public String PLAYER_1;
    public String PLAYER_2;
    public static final String TAG = "DrawMain";
    public int turn;
    private DrawTable drawTable;
    private DrawLine drawLine;
    public static float screenWidth;
    public static float screenHeight;
    public static List<DrawBall> listBall;
    private List<Bitmap> listBallBitmap;
    private List<PVector> listPositionBall;
    public static int positionBallClicked=-1;
    public static boolean isBallRun = false;
    private Activity activity;
    private final String[] name = new String[1];
    private PVector[] pVectorArray;
    private List<DrawBall> listBallCollision;
    private float speed;
    public DrawMain(Context context,Activity activity) {
        super(context);
        this.activity = activity;
        screenWidth = ScreenUtil.getScreenWidth(activity.getWindowManager());
        screenHeight = ScreenUtil.getScreenHeight(activity.getWindowManager());
        drawTable = new DrawTable(activity);
        drawLine = new DrawLine(activity);
        listBall = new ArrayList<>();
        listBallBitmap = new ArrayList<>();
        listPositionBall = new ArrayList<>();
        PLAYER_1 = DataUtil.config.getPlayer1Name();
        PLAYER_2 = DataUtil.config.getPlayer2Name();
        showSelectTurnDialog(activity);
    }

    /***
     * Select player go first
     * @param activity
     */
    private void showSelectTurnDialog(final Activity activity) {
        final Dialog dialog = new Dialog(activity);
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.custom_dialog,null);
        dialog.setContentView(layout);
        dialog.setTitle(activity.getResources().getString(R.string.select));

        ImageView imgOK = (ImageView) dialog.findViewById(R.id.imgOK);
        imgOK.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(activity.getResources(), R.drawable.background_label), screenWidth * 3 / 10));
        final RadioGroup radioSelect = (RadioGroup)layout.findViewById(R.id.radioSelect);
        for (int i = 0; i < radioSelect .getChildCount(); i++) {
            if(i==0){
                ((RadioButton) radioSelect.getChildAt(i)).setText(DataUtil.config.getPlayer1Name());
            }else {
                ((RadioButton) radioSelect.getChildAt(i)).setText(DataUtil.config.getPlayer2Name());
            }
        }
        imgOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (radioSelect.getCheckedRadioButtonId()) {
                    case R.id.radioP1:
                        turn = P1_TURN;
                        name[0] = DataUtil.config.getPlayer1Name();
                        break;
                    case R.id.radioP2:
                        turn = P2_TURN;
                        name[0] = DataUtil.config.getPlayer2Name();
                        break;
                }
                dialog.dismiss();
                EventUtil.makeToast(activity, name[0] + " go first");
                Log.d(TAG, "Player : " + name[0] + " go first");
            }
        });
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                turn = P1_TURN;
                name[0] = DataUtil.config.getPlayer1Name();
                EventUtil.makeToast(activity, name[0] + " go first");
            }
        });
        dialog.show();
    }

    /**
     * Prepare list ball
     */
    private void prepareListBall() {
        listBall.clear();
        for (int i = 0;i<10;i++){
            if(i<5){
                listBall.add(new DrawBall(listBallBitmap.get(i),listPositionBall.get(i),PLAYER_1));
            }else {
                listBall.add(new DrawBall(listBallBitmap.get(i),listPositionBall.get(i),PLAYER_2));
            }
        }
    }

    private Bitmap createBall(int ballId,float size){
        return ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), ballId), size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw table
        drawTable.draw(canvas);

        //Draw ball
        drawBall(canvas);

        //Draw line
        drawLine.draw(canvas);

        updateBall();

        invalidate();
    }

    /**
     * Draw ball
     */
    private void drawBall(Canvas canvas){
        for (DrawBall drawBall : listBall){
            drawBall.draw(canvas);
        }
    }

    /**
     * Update ball
     */
    private void updateBall() {
        for (DrawBall drawBall : listBall){
            drawBall.update();
        }
        for (DrawBall ball : listBall) {
            ball.checkCollisiton();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0;i<listBall.size();i++){
                    if (listBall.get(i).clickMe(event)){
                        switch (turn){
                            case P1_TURN:
                                if (listBall.get(i).getOwner().equals(PLAYER_1)){
                                    GameActivity.updateProgressBar(GameActivity.PLAYER1, true);
                                    drawLine.update(listPositionBall.get(i), event);
                                    positionBallClicked=i;
                                    break;
                                }else {
                                    EventUtil.makeToast(activity,"Is "+PLAYER_1+" turn !");
                                    positionBallClicked=-1;
                                }
                                break;
                            case P2_TURN:
                                if (listBall.get(i).getOwner().equals(PLAYER_2)){
                                    GameActivity.updateProgressBar(GameActivity.PLAYER2, true);
                                    drawLine.update(listPositionBall.get(i), event);
                                    positionBallClicked=i;
                                    break;
                                }else {
                                    EventUtil.makeToast(activity,"Is "+PLAYER_2+" turn !");
                                    positionBallClicked=-1;
                                }
                                break;
                        }
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if(positionBallClicked!=-1){
                    switch (turn){
                        case P1_TURN:
                            speed = GameActivity.pbPowerP1.getProgress();
                            turn=P2_TURN;
                            break;
                        case P2_TURN:
                            speed = GameActivity.pbPowerP2.getProgress();
                            turn=P1_TURN;
                            break;
                    }
                    listBall.get(positionBallClicked).prepareLineBallRun(speed);
                    positionBallClicked=-1;
                }
                drawLine.update(event);
                GameActivity.updateProgressBar(GameActivity.PLAYER1, false);
                GameActivity.updateProgressBar(GameActivity.PLAYER2, false);
                break;
            case MotionEvent.ACTION_MOVE:
                    drawLine.update(event);
            }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.screenWidth =  w;
        this.screenHeight =  h;
        prepareBitmap();
        prepareBallPosition();
        prepareListBall();
    }

    /**
     * Prepare list ball position
     */
    private void prepareBallPosition() {
        listPositionBall.clear();
        /**
         * Position of player 1 ball
         */
        //Ball size /6 : 01 ball
        PVector pVector = new PVector(screenWidth/2, screenHeight*7/8);
        listPositionBall.add(pVector);
        //Ball size /10 : 02 ball
        pVector = new PVector(screenWidth/5, screenHeight*7/8);
        listPositionBall.add(pVector);

        pVector = new PVector(screenWidth*4/5, screenHeight*7/8 );
        listPositionBall.add(pVector);

        //Ball size /12 : 02 ball
        pVector = new PVector(screenWidth/3, screenHeight*5/8);
        listPositionBall.add(pVector);

        pVector = new PVector(screenWidth*2/3, screenHeight*5/8);
        listPositionBall.add(pVector);

        /**
         * Position of player 2 ball
         */
        //Ball size /6 : 01 ball
        pVector = new PVector(screenWidth/2, screenHeight*1/8);
        listPositionBall.add(pVector);
        //Ball size /10 : 02 ball
        pVector = new PVector(screenWidth/5, screenHeight*1/8);
        listPositionBall.add(pVector);


        pVector = new PVector(screenWidth*4/5, screenHeight*1/8);
        listPositionBall.add(pVector);

        //Ball size /12 : 02 ball
        pVector = new PVector(screenWidth/3, screenHeight*3/8);
        listPositionBall.add(pVector);


        pVector = new PVector(screenWidth*2/3, screenHeight*3/8);
        listPositionBall.add(pVector);
    }

    /**
     * Prepare bitmap for ball
     */
    private void prepareBitmap() {
        listBallBitmap.clear();
        int ballP1Id = DataUtil.config.getBallPlayer1Id();
        int ballP2Id = DataUtil.config.getBallPlayer2Id();

        /**
         * Player 1 ball bitmap
         */
        //Ball size /6 : 01 ball
        Bitmap bpBall = createBall(ballP1Id, screenWidth / 5);
        listBallBitmap.add(bpBall);

        //Ball size /10 : 02 ball
        bpBall = createBall(ballP1Id,screenWidth/9);
        listBallBitmap.add(bpBall);
        listBallBitmap.add(bpBall);

        //Ball size /12 : 02 ball
        bpBall = createBall(ballP1Id,screenWidth/12);
        listBallBitmap.add(bpBall);
        listBallBitmap.add(bpBall);

        /**
         * Player 2 ball bitmap
         */
        //Ball size /6 : 01 ball
        bpBall = createBall(ballP2Id, screenWidth / 5);
        listBallBitmap.add(bpBall);

        //Ball size /10 : 02 ball
        bpBall = createBall(ballP2Id,screenWidth/9);
        listBallBitmap.add(bpBall);
        listBallBitmap.add(bpBall);

        //Ball size /12 : 02 ball
        bpBall = createBall(ballP2Id,screenWidth/12);
        listBallBitmap.add(bpBall);
        listBallBitmap.add(bpBall);
    }
}
