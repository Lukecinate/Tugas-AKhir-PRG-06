package com.example.project73.api;

import com.example.project73.model.PicArea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PicAreaService {
    @GET("getAllPicArea")
    Call<List<PicArea>> getAllPicArea();

    @GET("getPicArea")
    Call<PicArea> getPicArea(@Query("id") int id);
}
