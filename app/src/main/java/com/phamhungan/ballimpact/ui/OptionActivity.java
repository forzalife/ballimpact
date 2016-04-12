package com.phamhungan.ballimpact.ui;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.database.Database;
import com.phamhungan.ballimpact.model.Ball;
import com.phamhungan.ballimpact.model.Line;
import com.phamhungan.ballimpact.model.Table;
import com.phamhungan.ballimpact.ui.adapter.LineAdapter;
import com.phamhungan.ballimpact.ui.adapter.Player1BallAdapter;
import com.phamhungan.ballimpact.ui.adapter.Player2BallAdapter;
import com.phamhungan.ballimpact.ui.adapter.TableAdapter;
import com.phamhungan.ballimpact.util.DataUtil;
import com.phamhungan.ballimpact.util.EventUtil;
import com.phamhungan.ballimpact.util.ResizeBitmap;
import com.phamhungan.ballimpact.util.ScreenUtil;
import com.phamhungan.ballimpact.util.custom.CustomLinearLayoutManager;

/**
 * Created by MrAn PC on 18-Feb-16.
 */
public class OptionActivity extends AppCompatActivity {
    private final String TAG = "OptionActivity";
    private SeekBar seekBrightness;
    private TextView txtBriPercent;
    private WindowManager.LayoutParams windowsAttributes;
    private Double screenBrightness;
    private RecyclerView p1BallList;
    private RecyclerView p2BallList;
    private RecyclerView tableList;
    private RecyclerView lineList;
    private EditText txtP1Name;
    private EditText txtP2Name;
    private float screenWidth;
    private Player1BallAdapter player1BallAdapter;
    private Player2BallAdapter player2BallAdapter;
    private TableAdapter tableAdapter;
    private LineAdapter lineAdapter;
    private LinearLayoutManager layoutManager;
    private ImageView imgSave;
    private ImageView imgDefault;
    public static OptionActivity optionActivity;
    private Bitmap bpCheck;
    private Bitmap bpLock;
    private ProgressDialog progressDialog;
    private Database database;
    private boolean isSave = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        createInstance();
        initLayout();
        setOnClick();
    }

    private void setOnClick() {
        imgDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDefaultData();
            }
        });
        imgSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void saveData() {
        boolean isNameCorect = true;
        String p1Name = txtP1Name.getText().toString().trim();
        String p2Name = txtP2Name.getText().toString().trim();
        if(p1Name.equals("")||p1Name.isEmpty()||p1Name==null){
            EventUtil.makeToast(optionActivity, getResources().getString(R.string.p1_name_error));
            isNameCorect = false;
        }
        if(p2Name.equals("")||p2Name.isEmpty()||p2Name==null){
            EventUtil.makeToast(optionActivity, getResources().getString(R.string.p2_name_error));
            isNameCorect = false;
        }
        if(isNameCorect){
            new SaveDataAsync(p1Name,p2Name).execute(new Void[]{});
        }
    }

    private void createInstance() {
        if(optionActivity==null)
            optionActivity = this;
    }

    private void setDefaultData(){
        DataUtil.updateSelectedListTable(R.color.green_700);
        DataUtil.updateSelectedListBall(R.drawable.ball_red, 1, 0);
        DataUtil.updateSelectedListBall(R.drawable.ball_blue,0,1);
        DataUtil.updateSelectedListLine(R.color.grey_400);
        tableAdapter.notifyDataSetChanged();
        player1BallAdapter.notifyDataSetChanged();
        player2BallAdapter.notifyDataSetChanged();
        lineAdapter.notifyDataSetChanged();
        txtP1Name.setText(getResources().getString(R.string.player1));
        txtP2Name.setText(getResources().getString(R.string.player2));
        new SaveDataAsync(txtP1Name.getText().toString(),txtP2Name.getText().toString()).execute(new Void[]{});
    }

    private void loadLastData(){
        DataUtil.updateSelectedListTable(DataUtil.config.getTableId());
        DataUtil.updateSelectedListBall(DataUtil.config.getBallPlayer1Id(), 1, 0);
        DataUtil.updateSelectedListBall(DataUtil.config.getBallPlayer2Id(), 0, 1);
        DataUtil.updateSelectedListLine(DataUtil.config.getLineId());
        tableAdapter.notifyDataSetChanged();
        lineAdapter.notifyDataSetChanged();
        player1BallAdapter.notifyDataSetChanged();
        player2BallAdapter.notifyDataSetChanged();
        txtP1Name.setText(DataUtil.config.getPlayer1Name());
        txtP2Name.setText(DataUtil.config.getPlayer2Name());
    }

    /**
     * Init layout
     */
    private void initLayout() {
        database = new Database(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        screenWidth = ScreenUtil.getScreenWidth(getWindowManager());
        windowsAttributes = getWindow().getAttributes();
        bpCheck = ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.check), screenWidth / 16);
        bpLock = ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.lock), screenWidth / 16);
        seekBrightness = (SeekBar)findViewById(R.id.seekBrightness);
        txtBriPercent = (TextView)findViewById(R.id.txtBriPercent);
        p1BallList = (RecyclerView)findViewById(R.id.p1BallList);
        p2BallList = (RecyclerView)findViewById(R.id.p2BallList);
        tableList = (RecyclerView)findViewById(R.id.tableList);
        lineList = (RecyclerView)findViewById(R.id.lineList);
        imgDefault = (ImageView)findViewById(R.id.imgDefault);
        imgSave = (ImageView)findViewById(R.id.imgSave);
        txtP1Name = (EditText)findViewById(R.id.txtP1Name);
        txtP2Name = (EditText)findViewById(R.id.txtP2Name);

        createProgressDialog();
        setBackground();
        setSeekBrightness(getScreenBrightness());
        prepareListBall();
        prepareListTable();
        prepareListLine();

        setP1BallList();
        setP2BallList();
        setTableList();
        setLineList();
        setNamePlayer();
    }

    private void createProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getResources().getString(R.string.save_data_message));
    }

    private void setNamePlayer() {
        txtP1Name.setText(DataUtil.config.getPlayer1Name());
        txtP2Name.setText(DataUtil.config.getPlayer2Name());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!isSave){
            loadLastData();
        }
    }

    private void setBackground() {
        imgDefault.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.background_label), screenWidth/4));
        imgSave.setImageBitmap(ResizeBitmap.resize(BitmapFactory.decodeResource(getResources(), R.drawable.background_label), screenWidth / 4));
    }

    /**
     * Set list adapter for Tables
     */
    private void setTableList() {
        tableList.setLayoutManager(layoutManager);
        tableList.setAdapter(tableAdapter);
    }

    /**
     * Set list adapter for Line
     */
    private void setLineList() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        lineList.setLayoutManager(layoutManager);
        lineList.setAdapter(lineAdapter);
    }

    /**
     * Prepare list tables
     */
    private void prepareListTable() {
        tableAdapter = new TableAdapter(DataUtil.listTable,bpLock,bpCheck);
    }

    /**
     * Prepare list tables
     */
    private void prepareListLine() {
        lineAdapter = new LineAdapter(DataUtil.listLine,bpCheck);
    }

    /**
     * Set list adapter for player 2
     */
    private void setP2BallList() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        p2BallList.setLayoutManager(layoutManager);
        p2BallList.setAdapter(player2BallAdapter);
    }

    /**
     * Set list adapter for player 1
     */
    private void setP1BallList() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        p1BallList.setLayoutManager(layoutManager);
        p1BallList.setAdapter(player1BallAdapter);
    }

    /**
     * Prepare list ball
     */
    private void prepareListBall() {
        player1BallAdapter = new Player1BallAdapter(DataUtil.listBall,bpLock,bpCheck);
        player2BallAdapter = new Player2BallAdapter(DataUtil.listBall,bpLock,bpCheck);
    }

    /**
     * Set Seek brightness
     * @param screenBrightness
     */
    private void setSeekBrightness(int screenBrightness) {
        seekBrightness.setProgress(screenBrightness);
        txtBriPercent.setText(screenBrightness + "%");
        seekBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                new Thread(new RunBrightness(progress)).start();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    /**
     * Get screen brightness
     * @return
     */
    private int getScreenBrightness(){
        try{
            screenBrightness = Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS)/2.55;
            return screenBrightness.intValue()+1;
        }catch (Exception e){
            return 0;
        }
    }

    /**
     * Set seek brightness
     */
    private class RunBrightness implements Runnable{
        private int progress;
        public RunBrightness(int progress){
            this.progress = progress;
        }
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setBrighetness(progress);
                }
            });
        }

        private synchronized void setBrighetness(int progress){
            try{
                windowsAttributes.screenBrightness = (float)progress/100;
                getWindow().setAttributes(windowsAttributes);

                screenBrightness = progress*2.55;
                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS,screenBrightness.intValue());
                txtBriPercent.setText(progress+"%");
            }catch (Exception e){}
        }
    }

    private class SaveDataAsync extends AsyncTask<Void,Void,Void>{
        private String p1Name;
        private String p2Name;

        public SaveDataAsync(String p1Name,String p2Name){
            this.p1Name = p1Name.trim();
            this.p2Name = p2Name.trim();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            database.open();
            for (Ball ball : DataUtil.listBall){
                database.updateBall(ball.getBallId(),ball.getIsP1Selected(),"p1Selected");
                database.updateBall(ball.getBallId(),ball.getIsP2Selected(),"p2Selected");
                if(ball.getIsP1Selected()==DataUtil.SELECTED)
                    DataUtil.config.setBallPlayer1Id(ball.getBallId());
                if(ball.getIsP2Selected()==DataUtil.SELECTED)
                    DataUtil.config.setBallPlayer2Id(ball.getBallId());
            }
            for (Table table : DataUtil.listTable){
                database.updateSelectedTable(table.getTableId(), table.getIsSelected());
                if(table.getIsSelected()==DataUtil.SELECTED)
                    DataUtil.config.setTableId(table.getTableId());
            }
            for (Line line : DataUtil.listLine){
                database.updateSelectedLine(line.getLineId(), line.getIsSelected());
                if(line.getIsSelected()==DataUtil.SELECTED)
                    DataUtil.config.setLineId(line.getLineId());
            }
            DataUtil.config.setPlayer1Name(p1Name);
            DataUtil.config.setPlayer2Name(p2Name);
            database.updateConfig();
            database.close();
            isSave=true;
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            onBackPressed();
            EventUtil.makeToast(optionActivity,optionActivity.getResources().getString(R.string.save_success));
        }
    }
}
