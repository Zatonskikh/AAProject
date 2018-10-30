package com.example.sysoy.aafirstapp.presentation.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.sysoy.aafirstapp.R;

public class NYDetailsActivity extends AppCompatActivity {

    private WebView wv;
    private ProgressBar pb;

    public static void start(Context context, String url, String category){
        Intent intent = new Intent(context, NYDetailsActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("category", category);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ny_details);
        initScreen();
        initToolbar();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initScreen(){
        String url = getIntent().getStringExtra("url");
        wv = findViewById(R.id.root_web_view);
        pb = findViewById(R.id.progress);
        LinearLayout errorScreen = findViewById(R.id.error_screen);
        AppCompatButton retryButton = findViewById(R.id.button_retry);
        retryButton.setOnClickListener(view -> {
            wv.loadUrl(url);
            errorScreen.setVisibility(View.GONE);
        });
        wv.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                pb.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                pb.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                errorScreen.setVisibility(View.VISIBLE);
            }
        });
        wv.loadUrl(url);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbarer);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
        toolbar.setTitle(getIntent().getStringExtra("category").toUpperCase());
    }

    @Override
    public void onBackPressed() {
        if(wv.canGoBack()){
            wv.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
