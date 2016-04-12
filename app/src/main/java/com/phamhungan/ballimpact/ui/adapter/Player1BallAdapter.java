package com.phamhungan.ballimpact.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.model.Ball;
import com.phamhungan.ballimpact.util.DataUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAn PC on 20-Feb-16.
 */
public class Player1BallAdapter extends RecyclerView.Adapter<MyHolder> implements IInitAdapter{

    private List<Ball> listBall = new ArrayList<>();
    private final String TAG = "Player1BallAdapter";
    private Bitmap imgLock;
    private Bitmap imgCheck;
    public Player1BallAdapter(List<Ball> listBall,Bitmap imgLock,Bitmap imgCheck){
        this.listBall = listBall;
        this.imgLock = imgLock;
        this.imgCheck = imgCheck;
    }
    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ball, parent,false);

        MyHolder viewHolder = new MyHolder(view);
        return viewHolder;
    }

    private Ball getItem(int pos){
        return listBall.get(pos);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Ball ball = getItem(position);
        int isSelected = isSelected(position);
        //Check is Selected
        if(isSelected==DataUtil.SELECTED){
            holder.imgCheck.setImageBitmap(imgCheck);
        }else {
            //Check is Locked
            int isLocked = isLocked(position);
            if(isLocked==DataUtil.LOCKED){
                holder.imgCheck.setImageBitmap(imgLock);
            }else {
                holder.imgCheck.setImageBitmap(null);
            }
        }
        holder.imgBall.setImageBitmap(ball.getBallBitmap());
        holder.imgBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.updateSelectedListBall(ball.getBallId(),1,0);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBall.size();
    }

    @Override
    public int isSelected(int position) {
        Ball ball = listBall.get(position);
        if(ball.getIsP1Selected()==1)
            return DataUtil.SELECTED;
        return DataUtil.NOT_SELECTED;
    }

    @Override
    public int isLocked(int position) {
        Ball ball = listBall.get(position);
        if(ball.getLocked()==1)
            return DataUtil.LOCKED;
        return DataUtil.UNLOCKED;
    }
}
