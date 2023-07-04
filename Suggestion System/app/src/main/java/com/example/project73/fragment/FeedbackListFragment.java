package com.example.project73.fragment;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.project73.R;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.project73.R;
import com.example.project73.model.Feedback;
import com.example.project73.mvvm.FeedbackListViewModel;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class FeedbackListFragment extends Fragment {

    private static final String TAG = "FeedbackListFragment";

    public FeedbackListViewModel feedbackListViewModel;
    public RecyclerView recyclerView;
    private FeedbackAdapter adapter;
    private List<Feedback> feedbacks;

    public interface Callbacks{
        public void onFeedbackSelected(int feedbackId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUI(List<Feedback> feedbacksParam){
        adapter = new FeedbackAdapter(feedbacksParam);
        recyclerView.setAdapter(adapter);
        feedbacks = feedbacksParam;
    }

    public Callbacks callbacks = null;

    public FeedbackListFragment() {
        // Required empty public constructor
    }

    public static FeedbackListFragment newInstance() {
        return new FeedbackListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        feedbackListViewModel = new ViewModelProvider(this)
                .get(FeedbackListViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(TAG, "FeedbackListFragment.onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_feedback_list,container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.feedback_recycle_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(adapter);
        return inflater.inflate(R.layout.fragment_feedback_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i(TAG, "FeedbackListFragment.onViewCreated() called");
        feedbackListViewModel.getFeedbacks().observe(
                getViewLifecycleOwner(),
                new Observer<List<Feedback>>() {
                    @Override
                    public void onChanged(List<Feedback> feedbacks) {
//                        updateUI(feedbacks);
                        Log.i(TAG, "Got feedbacks : " + feedbacks.size());
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView imageView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView statusTextView;
        Feedback feedback;
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");


        public FeedbackHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater.inflate(R.layout.list_item_suggestion, parent, false));
            itemView.setOnClickListener(this);

            imageView = itemView.findViewById(R.id.home_image);
            titleTextView = itemView.findViewById(R.id.home_title_label);
            dateTextView = itemView.findViewById(R.id.home_date_label);
            timeTextView = itemView.findViewById(R.id.home_time_label);
            statusTextView = itemView.findViewById(R.id.home_status_label);
        }

        public void bind(Feedback feedbackParam){
            String date = feedbackParam.getDeadline().format(dateFormat);
            String time = feedbackParam.getDeadline().format(timeFormat);

            if(feedbackParam.getPreStatus().equals("Before") && !feedbackParam.getPostStatus().equals("")){

                Glide.with(imageView.getContext())
                        .load(feedbackParam.getPrePhoto())
                        .into(imageView);
                titleTextView.setText(feedbackParam.getId());
                dateTextView.setText(date);
                timeTextView.setText(time);
                statusTextView.setText(feedbackParam.getPreStatus());
            }else if(feedbackParam.getPostStatus().equals("Ongoing")){
                Glide.with(imageView.getContext())
                        .load(feedbackParam.getPrePhoto())
                        .into(imageView);
                titleTextView.setText(feedbackParam.getId());
                dateTextView.setText(date);
                timeTextView.setText(time);
                statusTextView.setText(feedbackParam.getPostStatus());
            }else if(feedbackParam.getPostStatus().equals("After")){
                Glide.with(imageView.getContext())
                        .load(feedbackParam.getPostPhoto())
                        .into(imageView);
                titleTextView.setText(feedbackParam.getId());
                dateTextView.setText(date);
                timeTextView.setText(time);
                statusTextView.setText(feedbackParam.getPostStatus());
            }
        }

        @Override
        public void onClick(View view) {
            callbacks.onFeedbackSelected(feedback.getId());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackHolder>{
        private List<Feedback> feedbackList;

        public FeedbackAdapter(List<Feedback> feedbacks){
            feedbackList = feedbacks;
        }

        @NonNull
        @Override
        public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FeedbackHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(@NonNull FeedbackHolder holder, int position) {
            Feedback feedback = feedbackList.get(position);
            holder.bind(feedback);
        }

        @Override
        public int getItemCount() {
            return feedbackList.size();
        }
    }
}