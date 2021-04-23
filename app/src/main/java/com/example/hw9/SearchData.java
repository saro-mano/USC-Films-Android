package com.example.hw9;

public class SearchData {
    private String id;
    private String title;
    private String media_type;
    private String background;
    private String rating;
    private String year;

    public SearchData(String id, String title, String media_type, String background, String rating, String year) {
        this.id = id;
        this.title = title;
        this.media_type = media_type;
        this.background = background;
        this.rating = rating;
        this.year = year;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
