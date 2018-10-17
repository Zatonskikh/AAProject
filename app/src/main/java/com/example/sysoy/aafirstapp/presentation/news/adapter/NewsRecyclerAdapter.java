package com.example.sysoy.aafirstapp.presentation.news.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.NewsItem;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class NewsRecyclerAdapter extends Adapter<NewsRecyclerAdapter.ViewHolder> {

    @NonNull
    private final List<NewsItem> newsItems;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private final RequestManager imageLoader;
    @Nullable
    private final OnItemClickListener clickListener;

    private String dateFormatter(@NonNull Date date){
        return DateFormat.getDateInstance().format(date);
    }

    public NewsRecyclerAdapter(@NonNull Context context, @NonNull List<NewsItem> newsItems, @Nullable OnItemClickListener clickListener) {
        this.newsItems = newsItems;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.default_news_image)
                .fallback(R.drawable.default_news_image)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    public interface OnItemClickListener{
        void onItemClick(NewsItem item);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        getItemViewType(i);
        return new ViewHolder(inflater.inflate(R.layout.news_item, viewGroup, false), clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(newsItems.get(i));
        viewHolder.itemView.setOnClickListener(view -> {
            if (clickListener != null) {
                clickListener.onItemClick(newsItems.get(i));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsCategory;
        private TextView newsHeader;
        private TextView newsBody;
        private  TextView newsDate;

        ViewHolder(@NonNull View itemView, @Nullable OnItemClickListener clickListener) {
            super(itemView);
            newsCategory = itemView.findViewById(R.id.category);
            newsHeader = itemView.findViewById(R.id.header);
            newsImage = itemView.findViewById(R.id.news_image);
            newsBody = itemView.findViewById(R.id.body);
            newsDate = itemView.findViewById(R.id.date);
        }

        void bind(NewsItem newsItem){
            newsCategory.setText(newsItem.getCategory().getName());
            newsHeader.setText(newsItem.getTitle());
            newsBody.setText(newsItem.getPreviewText());
            newsDate.setText(dateFormatter(newsItem.getPublishDate()));
            imageLoader
                    .load(newsItem.getImageUrl())
                    .into(newsImage);
        }
    }
}
