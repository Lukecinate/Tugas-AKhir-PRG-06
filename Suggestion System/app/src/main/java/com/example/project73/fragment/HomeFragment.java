package com.example.project73.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.project73.R;
import com.example.project73.api.ApiUtils;
import com.example.project73.model.Feedback;
import com.example.project73.mvvm.FeedbackViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomeFragment extends Fragment {
    // Constants
    private static final String TAG = "HomeFragment";
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;

    // Views and ViewModels
    public FeedbackViewModel mFeedbackViewModel;
    public RecyclerView mFeedbackRecyclerView;
    private HomeFragment.FeedbackAdapter mFeedbackAdapter;
    private List<Feedback> mFeedbacks;
    private ImageButton mBeforeImageButton, mAfterImageButton, mOngoingImageButton;
    private EditText mSearchText;
    Handler handler = new Handler();
    Runnable searchRunnable;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUI(List<Feedback> feedbacksParam) {
        Feedback feedback = feedbacksParam.get(0);
        Log.d(TAG, "updateFeedbacks : " + feedback);
        mFeedbacks = feedbacksParam;
        mFeedbackAdapter = new FeedbackAdapter(mFeedbacks, getActivity().getSupportFragmentManager());
        mFeedbackRecyclerView.setAdapter(mFeedbackAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, you can proceed with loading images using Glide
            } else {
                // Permission denied, handle this case appropriately
                // You might want to display a message or provide an alternative approach
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
            }
        }
    }

    @Override
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "HomeFragment.onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mFeedbackRecyclerView = v.findViewById(R.id.home_feedback_recycle_view);
        mFeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedbackRecyclerView.setAdapter(mFeedbackAdapter);

        mBeforeImageButton = v.findViewById(R.id.before_image_button);
        mBeforeImageButton.setOnClickListener(view -> {
            mFeedbackViewModel.getFeedbacksBefore().observe(getViewLifecycleOwner(),
                    feedbacks -> mFeedbackAdapter.updateFeedbacks(feedbacks));
            Log.d(TAG, "beforImageButton.onClicked() called");
        });

        mAfterImageButton = v.findViewById(R.id.after_image_button);
        mAfterImageButton.setOnClickListener(view -> {
            mFeedbackViewModel.getFeedbacksAfter().observe(getViewLifecycleOwner(),
                    feedbacks -> mFeedbackAdapter.updateFeedbacks(feedbacks));
            Log.d(TAG, "afterImageButton.onClicked() called");
        });

        mOngoingImageButton = v.findViewById(R.id.ongoing_image_button);
        mOngoingImageButton.setOnClickListener(view -> {
            mFeedbackViewModel.getFeedbacksOngoing().observe(getViewLifecycleOwner(),
                    feedbacks -> mFeedbackAdapter.updateFeedbacks(feedbacks));
            Log.d(TAG, "ongoingImageButton.onClicked() called");
        });

        mSearchText = v.findViewById(R.id.home_search_text);
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> {
                  String keywords = charSequence.toString().trim();
                  mFeedbackViewModel.getFeebacksByKeywords(keywords).observe(getViewLifecycleOwner(),
                          feedbacks -> mFeedbackAdapter.updateFeedbacks(feedbacks));
                };

                handler.postDelayed(searchRunnable, 1200);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return v;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            Thread.sleep(100);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
        }
        Log.i(TAG, "HomeFragment.onViewCreated() called");
        mFeedbackViewModel.getFeedbacks().observe(getViewLifecycleOwner(), this::updateUI);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView imageView;
        private TextView titleTextView;
        private TextView dateTextView;
        private TextView timeTextView;
        private TextView statusTextView;
        private FragmentManager fm;
        Feedback feedback;

        public FeedbackHolder(LayoutInflater inflater, ViewGroup parent, FragmentManager fragmentManager) {
            super(inflater.inflate(R.layout.list_item_suggestion, parent, false));

            imageView = itemView.findViewById(R.id.home_image_view);
            titleTextView = itemView.findViewById(R.id.home_title_label);
            dateTextView = itemView.findViewById(R.id.home_date_label);
            timeTextView = itemView.findViewById(R.id.home_time_label);
            statusTextView = itemView.findViewById(R.id.home_status_label);

            this.fm = fragmentManager;
            itemView.setOnClickListener(this::onClick);
        }

        public void bind(Feedback feedbackParam) {
            feedback = feedbackParam;

            DateFormat sourceDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

            String dateString = feedback.getDeadline();
            if (dateString != null) {
                try {

                    Glide.with(itemView.getContext()).load(ApiUtils.API_IMAGE_URL + feedback.getPrePhoto()).into(imageView);

                    Date date = sourceDate.parse(dateString);
                    String outputDate = dateFormat.format(date);
                    String outputTime = timeFormat.format(date);

                    if (feedback.getPreStatus() != null && !feedback.getPreStatus().isEmpty()) {
                        titleTextView.setText(feedback.getTitle());
                        dateTextView.setText(outputDate);
                        timeTextView.setText(outputTime);
                        statusTextView.setText(feedback.getPreStatus());
                    }

                    if (feedback.getPostStatus() != null && !feedback.getPostStatus().isEmpty()) {
                        titleTextView.setText(feedback.getTitle());
                        dateTextView.setText(outputDate);
                        timeTextView.setText(outputTime);
                        statusTextView.setText(feedback.getPostStatus());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                // Tindakan untuk penanganan jika dateString bernilai null
            }
        }

        @Override
        public void onClick(View view) {
            int feedbackId = feedback.getId();
            int picareaId = feedback.getAreaId();

            if(feedback.getPreStatus() != null && feedback.getPostStatus() == null){
                Log.d(TAG, "Send an Id number : " + feedbackId);
                Fragment fragment = DetailOngoingFragment.newInstance(feedbackId, picareaId);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }else if(feedback.getPreStatus() != null && feedback.getPostStatus().equalsIgnoreCase("ongoing")){
                Toast.makeText(getContext(), "Sorry still maintenance", Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(getContext(), "Sorry still maintenance", Toast.LENGTH_LONG).show();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackAdapter extends RecyclerView.Adapter<HomeFragment.FeedbackHolder> {
        private List<Feedback> feedbackList;
        private FragmentManager fm;

        public FeedbackAdapter(List<Feedback> feedbacks, FragmentManager fragmentManager) {
            feedbackList = feedbacks;
            this.fm = fragmentManager;
        }

        public void updateFeedbacks(List<Feedback> feedbacks) {
            feedbackList = feedbacks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public FeedbackHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new FeedbackHolder(layoutInflater, parent, fm);
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