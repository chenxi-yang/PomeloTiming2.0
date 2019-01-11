package com.example.cxyang.pomelotiming.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.cxyang.pomelotiming.R;
import com.example.cxyang.pomelotiming.timedata.OneTimeDetails;
import com.example.cxyang.pomelotiming.timedata.UserTimeDataManager;
import com.example.cxyang.pomelotiming.timedata.UserTimeDetailAdapter;

public class UserTimeDetailActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;

    private UserTimeDetailAdapter mUseTimeDetailAdapter;
    private UserTimeDataManager mUseTimeDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_time_details);

        mUseTimeDataManager = UserTimeDataManager.getInstance(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_use_time_detail);

        Intent intent = getIntent();
        showAppOpenTimes(intent.getStringExtra("pkg"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
    }

    private void showAppOpenTimes(String pkg){
        mUseTimeDetailAdapter = new UserTimeDetailAdapter(mUseTimeDataManager.getPkgOneTimeDetailList(pkg));
        mUseTimeDetailAdapter.setOnItemClickListener(new UserTimeDetailAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, OneTimeDetails details) {
                mUseTimeDataManager.setmOneTimeDetails(details);
            }
        });
        mRecyclerView.setAdapter(mUseTimeDetailAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
}

