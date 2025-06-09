package com.example.final_mobile.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CoursesResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private String price;

    @SerializedName("url")
    private String url;

    @SerializedName("image")
    private String image;

    @SerializedName("category")
    private String category;

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getUrl() { return url; }
    public String getImage() { return image; }
    public String getCategory() { return category; }
}