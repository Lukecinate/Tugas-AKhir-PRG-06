package com.example.project73.mvvm;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project73.model.Feedback;
import com.example.project73.repository.FeedbackRepository;

import java.util.List;

public class FeedbackListViewModel extends ViewModel {
    private static final String TAG = "FeedbackListViewModel";

    private MutableLiveData<List<Feedback>> feedbackMutableLiveData;
    public FeedbackRepository feedbackRepository;

    public FeedbackListViewModel(){
        Log.d(TAG, "FeedbackListViewModel() constructor onCalled");
        feedbackRepository = FeedbackRepository.get();
    }

    public MutableLiveData<List<Feedback>> getFeedbacks(){
        feedbackMutableLiveData = feedbackRepository.getFeedbacks();
        Log.d(TAG,"FeedbackListViewModel.getFeedbacks() called : " + feedbackMutableLiveData.toString());
        return feedbackMutableLiveData;
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
}
