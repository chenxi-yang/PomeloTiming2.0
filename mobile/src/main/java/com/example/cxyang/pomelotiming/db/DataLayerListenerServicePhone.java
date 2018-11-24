package com.example.cxyang.pomelotiming.db;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.cxyang.pomelotiming.main.MainActivity;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;

public class DataLayerListenerServicePhone extends WearableListenerService {

    @Override
    public void onDataChanged(DataEventBuffer dataEvents){
        super.onDataChanged(dataEvents);

        Log.v("wrox-mobile", "Data arrived");

        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        for(DataEvent event:events){
            final Uri uri = event.getDataItem().getUri();
            final String path = uri!=null?uri.getPath():null;
            if(MainActivity.MSG_WATCH2PHONE_PATH.equals(path)){
                final DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                //TODO: data from watch
                Integer X = map.getInt("watchData");
                Intent localIntent = new Intent("phone.localIntent");
                //localIntent.putExtra("result", reply);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
            }
        }
    }
}
