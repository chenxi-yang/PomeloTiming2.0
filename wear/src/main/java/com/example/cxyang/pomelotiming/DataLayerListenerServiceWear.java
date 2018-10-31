package com.example.cxyang.pomelotiming;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class DataLayerListenerServiceWear extends WearableListenerService{
    //    public static final String TAG = "DataLayerSample";
//    private static final String START_ACTIVITY_PATH = "/start-activity";
//    private static final String DATA_ITEM_RECEIVED_PATH = "/data-item-received";
    public static final String MSG_PHONE2WATCH_PATH = "/PHONE2WATCH";
    public static final String MSG_WATCH2PHONE_PATH = "/WATCH2PHONE";

    public static final String EXTRA_MESSAGE = "MSG_STATUS";

    public TextView mTaskStateName;
    public TextView mTaskState;

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        super.onDataChanged(dataEvents);

        Log.v("wrox-mobile", "Data arrived");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for(DataEvent event:events){
            final Uri uri = event.getDataItem().getUri();
            final String path = uri!=null?uri.getPath():null;
            if(MSG_PHONE2WATCH_PATH.equals(path)){
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                Double msgStatus = map.getDouble("msgStatus");

                Intent intent = new Intent(this, DisplayStatusActivity.class);
                intent.putExtra(EXTRA_MESSAGE, String.valueOf(msgStatus));
                startActivity(intent);
            }
        }
    }
}
