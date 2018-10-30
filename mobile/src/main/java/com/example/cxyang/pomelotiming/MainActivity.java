package com.example.cxyang.pomelotiming;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {
    public static final String serverHost = "http://45.32.5.192:80";
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String SERVER_URL = serverHost;

    public static int USERID;

    private static final String COUNT_KEY = "com.example.key.count";
    private DataClient mDataClient;
    private int count = 0;

    private SQLiteDatabase db;


    // Create a data map and put data in it
    private void increaseCounter() {
        PutDataMapRequest putDataMapReq = PutDataMapRequest.create("/data-item-received");
        putDataMapReq.getDataMap().putInt(COUNT_KEY, count++);
        PutDataRequest putDataReq = putDataMapReq.asPutDataRequest();
        Task<DataItem> putDataTask = mDataClient.putDataItem(putDataReq);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = SQLiteDatabase.openOrCreateDatabase(getFilesDir().getPath() + "/my_db.db", null);
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
    }
}
