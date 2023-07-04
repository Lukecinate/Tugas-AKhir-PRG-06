package com.example.project73.api;

import com.example.project73.model.Feedback;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface FeedbackService {
    @GET("getAllFeedback")
    Call<List<Feedback>> getAllFeedback();

    @GET("getAllFeedbackBefore")
    Call<List<Feedback>> getAllFeedbackBefore();

    @GET("getAllFeedbackOngoing")
    Call<List<Feedback>> getAllFeedbackOngoing();

    @GET("getAllFeedbackAfter")
    Call<List<Feedback>> getAllFeedbackAfter();

    @GET
    Call<List<Feedback>> search(@Query("keyword") String keyword);

    @POST("saveFeedback")
    Call<Feedback> saveFeedback(@Body Feedback feedback);

    @PUT
    Call<Feedback> update(@Query("id") String id, @Body Feedback feedback);
}

