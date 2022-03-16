package com.covidsmartapp;

public class NewsDataModel{

    private String source;
    private String title;
    private String description;
    private String url;
    private String urlToImage;
    private String publishDate;

    public NewsDataModel() {}

    public NewsDataModel(String source, String title, String description, String url, String urlToImage, String publishDate) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishDate = publishDate;
    }

    @Override
    public String toString() {
        return ", title='" + title + '\'' + '}';
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishDate() {
        String dateString = publishDate.substring(0, 10);
//        String [] dateArray = dateString.split("-");
//        String year = dateArray[0];
//        String month = dateArray[1];
//        String day = dateArray[2];

        return dateString;
    }

    public void setPublishDate(String publishDate) { this.publishDate = publishDate; }
}
