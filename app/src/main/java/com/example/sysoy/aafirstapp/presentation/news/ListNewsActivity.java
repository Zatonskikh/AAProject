package com.example.sysoy.aafirstapp.presentation.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;

import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.utils.DataUtils;

public class ListNewsActivity extends AppCompatActivity {

    private void initScreen(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_news_activity);

        ListView rw = findViewById(R.id.rw);
        rw.setAdapter( new NewsAdapter(this,
                DataUtils.generateNews()));
    }
}
