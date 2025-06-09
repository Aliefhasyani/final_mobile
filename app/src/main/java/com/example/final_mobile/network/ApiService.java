package com.example.final_mobile.network;

import com.example.final_mobile.model.Course;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("/")
    Call<List<Course>> getCourses(@Query("page") int page);
}