package com.example.cxyang.pomelotiming.main;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.cxyang.pomelotiming.R;
import com.example.cxyang.pomelotiming.db.DataBaseServer;
import com.example.cxyang.pomelotiming.timedata.OneTimeDetails;
import com.example.cxyang.pomelotiming.timedata.PackageInfo;
import com.example.cxyang.pomelotiming.timedata.UserTimeDataManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static java.lang.Integer.min;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener {
    public static final String serverHost = "http://45.32.5.192:80";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String SERVER_URL = serverHost;
    public static final String MSG_PHONE2WATCH_PATH = "/PHONE2WATCH";
    public static final String MSG_WATCH2PHONE_PATH = "/WATCH2PHONE";

    public static int USERID;

    private static final String COUNT_KEY = "com.example.key.count";

    private SQLiteDatabase db;

    private GoogleApiClient mGoogleApiClient;

    private static UserTimeDataManager mUseTimeDataManager;
    private ArrayList<PackageInfo> PackageInfoList;
    private ArrayList<OneTimeDetails> mOneTimeDetailInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/my_db.db", null);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        DataRecord();
    }

    private DataBaseServer localdb;

    public void DataRecord() {
        mUseTimeDataManager = UserTimeDataManager.getInstance(this);
        mUseTimeDataManager.refreshData(0);

        localdb = new DataBaseServer(this, "plan.db");
        PackageInfoList = mUseTimeDataManager.getmPackageInfoListOrderByTime();
        /*
            应用程序的list
        */
        System.out.print("Size: "); System.out.println(PackageInfoList.size());
        if (PackageInfoList.size() == 0) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
        for (int i = 0; i < PackageInfoList.size(); i ++) {
            String pkg = PackageInfoList.get(i).getmPackageName();
            //System.out.print("Package Name: ");
            //System.out.println(pkg);

            //System.out.print("Used Time: ");
            //System.out.println(PackageInfoList.get(i).getmUsedTime());

            //System.out.print("Count: ");
            //System.out.println(PackageInfoList.get(i).getmUsedCount());


            /*
                每一个应用的具体信息
             */
            mOneTimeDetailInfoList = mUseTimeDataManager.getPkgOneTimeDetailList(pkg);
            for (int j = 0; j < mOneTimeDetailInfoList.size(); j ++) {
                String startTime = mOneTimeDetailInfoList.get(j).getStartTime();
                String endTime = mOneTimeDetailInfoList.get(j).getStopTime();
                long totalTime = mOneTimeDetailInfoList.get(j).getUseTime();

                //System.out.print("---Start Time: "); System.out.println(startTime);
                //System.out.print("---End Time: "); System.out.println(endTime);
                //System.out.print("---Total Time: "); System.out.println(totalTime);

                localdb.addRecord(pkg, startTime, endTime, totalTime);
            }
        }
    }

    /**
     * Called when the user taps the Send button
     */
    public void onLogin(View view) {
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        EditText editText = (EditText) findViewById(R.id.editText);
        String username = editText.getText().toString();

        EditText editText2 = (EditText) findViewById(R.id.editText2);
        String password = editText2.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        Log.e("post", params.toString());

        String loginString = SERVER_URL + "/login";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, loginString, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("normalResponse", response.toString());

                        try {
                            USERID = response.getInt("user_id");
                            if (USERID == -1) {
                                // TODO: handle wrongpass word
                            } else {
                                // TODO: enter this user's homepage
                            }
                        } catch (JSONException e) {
                            //some exception handler code.
                        }
                        // System.out.print(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError arg0) {
                Log.e("errorResponse", arg0.toString());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
        //queue.add(jsonObjectRequest);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, username);
        startActivity(intent);

        //TEST
//        Intent intent = new Intent(this, PlanSettingActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, username);
//        startActivity(intent);
    }

    @Override
    public void onConnected(@Nullable Bundle connectionHint) {
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        // Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onDataChanged(DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

    }
}
