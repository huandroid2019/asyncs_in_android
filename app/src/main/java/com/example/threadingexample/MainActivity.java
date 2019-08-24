package com.example.threadingexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.threadingexample.retrofit_example.RetrofitActivity;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {

    TextView tv_progress, tv_result;
    ProgressBar progressBar;
    final double MAX = 1e5;
    Thread thread;


    static class MyProgressHandler extends Handler{
        private final WeakReference<MainActivity> activityWeakReference;
        MyProgressHandler(MainActivity activity) {
            this.activityWeakReference = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity activity = activityWeakReference.get();
            if(activity == null) {
                return;
            }
            activity.progressBar.setProgress(msg.what);
            activity.tv_progress.setText(""+msg.what);
            if(msg.arg1!=0){
                activity.tv_result.setText(""+(double)msg.obj);
            }
        }

    }
    MyProgressHandler myProgressHandler;
    /*Handler handler = new Handler(){
       @Override
       public void handleMessage(@NonNull Message msg) {
           progressBar.setProgress(msg.what);
           tv_progress.setText(""+msg.what);
           if(msg.arg1==1){
               tv_result.setText(""+(double)msg.obj);
           }
           super.handleMessage(msg);
       }
   };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tv_progress = findViewById(R.id.tv_progress);
        tv_result = findViewById(R.id.tv_result);
        progressBar = findViewById(R.id.progressBar);
       myProgressHandler = new MyProgressHandler(this);

    }

    void doWork() {//foreign thread
        double s= 0;
        double percents = 0;
        for(int i = 0; i < MAX;++i){
            s += i*i*i + Math.sqrt(i) - 4;
            percents = (i+1)/MAX*100;

            ///handler.sendEmptyMessage((int) percents);
            myProgressHandler.sendEmptyMessage((int) percents);
            if(Thread.interrupted()) {
                break;
            }
        }

        myProgressHandler.sendMessage(myProgressHandler.obtainMessage(100, 1,0,s));
        //handler.sendMessage(handler.obtainMessage((int) percents,1,0,s));
    }

    public void onClick(View view) {
       thread = new Thread(new Runnable() {
            @Override
            public void run() {
               doWork();
            }
        });

        thread.start();
       //doWork();
    }
    public void onCancelClick(View view) {
        if(thread!=null&& thread.isAlive()){
            thread.interrupt();
        }
    }

    @Override
    protected void onDestroy() {
        this.onCancelClick(null);
        super.onDestroy();
    }

    public void serviceStart(View view) {
        startService(new Intent(this,MyService.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(0,0,0,"web ");
        item.setIcon(android.R.drawable.arrow_up_float);
        MenuItem item2 = menu.add(0,1,1,"web activity good async task");
        MenuItem item3 = menu.add(0,2,2,"handler thread");
        MenuItem item4 = menu.add(0,3,3,"more handlers");
        MenuItem item5 = menu.add(0,4,4,"Volley example");
        MenuItem item6 = menu.add(0,5,5,"Retrofit Activity");

        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item2.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        item3.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        item4.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        item5.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
        item6.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==0){
            startActivity(new Intent(this, WebAcivityBadAsync.class));
            return true;
        }
        if(item.getItemId()==1){
            startActivity(new Intent(this, WebAcivityGoodAsync.class));
            return true;
        }
        if(item.getItemId()==2){
            startActivity(new Intent(this, HandlerThreadActivity.class));
            return true;
        }
        if(item.getItemId()==3){
            startActivity(new Intent(this, MoreHandlersActivity.class));
            return true;
        }
        if(item.getItemId()==4){
            startActivity(new Intent(this, SimpleVolleyActivity.class));
            return true;
        }
        if(item.getItemId()==5){
            startActivity(new Intent(this, RetrofitActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
