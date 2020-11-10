package com.example.couchpotato;

import java.util.ArrayList;

public class MovieModelClass {
    private String id;
    private String title;
    private String img;
    private String img2;
    private String reviewScore;
    private String description;

    public MovieModelClass(String id, String title, String img, String img2, String reviewScore, String description) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.img2 = img2;
        this.reviewScore = reviewScore;
        this.description = description;
    }



    public MovieModelClass() {

    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImg() {
        return img;
    }

    public String getReviewScore() {
        return reviewScore;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setReviewScore(String reviewScore) {
        this.reviewScore = reviewScore;
    }


    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImg2() {
        return img2;
    }

    public String getDescription() {
        return description;
    }
}