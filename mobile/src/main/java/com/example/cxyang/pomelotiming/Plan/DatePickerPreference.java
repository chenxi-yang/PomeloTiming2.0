package com.example.cxyang.pomelotiming.Plan;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.example.cxyang.pomelotiming.R;

import java.util.Calendar;

public class DatePickerPreference extends DialogPreference {
    public DatePickerPreference(Context context) {
        super(context);
    }

    public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DatePickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DatePickerPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private String date;
    @Override
    protected View onCreateDialogView() {
        setDialogLayoutResource(R.layout.layout_datepicker);
        setDialogTitle("Dialog Title");

        return super.onCreateDialogView();
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
//      Dialog dialog = getDialog();
        datePicker = (view.findViewById(R.id.date_picker));

        datePicker.setCalendarViewShown(false);
        //初始化当前日期
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), null);
    }

    private DatePicker datePicker;
    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        switch (which) {
            case Dialog.BUTTON_POSITIVE:
                //OK
                date = String.format("%d/%02d/%02d", datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth());

                SharedPreferences shared = getContext().getSharedPreferences("EditFragment", Context.MODE_PRIVATE);
                shared.edit().putString(DatePickerPreference.this.getKey(), date).commit();

                this.setSummary(date);
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
    public String getDate() { return date; }
}
