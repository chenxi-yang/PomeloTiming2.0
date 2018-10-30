package com.example.cxyang.pomelotiming;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.nfc.cardemulation.CardEmulation;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.CardFrame;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

public class MainActivity extends WearableActivity{
    private static final String COUNT_KEY = "com.example.key.count";
    private int count = 0;

    public TextView mTextView;
    public TextView mTaskStateName;
    public TextView mTaskState;
    /*
    @Override
    protected void onResume() {
        super.onResume();
        Wearable.getDataClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Wearable.getDataClient(this).removeListener(this);
    }
    */


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_frame);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        CardFragment cardFragment = CardFragment.create("title",  "description", R.layout.fragment_frame);
//        fragmentTransaction.add(R.id.frame_layout, cardFragment);
//        fragmentTransaction.commit();

        mTaskStateName = (TextView) findViewById(R.id.stateTitle);
        mTaskState = (TextView) findViewById(R.id.state);

        // Enables Always-on
        setAmbientEnabled();
    }
}
