package com.example.sysoy.aafirstapp.presentation.news;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.sysoy.aafirstapp.R;

public class NYDetailsActivity extends AppCompatActivity {

    private WebView wv;
    private ProgressBar pb;
    private String url;

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
        url = getIntent().getStringExtra("url");
        wv = findViewById(R.id.root_web_view);
        pb = findViewById(R.id.progress);
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

        });
        wv.loadUrl(url);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(view -> super.onBackPressed());
        Intent intent = getIntent();
        toolbar.setTitle(intent.getStringExtra("category"));
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
