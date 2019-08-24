package com.example.threadingexample;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.example.threadingexample.retrofit_example.RetrofitActivity;

public class MyService extends Service {
    public MyService() {
    }
    final double MAX = 1e5;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;//super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        doWork();
    }
    public void doWork(){
        double s= 0;
        double percents = 0;
        for(int i = 0; i < MAX;++i){
            s += i*i*i + Math.sqrt(i) - 4;
            percents = (i+1)/MAX*100;
            Log.d("service",""+i);
        }
        Log.d("service","result ="+s);
        startActivity(new Intent(this, RetrofitActivity.class));
        stopSelf();
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
