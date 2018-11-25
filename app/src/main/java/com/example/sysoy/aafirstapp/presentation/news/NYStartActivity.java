package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;

import com.bumptech.glide.Glide;
import com.example.sysoy.aafirstapp.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.relex.circleindicator.CircleIndicator;

public class NYStartActivity extends AppCompatActivity {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final int NUM_PAGES = 3;

    ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPref = getPreferences(MODE_PRIVATE);
        if(!sharedPref.getBoolean("is_first_time", true)){
            startSecondActivity();
        }else {
            sharedPref.edit().putBoolean("is_first_time", false).apply();
        }
        setContentView(R.layout.view_pager_start_screen);
        mPager = findViewById(R.id.start_pager);
        mPager.setBackgroundResource(android.R.color.holo_orange_dark);
        mPagerAdapter = new StartPagerAdapter(getSupportFragmentManager()) {
        };
        mPager.setAdapter(mPagerAdapter);
        CircleIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        mPager.setBackgroundResource(android.R.color.holo_orange_dark);
                        break;
                    case 1:
                        mPager.setBackgroundResource(android.R.color.holo_blue_dark);
                        break;
                    case 2:
                        mPager.setBackgroundResource(android.R.color.holo_purple);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        AppCompatButton skipButton = findViewById(R.id.skip_button);
        skipButton.setOnClickListener(view -> startSecondActivity());

    }

    private void startSecondActivity() {
        startActivity(new Intent(this, NYRecyclerActivity.class));
        finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        compositeDisposable.dispose();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    class StartPagerAdapter extends FragmentStatePagerAdapter {
        StartPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int i) {
            return StartFragment.newInstance(i);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
