package com.example.cxyang.pomelotiming.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cxyang.pomelotiming.db.DataBaseServer;
import com.example.cxyang.pomelotiming.Plan.Plan;
import com.example.cxyang.pomelotiming.R;

import java.util.List;

/**
 * Created by ldf on 17/6/14.
 */

class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {
    private Context context;
    private List<Plan> list;
    private DataBaseServer db;

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
        holder.tv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { removeData(position); }
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
        TextView tv, tv_delete, tv_duration;
        //因为删除有可能会删除中间条目，然后会造成角标越界，所以必须整体刷新一下！
        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.id_num);
            tv_duration = (TextView) view.findViewById(R.id.id_duration);
            tv_delete = (TextView) view.findViewById(R.id.tv_delete);
        }
    }
}
