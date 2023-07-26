package com.example.project73.application;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.project73.repository.FeedbackRepository;
import com.example.project73.repository.PicAreaRepository;

public class SuggestionApplication extends Application {
public static final String TAG = "FeedbackApplication";

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "FeedbackApplication.initialize() onCalled");
        FeedbackRepository.initialize(this);
        PicAreaRepository.initialize(this);
    }
}
