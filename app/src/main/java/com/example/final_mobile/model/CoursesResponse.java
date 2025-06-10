// CoursesResponse.java
package com.example.final_mobile.model;

import com.google.gson.annotations.SerializedName;

public class CoursesResponse {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private String price;

    @SerializedName("url")
    private String url;

    @SerializedName("pic")
    private String pic;

    @SerializedName("category")
    private String category;

    @SerializedName("desc_text")
    private String desc_text;

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getUrl() { return url; }
    public String getImage() { return pic; }
    public String getCategory() { return category; }
    public String getDesc_text() { return desc_text; }
}