package com.example.cxyang.pomelotiming;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.app.AlertDialog;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;


public class DisplayMessageActivity extends AppCompatActivity implements View.OnClickListener {

    //public static final String localHost = "http://localhost:80";
    public static final String serverHost = "http://45.32.5.192:80";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String SERVER_URL = serverHost;

    private static final String COUNT_KEY = "com.example.key.count";
    private String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        userid = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Capture the layout's TextView and set the string as its text
        //TextView textView = findViewById(R.id.textView);
        //textView.setText(message);

        Button button = (Button) findViewById(R.id.new_plan);
        button.setOnClickListener(this);

    }
    public void onClick(View v)
    {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.layoutdialog, null);
        AlertDialog dlg = new AlertDialog.Builder(this)
                .setTitle("New Plan")
                .setView(textEntryView)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public String getTime(String date, String time) {
                                String ret = "";
                                int i;
                                // Change Date to TimeStamp
                                for (i = 0; i < 4; i ++) ret = ret + date.charAt(i);
                                if (date.charAt(6) == '/' || date.charAt(6) == '.') {
                                    ret = ret + "0" + date.charAt(5);
                                    if (date.length() == 8)
                                        ret = ret + "0" + date.charAt(7);
                                    else ret = ret + date.charAt(7) + date.charAt(8);
                                } else {
                                    ret = ret + date.charAt(5) + date.charAt(6);
                                    if (date.length() == 9)
                                        ret = ret + "0" + date.charAt(8);
                                    else ret = ret + date.charAt(8) + date.charAt(9);
                                }
                                // Change Time to TimeStamp
                                if (time.charAt(1) == ':' || date.charAt(1) == '.') {
                                    ret = ret + "0" + time.charAt(0);
                                    if (time.length() == 3)
                                        ret = ret + "0" + time.charAt(2);
                                    else ret = ret + time.charAt(2) + time.charAt(3);
                                } else {
                                    ret = ret + time.charAt(0) + time.charAt(1);
                                    if (time.length() == 4)
                                        ret = ret + "0" + time.charAt(3);
                                    else ret = ret + time.charAt(3) + time.charAt(4);
                                }
                                return ret;
                            }

                            public void onClick(DialogInterface dialog, int whichButton) {

                                String st_planName = textEntryView.findViewById(R.id.text_planname).toString();

                                EditText start_date = textEntryView.findViewById(R.id.startdate);
                                EditText end_date = textEntryView.findViewById(R.id.enddate);
                                EditText start_time = textEntryView.findViewById(R.id.starttime);
                                EditText end_time = textEntryView.findViewById(R.id.endtime);

                                String st_startTime = getTime(start_date.toString(), start_time.toString());
                                String st_endTime = getTime(end_date.toString(), end_time.toString());


                                HashMap<String, String> params = new HashMap<String, String>();
                                params.put("start_time", st_startTime);
                                params.put("end_time", st_endTime);
                                params.put("plan_name", st_planName);

                                Log.e("post", params.toString());
                                String reqString = SERVER_URL + "/home/plan_setting/" + userid;

                                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, reqString, new JSONObject(params),
                                        new Response.Listener<JSONObject>(){
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                Log.e("normalResponse", response.toString());
                                                System.out.println(response.toString());
                                            }
                                        }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError arg0){
                                        Log.e("errorResponse", arg0.toString());
                                    }
                                });
                                MySingleton.getInstance(DisplayMessageActivity.this).addToRequestQueue(jsonObjectRequest);
                                //System.out.println(st_startTime);
                                //System.out.println(st_endTime);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        })
                .create();
        dlg.show();
    }
}
