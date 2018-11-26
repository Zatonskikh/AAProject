package com.example.sysoy.aafirstapp.presentation.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;
import com.example.sysoy.aafirstapp.models.NewsItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static android.support.constraint.Constraints.TAG;

public class NYTimesAdapter extends Adapter<NewsViewHolder> {

    @NonNull
    private final List<NewsItem> newsList = new ArrayList<>();

    @NonNull
    private final RequestManager imageLoader;
    @Nullable
    private final NYTimesAdapter.OnItemClickListener clickListener;

    public NYTimesAdapter(@Nullable NYTimesAdapter.OnItemClickListener clickListener, Context context) {
        this.clickListener = clickListener;
        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.default_news_image)
                .fallback(R.drawable.default_news_image)
                .centerCrop();
        imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return NewsViewHolder.create(viewGroup, clickListener, imageLoader, newsList);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        newsViewHolder.bind(newsList.get(i));
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(NewsItem item);
    }

    public void replaceItems(@NonNull List<NewsItem> items) {
        this.newsList.clear();
        this.newsList.addAll(items);
        notifyDataSetChanged();
    }

    public void removeAt(String title){
        List<NewsItem> resultList = newsList.stream()
                .filter(newsItem -> newsItem.getTitle().equals(title))
                .collect(Collectors.toList());
        int position = newsList.indexOf(resultList.get(0));
        newsList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, newsList.size());
    }

    public void editItem(String title, NewsItem item){
        List<NewsItem> resultList = newsList.stream()
                .filter(newsItem -> newsItem.getTitle().equals(title))
                .collect(Collectors.toList());
        int position = newsList.indexOf(resultList.get(0));
        newsList.remove(position);
        newsList.add(position, item);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, newsList.size());
        notifyDataSetChanged();
    }

}


