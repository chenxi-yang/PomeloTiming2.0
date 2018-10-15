package com.example.cxyang.pomelotiming.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cxyang.pomelotiming.MainActivity;
import com.example.cxyang.pomelotiming.MySingleton;
import com.example.cxyang.pomelotiming.R;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by IT-CTY on 2018/4/25.
 */

public class Agenda extends Fragment {

    public static final String serverHost = "http://45.32.5.192:80";
    public static final String SERVER_URL = serverHost;

    private String userid;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.agenda_fragment,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        userid = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        Button button = getActivity().findViewById(R.id.new_plan_fragment);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater factory = LayoutInflater.from(getActivity());
                final View textEntryView = factory.inflate(R.layout.layoutdialog, null);
                AlertDialog dlg = new AlertDialog.Builder(getActivity())
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
                                        MySingleton.getInstance(getActivity()).addToRequestQueue(jsonObjectRequest);
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
        });


    }
}