package com.example.hw9;

public class MovieData {
    private String imgUrl;
    private String id;
    private String media_type;

    public MovieData(String imgUrl, String id,String media_type) {
        this.imgUrl = imgUrl;
        this.id = id;
        this.media_type = media_type;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
