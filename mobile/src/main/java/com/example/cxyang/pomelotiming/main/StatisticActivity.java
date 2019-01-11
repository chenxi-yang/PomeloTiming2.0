package com.example.cxyang.pomelotiming.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cxyang.pomelotiming.R;
import com.example.cxyang.pomelotiming.timedata.UserTimeAdapter;
import com.example.cxyang.pomelotiming.timedata.UserTimeDataManager;

public class StatisticActivity extends AppCompatActivity {

    private UserTimeDataManager mUseTimeDataManager;
    private RecyclerView mRecyclerView;
    private UserTimeAdapter mUserTimeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        initData();
        initView();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

    }

    private void initData(){
        mUseTimeDataManager = UserTimeDataManager.getInstance(this);
        mUseTimeDataManager.refreshData(0);
    }

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_show_statistics);

        mUserTimeAdapter = new UserTimeAdapter(this,mUseTimeDataManager.getmPackageInfoListOrderByTime());
        mUserTimeAdapter.setOnItemClickListener(new UserTimeAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, String pkg) {
                showDetail(pkg);
            }
        });
        mRecyclerView.setAdapter(mUserTimeAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void showDetail(String pkg){
        Intent it = new Intent(StatisticActivity.this, UserTimeDetailActivity.class);
        it.putExtra("type","times");
        it.putExtra("pkg",pkg);
        startActivity(it);
    }
}
