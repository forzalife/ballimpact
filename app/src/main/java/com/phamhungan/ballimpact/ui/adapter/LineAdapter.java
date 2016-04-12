package com.phamhungan.ballimpact.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.model.Line;
import com.phamhungan.ballimpact.model.Table;
import com.phamhungan.ballimpact.util.DataUtil;

import java.util.List;

/**
 * Created by MrAn PC on 20-Feb-16.
 */
public class LineAdapter extends RecyclerView.Adapter<MyHolder>{
    private List<Line> listLine;
    private Bitmap imgCheck;
    private final String TAG = "LineAdapter";

    public LineAdapter(List<Line> listLine, Bitmap imgCheck){
        this.listLine = listLine;
        this.imgCheck = imgCheck;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ball, parent,false);

        MyHolder viewHolder = new MyHolder(view);
        return viewHolder;
    }

    private Line getItem(int pos){
        return listLine.get(pos);
    }

    public int isSelected(int position) {
        Line line = listLine.get(position);
        if(line.getIsSelected()==1){
            return DataUtil.SELECTED;
        }
        return DataUtil.NOT_SELECTED;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Line line = getItem(position);
        int isSelected = isSelected(position);
        //Check is Selected
        if(isSelected==DataUtil.SELECTED){
            holder.imgCheck.setImageBitmap(imgCheck);
        }else {
            holder.imgCheck.setImageBitmap(null);
        }
        holder.imgBall.setImageBitmap(line.getLineBitmap());
        holder.imgBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.updateSelectedListLine(line.getLineId());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listLine.size();
    }
}
