package com.example.cxyang.pomelotiming;

import android.content.Intent;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    public static final String SERVER_URL = "http://10.222.254.12";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view){
        //RequestQueue queue = Volley.newRequestQueue(this);
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).
                getRequestQueue();

        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("username", message);
        }catch (JSONException e1){
            e1.printStackTrace();
        }
        Log.e("post",jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, SERVER_URL, jsonObject,
                new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                Log.e("normalResponse", response.toString());
            }
            }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError arg0){
                Log.e("errorResponse", arg0.toString());
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);

        Intent intent = new Intent(this, DisplayMessageActivity.class);
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
}
