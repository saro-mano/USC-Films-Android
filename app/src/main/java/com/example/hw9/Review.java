package com.example.hw9;

public class Review {
    private String reviewName;
    private String rating;
    private String content;

    public Review(String reviewName, String rating, String content) {
        this.reviewName = reviewName;
        this.rating = rating;
        this.content = content;
    }

    public String getReviewName() {
        return reviewName;
    }

    public void setReviewName(String reviewName) {
        this.reviewName = reviewName;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
