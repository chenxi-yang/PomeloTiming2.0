package com.example.cxyang.pomelotiming.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cxyang.pomelotiming.db.DataBaseServer;
import com.example.cxyang.pomelotiming.Plan.Plan;
import com.example.cxyang.pomelotiming.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ldf on 17/6/14.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> implements MyItemTouchHelper.OnItemTouchListener {
    private Context context;
    private List<Plan> list;
    private DataBaseServer db;
    private ImageView iv;

    @Override
    public void onSwiped(int position) {
        removeData(position);
    }
    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            //从上往下拖动，每滑动一个item，都将list中的item向下交换，向上滑同理。
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        //注意此处只是notifyItemMoved并没有notifyDataSetChanged
        //原因下面会说明
    }

    public RecycleAdapter(Context context, List<Plan> list, DataBaseServer db) {
        this.context = context;
        this.list = list;
        this.db = db;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                context).inflate(R.layout.item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        String txt = list.get(position).get_name();
        holder.tv.setText(txt);
        String time_txt = list.get(position).get_start_time() + " —— " + list.get(position).get_end_time();

        holder.tv_duration.setText(time_txt);

        holder.tv_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageView iiv = v.findViewById(R.id.img_complete);
                iiv.setImageDrawable(context.getResources().getDrawable(R.drawable.checkcircle));
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    //  更新数据
    public void ChangeData(List<Plan> newlist) {
//      在list中添加数据，并通知条目加入一条
        list.clear();
        for (int i = 0; i < newlist.size(); i ++)
            list.add(i, newlist.get(i));

        notifyDataSetChanged();
    }
    //  删除数据
    public void removeData(int position) {
        db.DeletePlan(list.get(position));

        list.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    /**
     * ViewHolder的类，用于缓存控件
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv, tv_duration;
        ImageView tv_img;
        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);
            tv_duration = (TextView) view.findViewById(R.id.id_duration);
            tv_img = (ImageView) view.findViewById(R.id.img_complete);
            iv = (ImageView) view.findViewById(R.id.img_complete);
        }
    }
}
