package com.example.sysoy.aafirstapp.presentation.threads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.sysoy.aafirstapp.R;

public class Task4P1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_test_activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyHandler handler = new MyHandler();
        new Thread(new LeftLeg(handler)).start();
        new Thread(new RightLeg(handler)).start();
    }

    private class MyHandler{
        int current = 0;
        synchronized public void setCurrent(int current){
            this.current = current;
        }
        public int getCurrent(){
            return current;
        }
    }

    private class LeftLeg implements Runnable {

        MyHandler handler;
        LeftLeg(MyHandler handler){
            this.handler = handler;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 0){
                    System.out.println("Left step");
                    handler.setCurrent(1);
                }
            }
        }
    }

    private class RightLeg implements Runnable {

        MyHandler handler;
        RightLeg(MyHandler handler){
            this.handler = handler;
        }

        private boolean isRunning = true;
        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 1) {
                    System.out.println("Right step");
                    handler.setCurrent(0);
                }
            }
        }
    }

}

