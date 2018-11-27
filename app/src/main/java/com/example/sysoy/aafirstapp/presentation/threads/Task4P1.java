package com.example.sysoy.aafirstapp.presentation.threads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sysoy.aafirstapp.R;

import java.lang.ref.WeakReference;

public class Task4P1 extends AppCompatActivity {

    private LeftLeg left;
    private RightLeg right;
    private TextView mainText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_test_activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyHandler handler = new MyHandler();
        mainText = findViewById(R.id.main_text);
        left = new LeftLeg(handler, this);
        right = new RightLeg(handler, this);
        new Thread(left).start();
        new Thread(right).start();
    }

    void setText(String text){
        runOnUiThread(new TextSetter(text, mainText));
    }

    @Override
    protected void onStop() {
        super.onStop();
        left.stop();
        right.stop();
    }

    private class MyHandler {
        int current = 0;

        synchronized public void setCurrent(int current) {
            this.current = current;
        }

        public int getCurrent() {
            return current;
        }
    }

    private class LeftLeg implements Runnable {

        MyHandler handler;
        private boolean isRunning = true;
        WeakReference<Task4P1> mainActivity;

        LeftLeg(MyHandler handler, Task4P1 mainActivity) {
            this.handler = handler;
            this.mainActivity = new WeakReference<>(mainActivity);
        }

        void stop() {
            isRunning = false;
        }

        void setText(){
            if (mainActivity.get() != null){
                mainActivity.get().setText("Left");
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 0) {
                    System.out.println("Left step");
                    setText();
                    handler.setCurrent(1);
                }
            }

        }
    }

    private class RightLeg implements Runnable {

        MyHandler handler;
        private boolean isRunning = true;
        WeakReference<Task4P1> mainActivity;

        RightLeg(MyHandler handler, Task4P1 mainActivity) {
            this.handler = handler;
            this.mainActivity = new WeakReference<>(mainActivity);
        }

        void stop() {
            isRunning = false;
        }

        void setText(){
            if (mainActivity.get() != null){
                mainActivity.get().setText("Right");
            }
        }

        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 1) {
                    System.out.println("Right step");
                    setText();
                    handler.setCurrent(0);
                }
            }
        }
    }

    private class TextSetter implements Runnable  {
        String text;
        WeakReference<TextView> tv;

        TextSetter(String text, TextView tv) {
            this.text = text;
            this.tv = new WeakReference<>(tv);
        }
        @Override
        public void run() {
            if (tv.get() != null){
                tv.get().setText(text);
            }
        }
    }
}