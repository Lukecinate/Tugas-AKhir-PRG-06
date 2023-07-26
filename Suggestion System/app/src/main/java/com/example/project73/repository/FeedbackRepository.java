package com.example.project73.repository;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.project73.api.ApiUtils;
import com.example.project73.api.FeedbackService;
import com.example.project73.model.Feedback;

import java.io.IOException;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackRepository {
    //Variable Const
    public static final String TAG ="FeedbackRepository";

    //Attribute
    private static FeedbackRepository INSTANCE;
    private FeedbackService feedbackService;
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public FeedbackRepository(Context context) {
        feedbackService = ApiUtils.getFeedbackService();
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void initialize(Context context){
        if (INSTANCE == null){
            INSTANCE = new FeedbackRepository(context);
        }
    }

    public static FeedbackRepository get(){
        return INSTANCE;
    }

    public MutableLiveData<List<Feedback>> getFeedbacks(){
        MutableLiveData<List<Feedback>> feedbacks = new MutableLiveData<>();

        Call<List<Feedback>> call = feedbackService.getAllFeedback();
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                if (response.isSuccessful()){
                    feedbacks.setValue(response.body());
                    Log.d(TAG, "getFeedbacks.onResponse() " );
                }
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.e(TAG, "FAILURE : " + t.getMessage());
            }
        });

        return feedbacks;
    }

    public MutableLiveData<Feedback> getFeedbackById(String id){
        MutableLiveData<Feedback> feedback = new MutableLiveData<>();

        Call<Feedback> call = feedbackService.getFeedback(id);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                Log.d(TAG, "getFeedback(...).onResponse() called : " + response.body());
                feedback.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                Log.e(TAG,"FAILURE : " + t.getMessage());
            }
        });

        return feedback;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksBefore(){
        MutableLiveData<List<Feedback>> feedbacks = new MutableLiveData<>();

        Call<List<Feedback>> call = feedbackService.getAllFeedbackBefore();
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                feedbacks.setValue(response.body());
                Log.d(TAG, "getFeedbacks.onResponse() called");
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.e(TAG,"FAILURE : " + t.getMessage());
            }
        });

        return feedbacks;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksAfter(){
        MutableLiveData<List<Feedback>> feedbacks = new MutableLiveData<>();

        Call<List<Feedback>> call = feedbackService.getAllFeedbackAfter();
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                feedbacks.setValue(response.body());
                Log.d(TAG, "getFeedbacks.onResponse() called");
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.e(TAG,"FAILURE : " + t.getMessage());
            }
        });

        return feedbacks;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksOngoing(){
        MutableLiveData<List<Feedback>> feedbacks = new MutableLiveData<>();

        Call<List<Feedback>> call = feedbackService.getAllFeedbackOngoing();
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                feedbacks.setValue(response.body());
                Log.d(TAG, "getFeedbacks.onResponse() called");
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.e(TAG,"FAILURE : " + t.getMessage());
            }
        });

        return feedbacks;
    }

    public MutableLiveData<List<Feedback>> getFeedback(String keyword){
        MutableLiveData<List<Feedback>> feedback = new MutableLiveData<>();

        Call<List<Feedback>> call = feedbackService.search(keyword);
        call.enqueue(new Callback<List<Feedback>>() {
            @Override
            public void onResponse(Call<List<Feedback>> call, Response<List<Feedback>> response) {
                feedback.setValue(response.body());
                Log.d(TAG, "onResponse() called");
            }

            @Override
            public void onFailure(Call<List<Feedback>> call, Throwable t) {
                Log.e(TAG,"FAILURE : " + t.getMessage());
            }
        });

        return feedback;
    }

    public MutableLiveData<Feedback> doAttempt(String id){
        MutableLiveData<Feedback> feedback = new MutableLiveData<>();

        Call<Feedback> call = feedbackService.doAttempt(id);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                Log.d(TAG,"doAttempt.response() onCalled");
                if(response.isSuccessful()){
                    Log.d(TAG,"Data : " + response.body());
                    feedback.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                Log.e(TAG, "FAILURE : " + t.getMessage());
            }
        });
        return feedback;
    }

    public MutableLiveData<ResponseBody> uploadfile(MultipartBody.Part file, int area_id, String created_date,String deadline,String pre_status, String suggest_name, String suggestion, String title) {
        MutableLiveData<ResponseBody> respon = new MutableLiveData<>();
        Call<ResponseBody> call = feedbackService.uploadfile(  file,   area_id,created_date,   deadline,pre_status,  suggest_name,  suggestion, title);
        Log.d(TAG,"Found a File : " + file.toString());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG,"uploadfile.response() onCalled");
                if(response.isSuccessful()) {
                    Log.d(TAG,"Data : " + response.body());
                    respon.setValue(response.body());
                }else{
                    Log.d(TAG,"ERROR : "+response.raw().request().url().toString());
                    Log.d(TAG,"ERROR MESSAGE : " + response.toString());

                    try {
                        String errorResponse = response.errorBody().string();
                        Log.e(TAG, "ERROR MESSAGE WHILE UPLOADING : " + errorResponse);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "FAILURE : " + t.getMessage());
            }
        });

        return respon;
    }
}
