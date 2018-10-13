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

public class NewsAdapter extends BaseAdapter {

    private final List<NewsItem> newsItems;
    private final Context context;
    private final LayoutInflater inflater;
    private final RequestManager imageLoader;

    private String dateFormatter(Date date){
        return DateFormat.getDateInstance().format(date);
    }

    NewsAdapter(Context context, List<NewsItem> newsItems) {
        this.newsItems = newsItems;
        this.context = context;
        inflater = LayoutInflater.from(context);

        RequestOptions imageOption = new RequestOptions()
                .placeholder(R.drawable.default_news_image)
                .fallback(R.drawable.default_news_image)
                .centerCrop();
        this.imageLoader = Glide.with(context).applyDefaultRequestOptions(imageOption);
    }

    private NewsItem getNewsItem(int position) {
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
            view = inflater.inflate(R.layout.news_item, viewGroup, false);
            holder = new ViewHolder();
            holder.newsCategory = view.findViewById(R.id.category);
            holder.newsHeader = view.findViewById(R.id.header);
            holder.newsImage = view.findViewById(R.id.news_image);
            holder.newsBody = view.findViewById(R.id.body);
            holder.newsDate = view.findViewById(R.id.date);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }

        NewsItem newsItem = getNewsItem(i);

        holder.newsCategory.setText(newsItem.getCategory().getName());
        holder.newsHeader.setText(newsItem.getTitle());
        holder.newsBody.setText(newsItem.getPreviewText());
        holder.newsDate.setText(dateFormatter(newsItem.getPublishDate()));
        //newsImage.setImageBitmap(new Bitmap.createBitmap());
       imageLoader
                .asBitmap()
                .load(newsItem.getImageUrl())
                .into(holder.newsImage);
        return view;
    }

    static class ViewHolder {
         ImageView newsImage;
        TextView newsCategory;
        TextView newsHeader;
        TextView newsBody;
        TextView newsDate;
    }
}
