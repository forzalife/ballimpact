package com.phamhungan.ballimpact.ui.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phamhungan.ballimpact.R;
import com.phamhungan.ballimpact.model.Table;
import com.phamhungan.ballimpact.util.DataUtil;

import java.util.List;

/**
 * Created by MrAn PC on 20-Feb-16.
 */
public class TableAdapter extends RecyclerView.Adapter<MyHolder> implements IInitAdapter{
    private List<Table> listTable;
    private Bitmap imgLock;
    private Bitmap imgCheck;
    private final String TAG = "TableAdapter";

    public TableAdapter(List<Table> listTable,Bitmap imgLock,Bitmap imgCheck){
        this.listTable = listTable;
        this.imgLock = imgLock;
        this.imgCheck = imgCheck;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ball, parent,false);

        MyHolder viewHolder = new MyHolder(view);
        return viewHolder;
    }

    private Table getItem(int pos){
        return listTable.get(pos);
    }


    @Override
    public int isSelected(int position) {
        Table table = listTable.get(position);
        if(table.getIsSelected()==1){
            return DataUtil.SELECTED;
        }
        return DataUtil.NOT_SELECTED;
    }

    @Override
    public int isLocked(int position) {
        Table table = listTable.get(position);
        if(table.getLocked()==1)
            return DataUtil.LOCKED;
        return DataUtil.UNLOCKED;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        final Table table = getItem(position);
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
        holder.imgBall.setImageBitmap(table.getTableBitmap());
        holder.imgBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataUtil.updateSelectedListTable(table.getTableId());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return listTable.size();
    }
}
