package com.example.threadingexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class HandlerThreadActivity extends AppCompatActivity {
    HandlerThread myLongTask;
    Handler taskHandler;
    Handler uiHandler;
    TextView tv_result;
    EditText ed_description;
    int inx = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler_thread);
        myLongTask = new HandlerThread("task");
        myLongTask.start();
        taskHandler = new Handler(myLongTask.getLooper()){
            @Override
            public void handleMessage(@NonNull Message msg) {
                Log.d("task", " catch in thread");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        uiHandler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {

                tv_result.setText("what"+msg.what+"task#"+msg.arg1+" obj"+msg.obj.toString());
            }
        };
        tv_result = findViewById(R.id.tv_result);
        ed_description = findViewById(R.id.ed_desr);

    }
    public void onClick(View v){
        if(v.getId()==R.id.btn_start_task) {
            ++inx;
            taskHandler.post(new Runnable() {
                @Override
                public void run() {
                    int k = inx;
                    for (int i = 0; i < 1e4; ++i) {
                        Log.d("task", " task #" + k + " progress:" + i);
                        uiHandler.sendMessage(uiHandler.obtainMessage(i, k, 0, "foo"));
                    }
                }
            });
        }else if(v.getId()==R.id.btn_send){
            Log.d("task", "send to thread");
            taskHandler.sendEmptyMessage(555);

        }
    }

}
