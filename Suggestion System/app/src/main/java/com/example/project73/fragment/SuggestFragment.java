package com.example.project73.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.lifecycle.ViewModelProvider;

import com.example.project73.R;
import com.example.project73.helpers.ApiHelper;
import com.example.project73.helpers.DatePickerFragment;
import com.example.project73.helpers.TimePickerFragment;
import com.example.project73.model.Feedback;
import com.example.project73.model.PicArea;
import com.example.project73.mvvm.FeedbackViewModel;
import com.example.project73.mvvm.PicAreaViewModel;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

@RequiresApi(api = Build.VERSION_CODES.O)
public class SuggestFragment extends Fragment {
    private static final String PHOTO_DIRECTORY = "uploads/";
    //    private static final String ARG_FEEDBACK_ID = "feedback_id";
    private static final String TAG = "SuggestFragment";
    private static final int REQUEST_PHOTO = 1;
    private static final String REQUEST_DATE = "DialogDate";
    private static final String REQUEST_TIME = "DialogTime";
    private static final String PRE_STATUS = "Before";
    private static final String CURRENT_DATE_AND_TIME = Calendar.getInstance().toString();

    private Feedback mFeedback;
    private FeedbackViewModel mFeedbackViewModel;
    private PicAreaViewModel mPicAreaViewModel;
    private ApiHelper mApiHelper;
    private Uri mPhotoUri;
    private File mPhotoFile;
    private ImageButton mCameraButton;
    private ImageView mPhotoView;
    private Spinner mSpinnerPicArea;
    private Button mDateButton, mTimeButton, mSaveButton, mCancelButton;
    private EditText mTitleText, mSuggestionText;
    private Date mTimeSelected;
    private Date mDateSelected;
    private String mName;
    private List<PicArea> mPicArea;
    private int mPicAreaId;
    private ArrayList<String> mGetAreaName = new ArrayList<String>();
    private ArrayList<String> mGetPicName = new ArrayList<String>();
    private ArrayList<Integer> mGetPicAreaId = new ArrayList<Integer>();
    private ArrayAdapter<?> mPicAreaAdapter;

    public static SuggestFragment newInstance(/*int feedbackId*/) {
        /*Bundle args = new Bundle();
        args.putSerializable(ARG_FEEDBACK_ID, feedbackId);
        SuggestFragment fragment = new SuggestFragment();
        fragment.setArguments(args);*/
        return new SuggestFragment();
    }

    public SuggestFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedback = new Feedback();
        mApiHelper = ApiHelper.get();
        mFeedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        mPicAreaViewModel = new ViewModelProvider(this).get(PicAreaViewModel.class);
        if (mPhotoFile != null) {
            mPhotoUri = FileProvider.getUriForFile(getActivity(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", mPhotoFile);
        } else {
            return;
        }

        // Set Path Photo
        File storageDir = new File(getActivity().getFilesDir(), PHOTO_DIRECTORY);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String photoFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        mPhotoFile = new File(storageDir, photoFileName);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_suggest, container, false);

        mCameraButton = v.findViewById(R.id.suggest_camera_button);
        mPhotoView = (ImageView) v.findViewById(R.id.suggest_upload_image_view);
        mDateButton = v.findViewById(R.id.suggest_date_button);
        mTimeButton = v.findViewById(R.id.suggest_time_button);
        mCancelButton = v.findViewById(R.id.suggest_cancel_button);
        mSaveButton = v.findViewById(R.id.suggest_save_button);
        mTitleText = v.findViewById(R.id.suggest_title_text);
        mSuggestionText = v.findViewById(R.id.suggest_suggestion_text);

        // Button event handler click listener
        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (packageManager.resolveActivity(captureImage, PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mCameraButton.setEnabled(false);
        }

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage.putExtra(MediaStore.ACTION_IMAGE_CAPTURE, mPhotoUri);
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager()
                        .queryIntentActivities(captureImage, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activities : cameraActivities) {
                    getActivity().grantUriPermission(activities.activityInfo.packageName,
                            mPhotoUri,
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, REQUEST_PHOTO);
            }
        });

        String dateTime;
        android.icu.text.SimpleDateFormat mSimpleDateFormat;
        mSimpleDateFormat = new android.icu.text.SimpleDateFormat("EEEE, MMM dd, yyyy");
        dateTime = mSimpleDateFormat.format(mFeedback.getDeadline()).toString();
        mDateButton.setText(dateTime);
        mDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getChildFragmentManager();
                manager.setFragmentResultListener(
                        REQUEST_DATE,
                        getViewLifecycleOwner(),
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                if (requestKey.equals(REQUEST_DATE)) {
                                    if (result == null) {
                                        return;
                                    }

                                    mDateSelected = DatePickerFragment.getSelectedDate(result);
                                    mFeedback.setDeadline(String.valueOf(combineDateAndTime()));
                                }
                            }
                        }
                );
                DatePickerFragment dialog = null;
                try {
                    dialog = DatePickerFragment.newInstance(SimpleDateFormat.getDateTimeInstance().parse(mFeedback.getDeadline()), REQUEST_DATE);
                    dialog.show(manager, REQUEST_DATE);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        String time;
        android.icu.text.SimpleDateFormat mTimeFormat;
        mTimeFormat = new android.icu.text.SimpleDateFormat("HH:mm:ss");
        time = mTimeFormat.format(mFeedback.getDeadline()).toString();
        mTimeButton.setText(time);
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager manager = getChildFragmentManager();
                manager.setFragmentResultListener(
                        REQUEST_TIME,
                        getViewLifecycleOwner(),
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                if (requestKey.equals(REQUEST_TIME)) {
                                    if (result == null) {
                                        return;
                                    }
                                    mTimeSelected = TimePickerFragment.getSelectedTime(result);
//                                    mCrime.setDate(mTimeSelected);
                                    mFeedback.setDeadline(String.valueOf(combineDateAndTime()));
                                }
                            }
                        }
                );

                try {
                    TimePickerFragment dialog = TimePickerFragment.newInstance(SimpleDateFormat.getDateTimeInstance().parse(mFeedback.getDeadline()), REQUEST_TIME);
                    dialog.show(manager, REQUEST_TIME);
                }catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        mName = "Si Dia";
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //Set the value
                    mFeedback.setDeadline(combineDateAndTime().toString());
                    mFeedback.setTitle(mTitleText.getText().toString());
                    mFeedback.setSuggestName(mName);
                    mFeedback.setSuggest(mSuggestionText.getText().toString());
                    mFeedback.setPreStatus(PRE_STATUS);
                    mFeedback.setCreatedDate(CURRENT_DATE_AND_TIME);
                    mFeedback.setId(0);

                    //File handling
                    File file = mPhotoFile;
                    RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestBody);

                    //saving the data
                    mFeedbackViewModel.addFeedback(filePart, mFeedback.getId(), mFeedback.getCreatedDate(), mFeedback.getDeadline(), mFeedback.getPreStatus(), mFeedback.getSuggestName(),mFeedback.getSuggest(),mFeedback.getTitle());
                }catch (Exception e) {
                    Log.e(TAG, "Error while reading or writing file : " + e.getMessage());
                }
            }
        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPicAreaViewModel.getPicAreas().observe(getViewLifecycleOwner(), this::updateUI);

        // Set the listener for item selection
        mSpinnerPicArea = view.findViewById(R.id.suggest_dropdown_menu);
        mSpinnerPicArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Retrieve the selected PicArea object
                Log.d(TAG, "Got(i) id : " + mGetPicAreaId.get(position));
                mPicAreaId = mGetPicAreaId.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when nothing is selected
            }
        });
    }

    private void updateUI(List<PicArea> picAreasParam) {
        Log.d(TAG, "Got a Pic Area size : " + picAreasParam.size());
        for (PicArea p : picAreasParam) {
            mGetPicAreaId.add(p.getId());
            mGetAreaName.add(p.getArea());
            mGetPicName.add(p.getPicName());

            Log.d(TAG, "Got PicName = " + p.getPicName());
        }

        mPicAreaAdapter = new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mGetAreaName);
        mPicAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPicAreaAdapter.notifyDataSetChanged();
        mSpinnerPicArea.setAdapter(mPicAreaAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private Date combineDateAndTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(SimpleDateFormat.getDateTimeInstance().parse(mFeedback.getDeadline()));

            if (mDateSelected != null) {
                Calendar dateCalendar = Calendar.getInstance();
                dateCalendar.setTime(mDateSelected);
                calendar.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
            }

            if (mTimeSelected != null) {
                Calendar timeCalendar = Calendar.getInstance();
                timeCalendar.setTime(mTimeSelected);
                calendar.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
                calendar.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
            }

            mFeedback.setDeadline(calendar.getTime().toString());
            return calendar.getTime();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /*private void updateUI(List<PicArea> picAreasParam) {
        mPicArea = picAreasParam;

        // Extract the names of PicArea objects
        mGetAreaName.clear();
        for (PicArea picArea : mPicArea) {
            mGetAreaName.add(picArea.getArea());
        }

        // Create an ArrayAdapter using the mGetAreaName list
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, mGetAreaName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerPicArea.setAdapter(adapter);

        // Set the listener for item selection
        mSpinnerPicArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                // Retrieve the selected PicArea object
                PicArea selectedPicArea = mPicArea.get(position);
                mPicAreaId = selectedPicArea.getId();
                // You can perform any additional operations with the selected PicArea here
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Handle the case when nothing is selected
            }
        });
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_PHOTO) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mPhotoView.setImageBitmap(photo);
        } else {
            Log.d(TAG, "Error");
            mPhotoView.setImageResource(R.drawable.icon_list_photo);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}