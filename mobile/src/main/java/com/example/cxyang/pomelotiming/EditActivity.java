package com.example.cxyang.pomelotiming;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        final Button sttimeBtn = findViewById(R.id.sttimeBtn);
        sttimeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                //初始化时间
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("设置时间信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mHour = timePicker.getCurrentHour();
                        int mMinute = timePicker.getCurrentMinute();
                        //时间小于10的数字 前面补0 如01:12:00
                        sttimeBtn.setText(new StringBuilder().append(mHour < 10 ? "0" + mHour : mHour).append(":")
                                .append(mMinute < 10 ? "0" + mMinute : mMinute).append(":00") );
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        final Button edtimeBtn = findViewById(R.id.edtimeBtn);
        edtimeBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                //初始化时间
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("设置时间信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int mHour = timePicker.getCurrentHour();
                        int mMinute = timePicker.getCurrentMinute();
                        //时间小于10的数字 前面补0 如01:12:00
                        edtimeBtn.setText(new StringBuilder().append(mHour < 10 ? "0" + mHour : mHour).append(":")
                                .append(mMinute < 10 ? "0" + mMinute : mMinute).append(":00") );
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        final Button dateBtn = findViewById(R.id.dateBtn);
        dateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                //设置日期简略显示 否则详细显示 包括:星期\周
                datePicker.setCalendarViewShown(false);
                //初始化当前日期
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), null);
                //设置date布局
                builder.setView(view);
                builder.setTitle("设置日期信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //日期格式
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d/%02d/%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        dateBtn.setText(sb);
                        //赋值后面闹钟使用
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                EditText planname = findViewById(R.id.text_planname);
                Button start_date = findViewById(R.id.dateBtn);
                Button start_time = findViewById(R.id.sttimeBtn);
                Button end_time = findViewById(R.id.edtimeBtn);

                intent.putExtra("plan_name", planname.getText().toString());
                intent.putExtra("start_date", start_date.getText().toString());
                intent.putExtra("start_time", start_time.getText().toString().substring(0, 5));
                intent.putExtra("end_time", end_time.getText().toString().substring(0, 5));

                setResult(RESULT_OK, intent);

                finish();
            }
        });

        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                setResult(0, intent);
                finish();
            }
        });
    }
}
