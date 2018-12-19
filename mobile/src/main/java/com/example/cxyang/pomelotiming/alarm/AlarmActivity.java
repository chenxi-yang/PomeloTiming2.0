package com.example.cxyang.pomelotiming.alarm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.cxyang.pomelotiming.db.DataBaseServer;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

public class AlarmActivity extends BroadcastReceiver {
    public static final String MSG_PHONE2WATCH_PATH = "/PHONE2WATCH";
    private GoogleApiClient mGoogleApiClient;
    public Double workingStatus = 1.0;
    DataBaseServer db;

    @Override
    public void onReceive(Context context, Intent intent) {

        //onSendMsg(workingStatus);
        db = new DataBaseServer(context, "plan.db");
        String start_date = intent.getStringExtra("start_date");
        String start_time = intent.getStringExtra("start_time");

        int flag = db.findPlan(start_date, start_time);
        if (flag > 0) {
            Toast.makeText(context, "You Have a Plan to be Started Soon !!!", Toast.LENGTH_SHORT).show();
            System.out.println("Alarm!");
        }
    }

    public void onSendMsg(Double msg){
        PutDataMapRequest putRequest = PutDataMapRequest.create(MSG_PHONE2WATCH_PATH);
        DataMap map = putRequest.getDataMap();
        map.putDouble("msgStatus", msg);
        Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
    }
}


