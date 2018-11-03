package com.example.cxyang.pomelotiming;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CardFrame;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.Wearable;

import java.net.URI;

public class MainActivity extends WearableActivity implements DataApi.DataListener {
    private static final String COUNT_KEY = "com.example.key.count";
    public static final String MSG_PHONE2WATCH_PATH = "/PHONE2WATCH";
    public static final String MSG_WATCH2PHONE_PATH = "/WATCH2PHONE";
    public static final Double RESTING_STATUS = 0.0;
    public static final String RESTING = "Resting";
    public static final String WORKING = "Working";


    private int count = 0;

    public TextView mTextView;
    public TextView mTaskStateName;
    public TextView mTaskState;

    private GoogleApiClient mGoogleApiClient;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == 1){
                Toast.makeText(getApplicationContext(), "Working Now", Toast.LENGTH_SHORT).show();
                if (msg.obj == RESTING_STATUS){
                    mTaskState.setText(RESTING);
                }else{
                    mTaskState.setText(WORKING);
                }
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_frame);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create("title",  "description", R.layout.fragment_frame);

        mTaskStateName = (TextView) findViewById(R.id.stateTitle);
        mTaskState = (TextView) findViewById(R.id.state);

        // Enables Always-on
        setAmbientEnabled();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        mGoogleApiClient.connect();
        Wearable.DataApi.addListener(mGoogleApiClient, this);
//                .addApi(Wearable.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .build();
    }

//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    public void onSendMsg(Double msg){
//        PutDataMapRequest putRequest = PutDataMapRequest.create(MSG_PHONE2WATCH_PATH);
//        DataMap map = putRequest.getDataMap();
//        map.putDouble("msgStatus", msg);
//        Wearable.DataApi.putDataItem(mGoogleApiClient, putRequest.asPutDataRequest());
//    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {
        for(DataEvent event : dataEventBuffer){
            Uri uri = event.getDataItem().getUri();
            String path = uri != null ? uri.getPath() : null;

            if (MSG_PHONE2WATCH_PATH.equals(path)){
                DataMap map = DataMapItem.fromDataItem(event.getDataItem()).getDataMap();
                Double status = map.getDouble("msgStatus");
                handler.obtainMessage(1, status).sendToTarget();

            }

        }

    }
}
