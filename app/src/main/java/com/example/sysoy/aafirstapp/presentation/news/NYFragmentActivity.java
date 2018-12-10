package com.example.sysoy.aafirstapp.presentation.news;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.presentation.about.AboutActivity;
import com.example.sysoy.aafirstapp.presentation.news.app_fragments.FragmentListener;
import com.example.sysoy.aafirstapp.presentation.news.app_fragments.NYDetailsFragment;
import com.example.sysoy.aafirstapp.presentation.news.app_fragments.NewsFragment;
import com.example.sysoy.aafirstapp.presentation.news.ui_utils.ViewState;

public class NYFragmentActivity extends AppCompatActivity implements FragmentListener{

    private final int FRAGMENT_ACTIVITY_LAYOUT = R.layout.frame_activity;

    private AppCompatImageButton delete;
    private AppCompatImageButton edit;
    private AppCompatImageButton save;
    private AppCompatImageView info;
    private AppCompatSpinner spinner;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(FRAGMENT_ACTIVITY_LAYOUT);
        getElements();
        FragmentManager fragmentManager = getSupportFragmentManager();
        setupToolbar(ViewState.NEWS, null);
        if(savedInstanceState == null) {
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.news_fragment, NewsFragment.newInstance(), "firstFragment")
                        .addToBackStack("tag_1")
                        .commit();
        }
        NYDetailsFragment  fragment = (NYDetailsFragment) fragmentManager.findFragmentByTag("secondFragment");
        if (fragment!= null){
            fragmentManager.beginTransaction().replace(R.id.news_details, fragment, "secondFragment").commit();
            findViewById(R.id.news_details).setVisibility(View.VISIBLE);
            findViewById(R.id.news_fragment).setVisibility(View.GONE);
            setupToolbar(ViewState.DETAILS, null);
        }
    }

    @Override
    public void onDetailsClick(String id) {
        setupToolbar(ViewState.DETAILS, null);
        if (this.getResources().getConfiguration().orientation != Configuration.ORIENTATION_LANDSCAPE) {
            findViewById(R.id.news_details).setVisibility(View.VISIBLE);
            findViewById(R.id.news_fragment).setVisibility(View.GONE);
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.news_details, NYDetailsFragment.newInstance(id), "secondFragment")
                .addToBackStack("tag_2")
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getFragments().size() == 1){
            finish();
        }else {
            setupToolbar(ViewState.NEWS, null);
            findViewById(R.id.news_details).setVisibility(View.GONE);
            findViewById(R.id.news_fragment).setVisibility(View.VISIBLE);
            NewsFragment  fragment = (NewsFragment) getSupportFragmentManager().findFragmentByTag("firstFragment");
            if (fragment != null){
                fragment.updateNews();
            }
            super.onBackPressed();
        }
    }

    private void getElements(){
        delete = findViewById(R.id.delete);
        edit = findViewById(R.id.edit);
        save = findViewById(R.id.save);
        spinner = findViewById(R.id.spinner);
        info = findViewById(R.id.ic_info);
        toolbar = findViewById(R.id.toolbar);
    }

    public void setupToolbar(ViewState state, String category){
        switch (state){
            case NEWS:
                setSupportActionBar(toolbar);
                toolbar.setNavigationIcon(null);
                toolbar.setTitle(R.string.news);
                info.setOnClickListener(lambda_-> AboutActivity.start(this));
                delete.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                save.setVisibility(View.GONE);
                spinner.setVisibility(View.VISIBLE);
                info.setVisibility(View.VISIBLE);
                break;
            case DETAILS:
                toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
                toolbar.setNavigationOnClickListener(lambda_view -> onBackPressed());
                delete.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                spinner.setVisibility(View.GONE);
                info.setVisibility(View.GONE);
                break;
        }

    }
}
