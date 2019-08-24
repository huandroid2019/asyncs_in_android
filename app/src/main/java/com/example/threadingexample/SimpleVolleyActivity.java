package com.example.threadingexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

public class SimpleVolleyActivity extends AppCompatActivity {
    RequestQueue requestQueue;
    StringRequest stringRequest;
    JsonObjectRequest jsonObjectRequest;
    TextView tv_response;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_volley);
        requestQueue = Volley.newRequestQueue(this);
        tv_response = findViewById(R.id.tv_response);
    }

    public void makeRequest(View view) {
        stringRequest = new StringRequest(Request.Method.GET, "https://ya.ru", new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                tv_response.setText(s);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        tv_response.setText("something wrong\n"+volleyError.getMessage());
                    }
                });
        stringRequest.setTag("ya");
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onStop() {
        requestQueue.cancelAll(new RequestQueue.RequestFilter(){

            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
        super.onStop();
    }

    public void getJSON(View view) {
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                "https://api.bittrex.com/api/v1.1/public/getmarketsummaries",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                       /* try {
                            JSONArray array = jsonObject.getJSONArray("result");
                            JSONObject object = (JSONObject)array.get(0);
                            object.getString("MarketName");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }*/
                        tv_response.setText(jsonObject.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        tv_response.setText(volleyError.getLocalizedMessage());
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
}
