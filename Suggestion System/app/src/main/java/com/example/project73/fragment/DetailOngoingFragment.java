package com.example.project73.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.project73.R;
import com.example.project73.api.ApiUtils;
import com.example.project73.model.Feedback;
import com.example.project73.model.PicArea;
import com.example.project73.mvvm.FeedbackViewModel;
import com.example.project73.mvvm.PicAreaViewModel;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailOngoingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailOngoingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "DetailOngoingFragment";
    private static final String ARG_FEEDBACK_ID = "id";
    private static final String ARG_PICAREA_ID = "id_picarea";

    private Button backButton, attemptButton;
    private ImageView imageView;
    private TextView areaTextView, picnameTextView, suggestionTextView, nameTextView, titleTextView;
    private FeedbackViewModel mFeedbackViewModel;
    private PicAreaViewModel mPicAreaViewModel;
    private PicArea mPicArea;
    private Feedback mFeedback;
    private int mFeedbackId, mPicAreaId;



    public DetailOngoingFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static DetailOngoingFragment newInstance() {
        return new DetailOngoingFragment();
    }

    public static DetailOngoingFragment newInstance(int feedbackId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_FEEDBACK_ID, feedbackId);
        Log.d(TAG, "Getting Id number from args : " + args.getInt(ARG_FEEDBACK_ID));
        DetailOngoingFragment fragment = new DetailOngoingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static DetailOngoingFragment newInstance(int feedbackId, int picareaId) {
        Bundle args = new Bundle();
        args.putInt(ARG_FEEDBACK_ID, feedbackId);
        args.putInt(ARG_PICAREA_ID, picareaId);
        Log.d(TAG, "Getting Id number from args : " + args.getInt(ARG_FEEDBACK_ID));
        DetailOngoingFragment fragment = new DetailOngoingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public void updateUI() {
        if (mFeedback != null && mPicArea != null) {
            Log.d(TAG, "updateUI(...) onCalled");
            Log.d(TAG, "retrieve Feedbacks : " + mFeedback.getAreaId());
            Log.d(TAG, "retrieve PicArea : " + mPicArea.getArea());

            areaTextView.setText(mPicArea.getArea());
            picnameTextView.setText(mPicArea.getPicName());

            String suggest = "“" + mFeedback.getSuggest() + "”";
            String name = "-" + mFeedback.getSuggestName();
            suggestionTextView.setText(suggest);
            nameTextView.setText(name);
            titleTextView.setText(mFeedback.getTitle());
            Log.d(TAG, "suggestion : " + suggestionTextView.getText().toString()
                    + "\n name : " + nameTextView.getText().toString());
            String imageUrl = ApiUtils.API_IMAGE_URL + mFeedback.getPrePhoto();
            Glide.with(requireContext()).load(imageUrl).into(imageView);
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        mPicAreaViewModel = new ViewModelProvider(this).get(PicAreaViewModel.class);
        if (getArguments() != null) {
            mFeedbackId = getArguments().getInt(ARG_FEEDBACK_ID);
            mPicAreaId = getArguments().getInt(ARG_PICAREA_ID);
        }
    }

    @Override
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail_ongoing, container, false);

        // initialize views
        areaTextView = view.findViewById(R.id.ongoing_area_label);
        picnameTextView = view.findViewById(R.id.ongoing_picname_label);
        suggestionTextView = view.findViewById(R.id.ongoing_suggest_label);
        nameTextView = view.findViewById(R.id.ongoing_name_label);
        titleTextView = view.findViewById(R.id.ongoing_title_label);
        backButton = view.findViewById(R.id.ongoing_back_button);
        attemptButton = view.findViewById(R.id.ongoing_attempt_button);
        imageView = view.findViewById(R.id.ongoing_image_view);

        // event listener function
        backButton.setOnClickListener(v -> onBackToHomeFragment());
        attemptButton.setOnClickListener(v -> doAttempt());
        return view;
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated.mFeedbackViewModel.getFeeback(...) onCalled with ID  : " + String.valueOf(mFeedbackId));
        mFeedbackViewModel.getFeeback(String.valueOf(mFeedbackId)).observe(
                getViewLifecycleOwner(),
                feedback -> {
                    Log.d(TAG, "Get Data : " + feedback);
                    mFeedback = feedback;
                    updateUI();
                }
        );

        Log.d(TAG, "onViewCreated.mPicAreaViewModel.getPicAreaById(...) onCalled with ID  : " + getArguments().getInt(ARG_PICAREA_ID));
        mPicAreaViewModel.getPicArea(String.valueOf(mPicAreaId)).observe(
                getViewLifecycleOwner(),
                picArea -> {
                    Log.d(TAG, "Get Data : " + picArea);
                    mPicArea = picArea;
                    updateUI();
                }
        );


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void doAttempt() {

        new AlertDialog.Builder(getContext())
                .setTitle("Attempt Work")
                .setMessage("Do you really want to attempt work?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        android.R.string.yes,
                        (dialogInterface, i) -> {
                            Log.d(TAG, "Alert dialog! Yes button clicked");
                            performDoAttempt();
                        })
                .setNegativeButton(android.R.string.no, null)
                .show();


    }

    public void performDoAttempt() {
        String deadlineDate = mFeedback.getDeadline();
        String feedbackId = String.valueOf(mFeedbackId);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        try {
            Date dDate = df.parse(deadlineDate);
            Date currentDate = new Date();

            // Compare the deadline date with the current date
            if (currentDate.after(dDate)) {
                Toast.makeText(getContext(),
                                "Tidak bisa mengerjakan, dikarenakan telah melewati tenggat waktu",
                                Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // If the deadline date is in the future, proceed with the attempt
            Log.d(TAG, "Feedback Id successfully parse into string type : " + feedbackId);
            mFeedbackViewModel.doAttempt(feedbackId);
            Toast.makeText(getContext(), "Selamat Mengerjakan!", Toast.LENGTH_SHORT).show();
            onBackToHomeFragment();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    public void onBackToHomeFragment() {
        Fragment fragment = HomeFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}