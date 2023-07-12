package com.example.project73.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.project73.R;
import com.example.project73.model.Feedback;
import com.example.project73.mvvm.FeedbackListViewModel;

public class SuggestFragment extends Fragment {

    private static final String ARG_FEEDBACK_ID = "feedback_id";
    private static final String TAG = "SuggestFragment";

    private Feedback mFeedback;
    private FeedbackListViewModel mFeedbackListViewModel;


    private FeedbackListViewModel getSuggestViewModel(){
        if(mFeedbackListViewModel == null){
            mFeedbackListViewModel = new ViewModelProvider(this).get(FeedbackListViewModel.class);
        }
        return mFeedbackListViewModel;
    }

    public static SuggestFragment newInstance(int feedbackId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_FEEDBACK_ID, feedbackId);
        SuggestFragment fragment = new SuggestFragment();
        fragment.setArguments(args);
        return null;
    }

    public SuggestFragment() {
        // Required empty public constructor
    }

    public static SuggestFragment newInstance(String param1, String param2) {
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedback = new Feedback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_suggest, container, false);
    }
}