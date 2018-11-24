package com.example.cxyang.pomelotiming.Plan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import com.example.cxyang.pomelotiming.R;

import java.util.Calendar;

public class TimePickerPreference extends DialogPreference {
    public TimePickerPreference(Context context) {
        super(context);
    }

    public TimePickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public TimePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TimePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String time;
    @Override
    protected View onCreateDialogView() {
        setDialogLayoutResource(R.layout.layout_timepicker);
        setDialogTitle("Dialog Title");

        return super.onCreateDialogView();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
//      Dialog dialog = getDialog();
        timePicker = (view.findViewById(R.id.time_picker));

        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());

        timePicker.setIs24HourView(true);
        timePicker.setDrawingCacheEnabled(true);

        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(Calendar.MINUTE);
    }

    private TimePicker timePicker;
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //OK
                int mHour = timePicker.getCurrentHour();
                int mMinute = timePicker.getCurrentMinute();
                time = (mHour < 10 ? "0" + mHour : mHour) + ":" + (mMinute < 10 ? "0" + mMinute : mMinute);

                SharedPreferences shared = getContext().getSharedPreferences("EditFragment", Context.MODE_PRIVATE);
                shared.edit().putString(TimePickerPreference.this.getKey(), time).commit();

                this.setSummary(time);
                break;
            case Dialog.BUTTON_NEGATIVE:
                //do something
                break;
            case Dialog.BUTTON_NEUTRAL:
                //dosomething
                break;
        }
        super.onClick(dialogInterface, which);
    }

    @Override
    protected View onCreateView(ViewGroup parent) {
        return super.onCreateView(parent);
    }
    public String getTime() { return time; }
}
