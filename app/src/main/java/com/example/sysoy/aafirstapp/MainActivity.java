package com.example.sysoy.aafirstapp;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstTime = true;

    private int approximateKeyboardLessSize = 0;

    private AppCompatImageView image;

    private LinearLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScreen();
    }

    private void initScreen() {
        AppCompatEditText tv = findViewById(R.id.email_message);
        AppCompatImageView tg = findViewById(R.id.telegram);
        AppCompatImageView tw = findViewById(R.id.twitter);
        AppCompatImageView in = findViewById(R.id.instagram);
        AppCompatButton sendButton = findViewById(R.id.send_button);
        image = findViewById(R.id.profile_image);
        root = findViewById(R.id.root);

        sendButton.setOnClickListener(getOnClickListener());
        tg.setOnClickListener(getOnClickListener());
        tw.setOnClickListener(getOnClickListener());
        in.setOnClickListener(getOnClickListener());


        root.getViewTreeObserver()
                .addOnGlobalLayoutListener(gll);
        tv.setOnFocusChangeListener(new View
                .OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && view.getId() != R.id.send_button) {
                    hideKeyboard(view);
                }
            }
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    ViewTreeObserver.OnGlobalLayoutListener gll = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect measureRect = new Rect();
            root.getWindowVisibleDisplayFrame(measureRect);
            int keypadHeight = root.getRootView().getHeight() - measureRect.bottom;
            if (isFirstTime) {
                approximateKeyboardLessSize = keypadHeight;
                isFirstTime = false;
            }
            if (keypadHeight > approximateKeyboardLessSize) {
                image.setVisibility(View.GONE);
            } else {
                image.setVisibility(View.VISIBLE);
            }
        }
    };

    private View.OnClickListener getOnClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.telegram:
                        openApp("telegram");
                        break;
                    case R.id.instagram:
                        openApp("instagram");
                        break;
                    case R.id.twitter:
                        openApp("twitter");
                        break;
                    case R.id.send_button:
                        openEmailApp();
                        break;
                }
            }
        };
    }

    private void openEmailApp() {
        AppCompatEditText text = findViewById(R.id.email_message);
        Intent emailIntent = new Intent(Intent.ACTION_SEND)
                .setType("plain/Text")
                .putExtra(Intent.EXTRA_EMAIL, new String[]{"efimzatonskih@gmail.com"})
                .putExtra(Intent.EXTRA_SUBJECT, "Hi there");
        try {
            emailIntent
                    .putExtra(Intent.EXTRA_TEXT, text.getText().toString());
        } catch (NullPointerException e) {
            Toast.makeText(this, R.string.no_text, Toast.LENGTH_SHORT)
                    .show();
        }
        try {
            startActivity(Intent.createChooser(emailIntent, "Sending email..."));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.no_app_to_send, Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void openApp(String appName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        switch (appName) {
            case "telegram":
                intent.setData(Uri.parse("https://telegram.me/addmeto"));
                break;
            case "twitter":
                intent.setData(Uri.parse("https://twitter.com/turbojedi"));
                break;
            case "instagram":
                intent.setData(Uri.parse("https://www.instagram.com/daverapoza"));
                break;
        }
        startActivity(Intent.createChooser(intent, "Open with..."));
    }


}
