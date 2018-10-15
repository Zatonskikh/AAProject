package com.example.sysoy.aafirstapp.presentation.threads;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.sysoy.aafirstapp.R;

public class Task4P1 extends AppCompatActivity {

    private LeftLeg left;
    private RightLeg right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_test_activity);
    }

    @Override
    public void onStart() {
        super.onStart();
        MyHandler handler = new MyHandler();
        TextSetter textSetterLeft = new TextSetter("Left");
        TextSetter textSetterRight = new TextSetter("Right");
        left = new LeftLeg(handler, textSetterLeft);
        right = new RightLeg(handler, textSetterRight);
        new Thread(left).start();
        new Thread(right).start();
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
        TextSetter textSetter;
        private boolean isRunning = true;

        LeftLeg(MyHandler handler, TextSetter textSetter) {
            this.handler = handler;
            this.textSetter = textSetter;
        }

        void stop() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 0) {
                    //System.out.println("Left step");
                    runOnUiThread(textSetter);
                    handler.setCurrent(1);
                }
            }

        }
    }

    private class RightLeg implements Runnable {

        MyHandler handler;
        TextSetter textSetter;
        private boolean isRunning = true;

        RightLeg(MyHandler handler, TextSetter textSetter) {
            this.handler = handler;
            this.textSetter = textSetter;
        }

        void stop() {
            isRunning = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                if (handler.getCurrent() == 1) {
                    //System.out.println("Right step");
                    runOnUiThread(textSetter);
                    handler.setCurrent(0);
                }
            }
        }
    }

    private class TextSetter implements Runnable {
        String text;

        TextSetter(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            TextView tv = findViewById(R.id.main_text);
            tv.setText(text);
        }
    }
}