package com.example.project73.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import com.example.project73.helpers.DateTimeUtils;
import com.example.project73.helpers.TimePickerFragment;
import com.example.project73.model.Feedback;
import com.example.project73.model.PicArea;
import com.example.project73.mvvm.FeedbackViewModel;
import com.example.project73.mvvm.PicAreaViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS";
    private static final String TAG = "SuggestFragment";
    private static final int REQUEST_PHOTO = 1;
    private static final String REQUEST_DATE = "DialogDate";
    private static final String REQUEST_TIME = "DialogTime";
    private static final String PRE_STATUS = "Before";
    private static final String CURRENT_DATE_AND_TIME = Calendar.getInstance().toString();
    private static final int REQUEST_CODE_IMAGE = 2;

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
    private LocalDateTime mTimeSelected;
    private LocalDateTime mDateSelected;
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
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        String photoFileName = "IMG_" + System.currentTimeMillis() + ".jpg";
        mPhotoFile = new File(storageDir, photoFileName);
//        mPhotoUri = FileProvider.getUriForFile(requireContext(), getActivity().getApplicationContext().getPackageName() + ".fileprovider", mPhotoFile);
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

        mDateButton.setText(LocalDateTime.now().toString());
        mTimeButton.setText(LocalDateTime.now().toString());
        mFeedback.setDeadline(LocalDateTime.now().toString());

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


        String date;
        DateTimeFormatter mDateFormatter = DateTimeFormatter.ofPattern("dd/MMM/yyyy");

        try {
            /*if (mFeedback.getDeadline() == null) {
                mFeedback.setDeadline(LocalDateTime.now().toString());
            }*/
            LocalDate convertToDate = LocalDate.parse(mFeedback.getDeadline(), mDateFormatter);
            date = convertToDate.format(mDateFormatter);
            mDateButton.setText(date);
        } catch (DateTimeParseException ne) {
            Log.d(TAG, "Error parsing date: " + ne.getMessage());
        }

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
                    LocalDateTime deadlineDateTime = LocalDateTime.parse(mFeedback.getDeadline());
                    dialog = DatePickerFragment.newInstance(deadlineDateTime, REQUEST_DATE);
                    dialog.show(manager, REQUEST_DATE);
                } catch (DateTimeParseException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        String time;
        DateTimeFormatter mTimeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

        try {
            /*if (mFeedback.getDeadline() == null) {
                mFeedback.setDeadline(LocalDateTime.now().toString());
            }*/

            LocalTime convertToTime = LocalTime.parse(mFeedback.getDeadline(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            time = convertToTime.format(mTimeFormat);
            mTimeButton.setText(time);
        } catch (DateTimeParseException ne) {
            Log.d(TAG, "Error parsing time: " + ne.getMessage());
        }

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
                                    // Assuming that mTimeSelected is a LocalTime object
                                    mFeedback.setDeadline(String.valueOf(combineDateAndTime()));
                                }
                            }
                        }
                );

                try {
                    LocalDateTime deadlineTime = LocalDateTime.parse(mFeedback.getDeadline(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                    TimePickerFragment dialog = TimePickerFragment.newInstance(deadlineTime, REQUEST_TIME);
                    dialog.show(manager, REQUEST_TIME);
                } catch (DateTimeParseException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

        mName = "Si Dia";
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the value
                mFeedback.setDeadline(combineDateAndTime().toString());
                mFeedback.setTitle(mTitleText.getText().toString());
                mFeedback.setSuggestName(mName);
                mFeedback.setSuggest(mSuggestionText.getText().toString());
                mFeedback.setPreStatus(PRE_STATUS);
                mFeedback.setCreatedDate(CURRENT_DATE_AND_TIME);
                mFeedback.setId(0);

                // Convert the image bitmap to a byte array
                Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                byte[] byteArray = outputStream.toByteArray();

                // Create a RequestBody using the byte array
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), byteArray);
                Log.d(TAG, "URI File name : " + mPhotoFile.toURI());
                // Create the MultipartBody.Part using the RequestBody and the file name
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", mPhotoFile.getName(), requestBody);
                Log.d(TAG, "File name : " + mPhotoFile.getName());
                // Saving the data
                mFeedbackViewModel.addFeedback(filePart, mFeedback.getId(), mFeedback.getCreatedDate(), mFeedback.getDeadline(), mFeedback.getPreStatus(), mFeedback.getSuggestName(), mFeedback.getSuggest(), mFeedback.getTitle());
            }
        });

        /*mSaveButton.setOnClickListener(new View.OnClickListener() {
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
                    mFeedbackViewModel.addFeedback(filePart, mFeedback.getId(), mFeedback.getCreatedDate(), mFeedback.getDeadline(), mFeedback.getPreStatus(), mFeedback.getSuggestName(), mFeedback.getSuggest(), mFeedback.getTitle());
                } catch (Exception e) {
                    Log.e(TAG, "Error while reading or writing file : " + e.getMessage());
                }
            }
        });*/

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

        mCameraButton = view.findViewById(R.id.suggest_camera_button);
        mPhotoView = (ImageView) view.findViewById(R.id.suggest_upload_image_view);

        mCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (captureImage.resolveActivity(requireActivity().getPackageManager()) != null) {
                    captureImage.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                    startActivityForResult(captureImage, REQUEST_PHOTO);
                }
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
        combineDateAndTime();
        mPicAreaAdapter = new ArrayAdapter<>(getActivity(), androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, mGetAreaName);
        mPicAreaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mPicAreaAdapter.notifyDataSetChanged();
        mSpinnerPicArea.setAdapter(mPicAreaAdapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private LocalDateTime combineDateAndTime() {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

            LocalDateTime dateTime = LocalDateTime.parse(mFeedback.getDeadline(), dateTimeFormatter);

            if (mDateSelected != null) {
                dateTime = dateTime.withYear(mDateSelected.getYear())
                        .withMonth(mDateSelected.getMonthValue())
                        .withDayOfMonth(mDateSelected.getDayOfMonth());
            }

            if (mTimeSelected != null) {
                dateTime = dateTime.withHour(mTimeSelected.getHour())
                        .withMinute(mTimeSelected.getMinute());
            }

            mFeedback.setDeadline(dateTime.format(dateTimeFormatter));

            // Set the updated date and time values to the date and time buttons
            mDateButton.setText(DateTimeFormatter.ofPattern("dd - MMM - yyyy").format(dateTime));
            mTimeButton.setText(DateTimeFormatter.ofPattern("HH:mm:ss").format(dateTime));

            // return the value
            return dateTime;
        } catch (DateTimeParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        /*if (requestCode == REQUEST_PHOTO) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mPhotoView.setImageBitmap(photo);
        } else {
            Log.d(TAG, "Error");
            mPhotoView.setImageResource(R.drawable.icon_list_photo);
            super.onActivityResult(requestCode, resultCode, data);
        }*/
        if (requestCode == REQUEST_PHOTO) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            mPhotoView.setImageBitmap(photo);
        } else if (requestCode == REQUEST_CODE_IMAGE && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = getBitmapFromUri(uri);
            if (bitmap != null) {
                mPhotoView.setImageBitmap(bitmap);
            } else {
                // Handle the case when the bitmap is null
                Log.e(TAG, "Error loading image");
            }
        } else {
            Log.d(TAG, "Error");
            mPhotoView.setImageResource(R.drawable.icon_list_photo);
            super.onActivityResult(requestCode, resultCode, data);

        }
    }

    private Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null) {
            // Handle the case when the URI is null
            return null;
        }

        try {
            ContentResolver resolver = requireActivity().getContentResolver();
            InputStream inputStream = resolver.openInputStream(uri);

            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream);
            }
        } catch (FileNotFoundException e) {
            Log.e(TAG, "File not found: " + e.getMessage());
        }

        return null;
    }

}