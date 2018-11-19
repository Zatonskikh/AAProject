package com.example.sysoy.aafirstapp.models;

public class NewsItem {

    private final String title;
    private final String imageUrl;
    private String category;
    private final String publishDate;
    private final String previewText;
    private final String urlToFull;

    public NewsItem(String title, String imageUrl, String category, String publishDate, String previewText, String urlToFull) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.category = category;
        this.publishDate = publishDate;
        this.previewText = previewText;
        this.urlToFull = urlToFull;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public String getPreviewText() {
        return previewText;
    }

    public String getUrlToFull() {
        return urlToFull;
    }

    public void setCategory(String newCategory){category = newCategory;};
}
