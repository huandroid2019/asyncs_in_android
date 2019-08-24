package com.example.threadingexample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Thread.sleep;

public class WebAcivityBadAsync extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_acivity);
    }
    public void onClick(View v){
        new AsyncTask<Void,Void,String>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                findViewById(R.id.progressBar_web).setVisibility(View.VISIBLE);
            }

            @Override
            protected String doInBackground(Void... voids) {

                try {
                    URL url = new URL("https://api.bittrex.com/api/v1.1/public/getmarketsummaries");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;
                    StringBuffer buffer = new StringBuffer();
                    while ((line = reader.readLine())!=null){
                        buffer.append(line);
                        Log.d("data","fetch data");
                    }

                    return buffer.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                    return e.getMessage()+"\n"+e.toString();
                }
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.d("data","post execute");
                findViewById(R.id.progressBar_web).setVisibility(View.GONE);
                ((TextView)findViewById(R.id.tv_result)).setText(s);
            }
        }.execute();
    }
}
