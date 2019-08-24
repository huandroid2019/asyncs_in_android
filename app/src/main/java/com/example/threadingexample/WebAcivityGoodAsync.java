package com.example.threadingexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

public class WebAcivityGoodAsync extends AppCompatActivity {
    TextView tv_progress, tv_result;
    MyAsync loadData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_acivity);
        tv_progress = findViewById(R.id.tv_progress);
        tv_result = findViewById(R.id.tv_result);
    }
    public static class MyAsync extends AsyncTask<String,Integer, JSONObject>{
        private WeakReference<WebAcivityGoodAsync> reference;
        public MyAsync(WeakReference<WebAcivityGoodAsync> reference){
            this.reference = reference;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            WebAcivityGoodAsync acivity = reference.get();
            if(acivity == null || acivity.isFinishing()){
                return;
            }
            acivity.findViewById(R.id.progressBar_web).setVisibility(View.VISIBLE);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                int progress = 0;
                StringBuffer buffer = new StringBuffer();
                while ((line = reader.readLine())!=null){
                    buffer.append(line);
                    progress += line.length();
                    Log.d("data","fetch data");
                    publishProgress(progress);
                }
                return new JSONObject(buffer.toString());
            } catch (Exception e) {
                e.printStackTrace();
                JSONObject object = new JSONObject();
                try {
                    object.put("error",e.getMessage()+"\n"+e.toString());
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                return object;
            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            WebAcivityGoodAsync acivityGoodAsync = reference.get();
            if(acivityGoodAsync == null||acivityGoodAsync.isFinishing()){
                return;
            }
            acivityGoodAsync.tv_progress.setText(""+values[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            WebAcivityGoodAsync acivityGoodAsync = reference.get();
            if(acivityGoodAsync == null||acivityGoodAsync.isFinishing()){
                return;
            }
            acivityGoodAsync.tv_result.setText(jsonObject.toString());
            acivityGoodAsync.findViewById(R.id.progressBar_web).setVisibility(View.GONE);
        }
    }
    public void onClick(View v){
        loadData = new MyAsync(new WeakReference<WebAcivityGoodAsync>(this));
        loadData.execute("https://api.bittrex.com/api/v1.1/public/getmarketsummaries");
    }

    @Override
    public void onBackPressed() {
        if(loadData!=null){
            loadData.cancel(true);
        }
        super.onBackPressed();
    }
}
