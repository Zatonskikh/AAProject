package com.example.sysoy.aafirstapp.presentation.news.app_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sysoy.aafirstapp.R;

public class StartFragment extends Fragment {

    private static final String ARGS_POSITION = "args:position";

    public static StartFragment newInstance(int position) {

        Bundle args = new Bundle();
        args.putInt(ARGS_POSITION, position);
        StartFragment fragment = new StartFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void screenChooser(int position, AppCompatImageView view, AppCompatTextView text){
        switch (position){
            case 0:
                resourceSetter(view, text, R.drawable.read_news, R.string.read_news);
                break;
            case 1:
                resourceSetter(view, text, R.drawable.ic_edit_black_24dp, R.string.update_news);
                break;
            case 2:
                resourceSetter(view, text, R.drawable.ic_delete_white_24dp, R.string.delete_news);
                break;
        }
    }

    private void resourceSetter(AppCompatImageView view, AppCompatTextView text, int viewId, int textId){
        view.setImageResource(viewId);
        text.setText(textId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.start_fragment, container, false);
        int position =  getArguments().getInt(ARGS_POSITION, 0);
        AppCompatImageView fragmentView = view.findViewById(R.id.fragment_image);
        AppCompatTextView fragmentText = view.findViewById(R.id.fragment_text);
        screenChooser(position, fragmentView, fragmentText);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
