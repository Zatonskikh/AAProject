package com.example.sysoy.aafirstapp.presentation.news.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.example.sysoy.aafirstapp.R;
import com.example.sysoy.aafirstapp.models.DTO.NewsItemDTO;
import com.example.sysoy.aafirstapp.models.DTO.NewsListDTO;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NYTimesAdapter extends Adapter<NYTimesAdapter.NewsViewHolder> {

    @NonNull
    private final NewsListDTO newsList;
    @NonNull
    private final LayoutInflater inflater;
    @NonNull
    private final RequestManager imageLoader;
    @Nullable
    private final NYTimesAdapter.OnItemClickListener clickListener;

    private String dateFormatter(@NonNull String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd", Locale.getDefault());
        try {
            return DateFormat.getDateInstance().format(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            return "no date";
        }
    }

    public NYTimesAdapter(@NonNull Context context, @NonNull NewsListDTO newsList, @Nullable NYTimesAdapter.OnItemClickListener clickListener) {
        this.newsList = newsList;
        this.clickListener = clickListener;
        inflater = LayoutInflater.from(context);
        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.default_news_image)
                .fallback(R.drawable.default_news_image)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        getItemViewType(i);
        return new NewsViewHolder(inflater.inflate(R.layout.news_item, viewGroup, false),
                clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        newsViewHolder.bind(newsList.getNews().get(i));
    }

    @Override
    public int getItemCount() {
        return newsList.getNews().size();
    }

    public interface OnItemClickListener {
        void onItemClick(NewsItemDTO item);
    }

    class NewsViewHolder extends RecyclerView.ViewHolder {
        private ImageView newsImage;
        private TextView newsCategory;
        private TextView newsHeader;
        private TextView newsBody;
        private TextView newsDate;

        NewsViewHolder(@NonNull View itemView, @Nullable OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (clickListener != null && position != RecyclerView.NO_POSITION) {
                    clickListener.onItemClick(newsList.getNews().get(position));
                }
            });
            this.newsCategory = itemView.findViewById(R.id.category);
            this.newsHeader = itemView.findViewById(R.id.header);
            this.newsImage = itemView.findViewById(R.id.news_image);
            this.newsBody = itemView.findViewById(R.id.body);
            this.newsDate = itemView.findViewById(R.id.date);

        }

        void bind(NewsItemDTO newsItem) {
            newsCategory.setText(newsItem.getSubSection() == null ? "" : newsItem.getSubSection());
            newsHeader.setText(newsItem.getTitle());
            newsBody.setText(newsItem.getAbstractNew());
            newsDate.setText(dateFormatter(newsItem.getPublishedDate()));
            imageLoader
                    .asDrawable()
                    .load(newsItem.getMultimedia() == null ? "" :newsItem.getMultimedia().get(0).getUrl())
                    .into(newsImage);
        }
    }

}


