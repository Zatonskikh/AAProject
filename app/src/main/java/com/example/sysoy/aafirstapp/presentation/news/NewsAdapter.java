package com.example.sysoy.aafirstapp.presentation.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

import io.reactivex.annotations.NonNull;

public class NewsAdapter extends BaseAdapter {

    private final List<NewsItem> newsItems;
    private final LayoutInflater inflater;
    private final RequestManager imageLoader;

    private String dateFormatter(Date date){
        return DateFormat.getDateInstance().format(date);
    }

    NewsAdapter(Context context, List<NewsItem> newsItems) {
        this.newsItems = newsItems;
        inflater = LayoutInflater.from(context);
        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.default_news_image)
                .fallback(R.drawable.default_news_image)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    private ViewHolder onCreateViewHolder(ViewGroup viewGroup){
        return new ViewHolder(inflater.inflate(R.layout.news_item, viewGroup, false));
    }

    private void onBindViewHolder(ViewHolder holder, int position){
        NewsItem newsItem = getNewsItem(position);
        holder.newsCategory.setText(newsItem.getCategory().getName());
        holder.newsHeader.setText(newsItem.getTitle());
        holder.newsBody.setText(newsItem.getPreviewText());
        holder.newsDate.setText(dateFormatter(newsItem.getPublishDate()));
        imageLoader
                .asBitmap()
                .load(newsItem.getImageUrl())
                .into(holder.newsImage);
    }

    public NewsItem getNewsItem(int position) {
        return newsItems.get(position);
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int i) {
        return newsItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null) {
            holder = onCreateViewHolder(viewGroup);
            view = holder.root;
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        onBindViewHolder(holder, i);
        return view;
    }

    static class ViewHolder {
        View root;

        ImageView newsImage;
        TextView newsCategory;
        TextView newsHeader;
        TextView newsBody;
        TextView newsDate;

        ViewHolder(@NonNull View view){
            this.root = view;
            this.newsCategory = view.findViewById(R.id.category);
            this.newsHeader = view.findViewById(R.id.header);
            this.newsImage = view.findViewById(R.id.news_image);
            this.newsBody = view.findViewById(R.id.body);
            this.newsDate = view.findViewById(R.id.date);
        }
    }
}
