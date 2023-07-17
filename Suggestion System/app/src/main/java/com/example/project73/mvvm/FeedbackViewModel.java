package com.example.project73.mvvm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project73.helpers.UploadHelper;
import com.example.project73.model.Feedback;
import com.example.project73.repository.FeedbackRepository;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class FeedbackViewModel extends ViewModel {
    private static final String TAG = "FeedbackListViewModel";

    private MutableLiveData<List<Feedback>> feedbackMutableLiveData;
    private UploadHelper mUploadHelper;
    public FeedbackRepository feedbackRepository;

    public FeedbackViewModel(){
        Log.d(TAG, "FeedbackListViewModel() constructor onCalled");
        feedbackRepository = FeedbackRepository.get();
    }

    public LiveData<List<Feedback>> getFeedbacks(){
        return feedbackRepository.getFeedbacks();
//        feedbackMutableLiveData = feedbackRepository.getFeedbacks();
//        Log.d(TAG,"FeedbackListViewModel.getFeedbacks() called : " + feedbackRepository.getFeedbacks());
//        return feedbackMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksAfter(){
        feedbackMutableLiveData = feedbackRepository.getFeedbacksAfter();
        Log.d(TAG,"FeedbackListViewModel.getFeedbacksAfter() called : " + feedbackMutableLiveData.toString());
        return feedbackMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksBefore(){
        feedbackMutableLiveData = feedbackRepository.getFeedbacksBefore();
        Log.d(TAG,"FeedbackListViewModel.getFeedbacksBefore() called : " + feedbackMutableLiveData.toString());
        return feedbackMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksOngoing(){
        feedbackMutableLiveData = feedbackRepository.getFeedbacksOngoing();
        Log.d(TAG,"FeedbackListViewModel.getFeedbacksOngoing() called : " + feedbackMutableLiveData.toString());
        return feedbackMutableLiveData;
    }

    public void addFeedback(Feedback feedback){
        Log.d(TAG,"FeedbackListViewModel.addFeedback() called");
        feedbackRepository.addFeedback(feedback);
    }

    public void updateFeedback(String id, Feedback feedback){
        Log.d(TAG,"FeedbackListViewModel.updateFeedback() called");
        feedbackRepository.updateFeedback(id, feedback);
    }

    public MutableLiveData<List<Feedback>> getFeebacksByKeywords(String keyword){
        feedbackMutableLiveData = feedbackRepository.getFeedback(keyword);
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksByKeyword() called");
        return  feedbackMutableLiveData;
    }

    public void addFeedback(MultipartBody.Part file, int area_id, String created_date, String deadline, String pre_status, String suggest_name, String suggestion, String title){
        Log.d(TAG, "uploadData uploadfile onCalled()");
        Log.d(TAG, "result : " + file+area_id);
        feedbackRepository.uploadfile(file, area_id, created_date, deadline, pre_status, suggest_name, suggestion, title);
    }

}
