package com.example.cxyang.pomelotiming;

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
import android.widget.Toast;

public class AlarmActivity extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "You Have a Plan to be Started Soon !!!", Toast.LENGTH_SHORT).show();
        //Intent i = new Intent(context, LongRunningService.class);
        //context.startService(i);
    }
}


