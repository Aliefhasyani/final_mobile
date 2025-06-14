package com.example.final_mobile.model;

import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("price")
    private String price;

    @SerializedName("url")
    private String url;

    @SerializedName("pic")  // Changed from "image" to "pic"
    private String pic;     // Changed variable name

    @SerializedName("category")
    private String category;
    @SerializedName("desc_text")
    private String desc_text;

    public Course(String title, String price, String url, String pic, String category, String id, String desc_text) {
        this.title = title;
        this.price = price;
        this.url = url;
        this.pic = pic;
        this.category = category;
        this.id = id;
        this.desc_text = desc_text;
    }

    // Getters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getUrl() { return url; }
    public String getImage() { return pic; }  // Keep method name for compatibility
    public String getCategory() { return category; }
    public String getDesc_text() { return desc_text; }
}