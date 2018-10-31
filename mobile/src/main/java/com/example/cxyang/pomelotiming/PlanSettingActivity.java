package com.example.cxyang.pomelotiming;
/*
* First used for testing data transformation
* Function:
* 1. Plan set successfully
* 2. having one button to return to users' home
* */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

class PlanSettingActivity extends AppCompatActivity {

    public static final String MSG_PHONE2WATCH_PATH = "/PHONE2WATCH";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_planset);

    }
    public void onSendMsg(View view){
        PutDataMapRequest putRequest = PutDataMapRequest.create(MSG_PHONE2WATCH_PATH);
        DataMap map = putRequest.getDataMap();
        map.putDouble("msg", 1.0);
        Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
    }
}
