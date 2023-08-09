package com.example.project73.mvvm;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project73.model.Feedback;
import com.example.project73.repository.FeedbackRepository;

import java.util.List;

import okhttp3.MultipartBody;

public class FeedbackViewModel extends ViewModel {
    private static final String TAG = "FeedbackListViewModel";

    private MutableLiveData<List<Feedback>> feedbacksMutableLiveData;
    private MutableLiveData<Feedback> feedbackMutableLiveData;
    private MutableLiveData<Long> feedbackCountLiveData;
    public FeedbackRepository feedbackRepository;

    public FeedbackViewModel() {
        Log.d(TAG, "FeedbackListViewModel() constructor onCalled");
        feedbackRepository = FeedbackRepository.get();
    }

    public LiveData<List<Feedback>> getFeedbacks() {
        return feedbackRepository.getFeedbacks();
    }

    public MutableLiveData<List<Feedback>> getFeedbacksAfter() {
        feedbacksMutableLiveData = feedbackRepository.getFeedbacksAfter();
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksAfter() called : " + feedbacksMutableLiveData.toString());
        return feedbacksMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksBefore() {
        feedbacksMutableLiveData = feedbackRepository.getFeedbacksBefore();
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksBefore() called : " + feedbacksMutableLiveData.toString());
        return feedbacksMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeedbacksOngoing() {
        feedbacksMutableLiveData = feedbackRepository.getFeedbacksOngoing();
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksOngoing() called : " + feedbacksMutableLiveData.toString());
        return feedbacksMutableLiveData;
    }

    public MutableLiveData<List<Feedback>> getFeebacksByKeywords(String keyword) {
        feedbacksMutableLiveData = feedbackRepository.getFeedback(keyword);
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksByKeyword() called");
        return feedbacksMutableLiveData;
    }

    public MutableLiveData<Feedback> getFeeback(String id) {
        Log.d(TAG, "FeedbackListViewModel.getFeedbacksById(...) called");
        feedbackMutableLiveData = feedbackRepository.getFeedbackById(id);
        return feedbackMutableLiveData;
    }

    public MutableLiveData<Long> countBeforeStatus(){
        Log.d(TAG, "FeedbackListViewModel.countBeforeStatus() called");
        feedbackCountLiveData = feedbackRepository.countBeforeStatus();
        return feedbackCountLiveData;
    }

    public MutableLiveData<Long> countAfterStatus(){
        Log.d(TAG, "FeedbackListViewModel.countAfterStatus() called");
        feedbackCountLiveData = feedbackRepository.countAfterStatus();
        return feedbackCountLiveData;
    }

    public MutableLiveData<Long> countOngoingStatus(){
        Log.d(TAG, "FeedbackListViewModel.countOngoingStatus() called");
        feedbackCountLiveData = feedbackRepository.countOngoingStatus();
        return feedbackCountLiveData;
    }

    public void doAttempt(String id){
        Log.d(TAG, "FeedbackListViewModel.doAttempt(...) called");
        feedbackRepository.doAttempt(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addFeedback(MultipartBody.Part file, int area_id, String created_date, String deadline, String pre_status, String suggest_name, String suggestion, String title) {
        Log.d(TAG, "uploadData.uploadfile onCalled()");
        Log.d(TAG, "result : " + file + ";\nresult ID : " + area_id);
        feedbackRepository.uploadfile(file, area_id, created_date, deadline, pre_status, suggest_name, suggestion, title);
    }

    public void doFinish(MultipartBody.Part file, int id, String worker_name){
        Log.d(TAG, "uploadData.uploadfile onCalled()");
        Log.d(TAG, "result : " + file + ";\nresult ID : " + id);
        feedbackRepository.doFinish(file, id, worker_name);
    }

}
