package com.example.cxyang.pomelotiming;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.widget.TextView;

class DisplayStatusActivity extends WearableActivity {

    public TextView mTaskStateName;
    public TextView mTaskState;
    public static final Double RESTING_STATUS = 0.0;
    public static final String RESTING = "Resting";
    public static final String WORKING = "Working";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Double MsgStatus = Double.parseDouble(getIntent().getStringExtra("MSG_STATUS"));

        mTaskStateName = (TextView) findViewById(R.id.stateTitle);
        mTaskState = (TextView) findViewById(R.id.state);

        if (MsgStatus.equals(RESTING_STATUS)){
            //Rest
            mTaskState.setText(RESTING);
        }else{
            //TODO: progress!
            mTaskState.setText(WORKING);
        }
        setContentView(R.layout.fragment_frame);
    }
}
