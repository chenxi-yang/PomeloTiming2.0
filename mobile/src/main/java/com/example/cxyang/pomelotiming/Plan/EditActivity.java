package com.example.cxyang.pomelotiming.Plan;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.cxyang.pomelotiming.R;

import java.util.Calendar;

public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        EditFragment EditFragment = new EditFragment();
        transaction.add(R.id.EditFragment, EditFragment);
        transaction.commit();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用

        FloatingActionButton save = findViewById(R.id.save);
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent();

                    SharedPreferences sharedPreferences = getSharedPreferences("EditFragment", Context.MODE_PRIVATE);
                    String start = sharedPreferences.getString("sttime_preference", "");
                    String end = sharedPreferences.getString("edtime_preference", "");
                    String date = sharedPreferences.getString("date_preference", "");
                    String name = sharedPreferences.getString("name_preference", "");

                    intent.putExtra("plan_name", name);
                    intent.putExtra("start_date", date);
                    intent.putExtra("start_time", start);
                    intent.putExtra("end_time", end);
                    //intent.putExtra("start_time", start_time.getText().toString().substring(0, 5));
                    //intent.putExtra("end_time", end_time.getText().toString().substring(0, 5));

                    setResult(RESULT_OK, intent);

                    finish();
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            {
                finish();
                return true;
            }
        return super.onOptionsItemSelected(item);
    }
}