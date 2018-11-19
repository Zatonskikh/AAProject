package com.example.sysoy.aafirstapp.presentation.news.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.RequestManager;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;
import com.example.sysoy.aafirstapp.models.NewsItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;
import static com.example.sysoy.aafirstapp.presentation.news.helpers.Helper.dateFormatter;

public class NewsViewHolder extends RecyclerView.ViewHolder {
    private ImageView newsImage;
    private TextView newsCategory;
    private TextView newsHeader;
    private TextView newsBody;
    private TextView newsDate;

    private RequestManager imageLoader;

    public static NewsViewHolder create(@NonNull ViewGroup viewGroup, NYTimesAdapter.OnItemClickListener clickListener, RequestManager imageLoader, List<NewsItem> newsItems){
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item, viewGroup, false);

        return new NewsViewHolder(view, clickListener, imageLoader, newsItems);
    }

    private NewsViewHolder(@NonNull View itemView, @Nullable NYTimesAdapter.OnItemClickListener clickListener, RequestManager imageLoader, List<NewsItem> newsItems) {
        super(itemView);
        this.imageLoader = imageLoader;
        itemView.setOnClickListener(view -> {
            int position = getAdapterPosition();
            if (clickListener != null && position != RecyclerView.NO_POSITION) {
                clickListener.onItemClick(newsItems.get(position));
            }
        });
        this.newsCategory = itemView.findViewById(R.id.category);
        this.newsHeader = itemView.findViewById(R.id.header);
        this.newsImage = itemView.findViewById(R.id.news_image);
        this.newsBody = itemView.findViewById(R.id.body);
        this.newsDate = itemView.findViewById(R.id.date);

    }

    void bind(NewsItem newsItem) {
        if (newsItem.getCategory() == null){
            newsCategory.setVisibility(View.GONE);
        }else {
            newsCategory.setText(newsItem.getCategory());
        }
        newsHeader.setText(newsItem.getTitle());
        newsBody.setText(newsItem.getPreviewText());
        newsDate.setText(dateFormatter(newsItem.getPublishDate()));
        if (newsItem.getImageUrl().equals("")){
            newsImage.setVisibility(View.GONE);
        }else {
            imageLoader
                    .asDrawable()
                    .load(newsItem.getImageUrl())
                    .into(newsImage);
        }
    }
}