package com.example.threadingexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import static java.lang.Thread.sleep;

/**
 * еще немного прохендлеры. метод post
 */
public class MoreHandlersActivity extends AppCompatActivity {
    TextView tv_1, tv_2, tv_3, tv_result;
    Handler handler;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_handlers);
        tv_1 = findViewById(R.id.tv_1);
        tv_2 = findViewById(R.id.tv_2);
        tv_3 = findViewById(R.id.tv_3);
        tv_result = findViewById(R.id.tv_result);
        imageView = findViewById(R.id.imageView);

        //это просто интерфейс на задачу
        //здесь задача установить текущее время в tv_3 и заставить tv_3
        //запустить эту же задачу через 1 секунду
        Runnable task = new Runnable() {
            @Override
            public void run() {
                tv_3.setText(Calendar.getInstance().getTime().toString());
                //post() выполняется в UI-потоке, никаких других потоков здесь нет!
                tv_3.postDelayed(this,1000);
            }
        };
        //и сразу первый вызов
        tv_3.post(task);

        //правильный хендлер создан в MainActivity, но этот для примера - пойдет
        //лучше его делать статик и со слабой ссылкой
        //этот хендлер отправляет сообщения в handleMessaage, где они их ловит и снова же отправляет в handleMessage
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                tv_1.setText(Calendar.getInstance().getTime().toString());
                handler.sendEmptyMessageDelayed(0,1000);
            }
        };
        //начинаем круговорот
        handler.sendEmptyMessage(0);

        //выше были два примера таймеров без потоков, но здесь просто пример как из
        //одного потока запускать задачи в другом
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    //tv_2 "постит" задачу в свой поток (UI)
                    tv_2.post(new Runnable() {
                        @Override
                        public void run() {
                            tv_2.setText(Calendar.getInstance().getTime().toString());
                        }
                    });
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();


    }

    public void onClick(View v){
        //задержка появления/исчезновения картинки с помощью хендлера
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(imageView.getVisibility()==View.GONE?View.VISIBLE:View.GONE);
            }
        },3000);
        //загрузка данных из сети только не в UI-потоке
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String res = getFromUrl();
                //не в UI потоке нельзя напрямую установит ьтекст в tb_result
                //но отправить задание на установку можно
                tv_result.post(new Runnable() {
                    @Override
                    public void run() {
                        tv_result.setText(res);
                    }
                });
            }
        }).start();

    }

    /**
     * получаем строку с сайта по url.
     * @return
     */
    String getFromUrl(){
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
}
