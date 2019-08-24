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
        /***
         * HandleThread в отличие от Thread не завершается, а ждет задачи из очереди сообщений
         */
        myLongTask = new HandlerThread("task");
        //вот, ждет...
        myLongTask.start();
        //этот хендлер привязан к потоку myLongTask и все сообщения и задания будет отправлять ему
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
        //а этот привязан к UI потоку
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
            //запускаем задание в потоке myLongTask (к нему привязан taskHandler)
            taskHandler.post(new Runnable() {
                @Override
                public void run() {
                    int k = inx;
                    for (int i = 0; i < 1e4; ++i) {
                        Log.d("task", " task #" + k + " progress:" + i);
                        //прогресс показываем в UI с помощью его хендлера
                        uiHandler.sendMessage(uiHandler.obtainMessage(i, k, 0, "foo"));
                    }
                }
            });
        }else if(v.getId()==R.id.btn_send){//стави myLongTask на паузу
            Log.d("task", "send to thread");
            taskHandler.sendEmptyMessage(555);

        }
    }

}
