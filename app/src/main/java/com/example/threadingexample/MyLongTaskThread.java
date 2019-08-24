package com.example.threadingexample;

import android.os.Handler;
import android.os.HandlerThread;

public class MyLongTaskThread extends HandlerThread {
    public Handler taskHandler;
    public MyLongTaskThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        super.run();

    }
}
