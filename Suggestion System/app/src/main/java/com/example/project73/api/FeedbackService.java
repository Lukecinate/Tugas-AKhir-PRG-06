package com.example.project73.api;

import com.example.project73.model.Feedback;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface FeedbackService {
    @GET("getAllFeedback")
    Call<List<Feedback>> getAllFeedback();

    @GET("getFeedback/")
    Call<Feedback> getFeedback(@Query("id") String id);

    @GET("getAllFeedbackBefore")
    Call<List<Feedback>> getAllFeedbackBefore();

    @GET("getAllFeedbackOngoing")
    Call<List<Feedback>> getAllFeedbackOngoing();

    @GET("getAllFeedbackAfter")
    Call<List<Feedback>> getAllFeedbackAfter();

    @GET("getFeebackByKeywords")
    Call<List<Feedback>> search(@Query("keyword") String keyword);

    @GET("countFeedbackBeforeStatus")
    Call<Long> countBeforeStatus();

    @GET("countFeedbackOngoingStatus")
    Call<Long> countOngoingStatus();

    @GET("countFeedbackAfterStatus")
    Call<Long> countAfterStatus();

    @PUT("doAttemptFeedback")
    Call<Feedback> doAttempt(@Query("id") String id);

    @Multipart
    @PUT("doFinishFeedback")
    Call<ResponseBody> doFinish(@Part MultipartBody.Part file, @Query("id") int id, @Query("worker_name") String worker_name);

    @Multipart
    @POST("savefeedback")
    Call<ResponseBody> uploadfile(@Part MultipartBody.Part file, @Query("area_id") int area_id, @Query("created_date") String created_date, @Query("deadline") String deadline, @Query("pre_status") String pre_status, @Query("suggest_name") String suggest_name, @Query("suggestion") String suggestion, @Query("title") String title);
}

