package com.example.project73.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project73.R;
import com.example.project73.model.Feedback;
import com.example.project73.mvvm.FeedbackListViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    private static final String TAG = "HomeFragment";

    public FeedbackListViewModel mFeedbackListViewModel;
    public RecyclerView mFeedbackRecyclerView;
    private HomeFragment.FeedbackAdapter mFeedbackAdapter;
    private List<Feedback> mFeedbacks;

    public interface Callbacks {
        public void onFeedbackSelected(int feedbackId);
    }

    public Callbacks callbacks = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUI(List<Feedback> feedbacksParam) {
        Feedback feedback = feedbacksParam.get(0);
        Log.d(TAG, "updateFeedbacks : " + feedback);
        mFeedbacks = feedbacksParam;
        mFeedbackAdapter = new FeedbackAdapter(mFeedbacks);
        mFeedbackRecyclerView.setAdapter(mFeedbackAdapter);
    }

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedbackListViewModel = new ViewModelProvider(this).get(FeedbackListViewModel.class);
    }

    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "HomeFragment.onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mFeedbackRecyclerView = (RecyclerView) v.findViewById(R.id.home_feedback_recycle_view);
        mFeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedbackRecyclerView.setAdapter(mFeedbackAdapter);
        return v;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        Log.i(TAG, "HomeFragment.onViewCreated() called");
        mFeedbackListViewModel.getFeedbacks().observe(getViewLifecycleOwner(), this::updateUI);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView statusTextView;
        Feedback feedback;

        DateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");


        public FeedbackHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.list_item_suggestion, parent, false));

//            imageView = itemView.findViewById(R.id.home_image);
            titleTextView = itemView.findViewById(R.id.home_title_label);
            dateTextView = itemView.findViewById(R.id.home_date_label);
            timeTextView = itemView.findViewById(R.id.home_time_label);
            statusTextView = itemView.findViewById(R.id.home_status_label);
        }

        public void bindPre(Feedback feedbackParam) {
            feedback = feedbackParam;

            try {

                Date date = sourceDate.parse(feedback.getDeadline());
                String outputDate = dateFormat.format(date);
                String outputTime = timeFormat.format(date);
                titleTextView.setText(feedback.getTitle());
                dateTextView.setText(outputDate);
                timeTextView.setText(outputTime);
                statusTextView.setText(feedback.getPreStatus());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        public void bind(Feedback feedbackParam) {
            feedback = feedbackParam;
            /*Glide.with(imageView.getContext())
                        .load(feedbackParam.getPrePhoto())
                        .into(imageView);*/
            try {
                if (feedback.getPostStatus().equals("After")) {
                    Date date = sourceDate.parse(feedback.getDeadline());
                    String outputDate = dateFormat.format(date);
                    String outputTime = timeFormat.format(date);
                    titleTextView.setText(feedback.getTitle());
                    dateTextView.setText(outputDate);
                    timeTextView.setText(outputTime);
                    statusTextView.setText(feedback.getPostStatus());
                } else {
                    Date date = sourceDate.parse(feedback.getDeadline());
                    String outputDate = dateFormat.format(date);
                    String outputTime = timeFormat.format(date);
                    titleTextView.setText(feedback.getTitle());
                    dateTextView.setText(outputDate);
                    timeTextView.setText(outputTime);
                    statusTextView.setText(feedback.getPostStatus());
                }
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackAdapter extends RecyclerView.Adapter<HomeFragment.FeedbackHolder> {
        private List<Feedback> feedbackList;

        public FeedbackAdapter(List<Feedback> feedbacks) {
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
            holder.bindPre(feedback);
            holder.bind(feedback);
        }

        @Override
        public int getItemCount() {
            return feedbackList.size();
        }
    }
}