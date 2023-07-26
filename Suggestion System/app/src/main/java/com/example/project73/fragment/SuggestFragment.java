package com.example.project73.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.project73.R;
import com.example.project73.model.Feedback;
import com.example.project73.model.PicArea;
import com.example.project73.mvvm.FeedbackViewModel;
import com.example.project73.mvvm.PicAreaViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
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

    // Constants
    private static final String TAG = "SuggestFragment";
    private static final String PRE_STATUS = "Before";
    private static final int REQUEST_CODE_IMAGE = 2;

    // Views and ViewModels
    private FeedbackViewModel mFeedbackViewModel;
    private PicAreaViewModel mPicAreaViewModel;
    private ArrayAdapter<String> mPicAreaAdapter;
    private EditText mTitleText, mSuggestionText;
    private Spinner mSpinnerPicArea;
    private ImageView mPhotoView;
    private Button mDateButton, mTimeButton, mSaveButton, mCancelButton;
    private LocalDateTime mTimeSelected, mDateSelected;
    private ImageButton mCameraButton;
    private String mName;
    private Uri mPhotoUri;
    private File mPhotoFile;

    // Feedback data
    private Feedback mFeedback;
    private int mPicAreaId;
    private ArrayList<String> mGetAreaName = new ArrayList<>();
    private ArrayList<Integer> mGetPicAreaId = new ArrayList<>();

    public static SuggestFragment newInstance() {
        return new SuggestFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_suggest, container, false);


        // Initialize views
        mCameraButton = v.findViewById(R.id.suggest_camera_button);
        mPhotoView = v.findViewById(R.id.suggest_upload_image_view);
        mDateButton = v.findViewById(R.id.suggest_date_button);
        mTimeButton = v.findViewById(R.id.suggest_time_button);
        mCancelButton = v.findViewById(R.id.suggest_cancel_button);
        mSaveButton = v.findViewById(R.id.suggest_save_button);
        mTitleText = v.findViewById(R.id.suggest_title_text);
        mSuggestionText = v.findViewById(R.id.suggest_suggestion_text);
        mSpinnerPicArea = v.findViewById(R.id.suggest_dropdown_menu);

        // Set initial date and time
        mFeedback = new Feedback();
        LocalDateTime currentDateTime = LocalDateTime.now();
        mTimeSelected = currentDateTime; // Initialize mTimeSelected with current date and time
        mDateSelected = currentDateTime; // Initialize mDateSelected with current date and time

        mFeedback.setDeadline(currentDateTime.toString());
        mDateButton.setText(currentDateTime.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));
        mTimeButton.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));

        // Feedback ViewModel
        mFeedbackViewModel = new ViewModelProvider(this).get(FeedbackViewModel.class);
        mPicAreaViewModel = new ViewModelProvider(this).get(PicAreaViewModel.class);

        // Observe PicArea data
        mPicAreaViewModel.getPicAreas().observe(getViewLifecycleOwner(), this::updatePicAreas);

        // Set listeners
        mCancelButton.setOnClickListener(view -> onBackToHome());

        mDateButton.setOnClickListener(view -> showDatePicker());

        mTimeButton.setOnClickListener(view -> showTimePicker());

        mSaveButton.setOnClickListener(view -> saveFeedback());

        mCameraButton.setOnClickListener(view -> openImagePicker());

        mSpinnerPicArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected PicArea ID from the list
                if (position >= 0 && position < mGetPicAreaId.size()) {
                    mPicAreaId = mGetPicAreaId.get(position);
                } else {
                    mPicAreaId = 0;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle nothing selected (optional)
            }
        });

        return v;
    }

    private void onBackToHome() {
        Fragment fragment = HomeFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void updatePicAreas(List<PicArea> picAreas) {
        mGetAreaName.clear();
        mGetPicAreaId.clear();

        for (PicArea picArea : picAreas) {
            mGetAreaName.add(picArea.getArea());
            mGetPicAreaId.add(picArea.getId());
        }

        if (mPicAreaAdapter == null) {
            mPicAreaAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, mGetAreaName);
            mSpinnerPicArea.setAdapter(mPicAreaAdapter);
        } else {
            mPicAreaAdapter.notifyDataSetChanged();
        }
    }

    private void showDatePicker() {
        // Create a DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    // Handle the selected date here
                    // Create a LocalDateTime object with the selected date
                    mDateSelected = LocalDateTime.of(year, monthOfYear + 1, dayOfMonth, 0, 0);
                    // Update the date button text
                    mDateButton.setText(mDateSelected.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
                },
                // Get the current date as the default selected date
                LocalDateTime.now().getYear(),
                LocalDateTime.now().getMonthValue() - 1,
                LocalDateTime.now().getDayOfMonth()
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    private void showTimePicker() {
        // Create a TimePickerDialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                (view, hourOfDay, minute) -> {
                    // Handle the selected time here
                    // Create a LocalDateTime object with the selected time
                    mTimeSelected = LocalDateTime.of(mTimeSelected.getYear(), mTimeSelected.getMonthValue(), mTimeSelected.getDayOfMonth(), hourOfDay, minute);
                    // Update the time button text
                    mTimeButton.setText(mTimeSelected.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                },
                // Get the current time as the default selected time
                LocalDateTime.now().getHour(),
                LocalDateTime.now().getMinute(),
                true // Set this to true if you want 24-hour format, otherwise false for 12-hour format
        );

        // Show the TimePickerDialog
        timePickerDialog.show();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String combineDateAndTime() {
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

            LocalDateTime dateTime = LocalDateTime.parse(mDateButton.getText() + " " + mTimeButton.getText(), DateTimeFormatter.ofPattern("dd - MMM - yyyy HH:mm:ss"));

            // Convert LocalDateTime to database-friendly format (ISO 8601)
            DateTimeFormatter dbFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            String databaseDateTime = dateTime.format(dbFormatter);

            return databaseDateTime;
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Error parsing date and time: " + e.getMessage());
            return null;
        }
    }

    private void saveFeedback() {
        // Set the value
        mName = "Si Dia";
        mFeedback.setAreaId(mPicAreaId);
        mFeedback.setTitle(mTitleText.getText().toString());
        mFeedback.setSuggestName(mName);
        mFeedback.setSuggest(mSuggestionText.getText().toString());
        mFeedback.setPreStatus(PRE_STATUS);
        Date currentDate = new Date();

        // Define a format for the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        // Format the current date and time using the format
        String created_date = dateFormat.format(currentDate);
        mFeedback.setCreatedDate(created_date);
        mFeedback.setId(0);
        mFeedback.setDeadline(mDateButton.getText().toString() + " " + mTimeButton.getText().toString());

        // Convert the image bitmap to a byte array
        Bitmap bitmap = ((BitmapDrawable) mPhotoView.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        // Create a RequestBody using the byte array
        if (mPhotoFile != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), mPhotoFile);
            Log.d(TAG, "URI File name : " + mPhotoFile.toURI());

            // Create the MultipartBody.Part using the RequestBody and the file name
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", mPhotoFile.getName(), requestBody);
            Log.d(TAG, "File name : " + mPhotoFile.getName());

            // Saving the data
            mFeedbackViewModel.addFeedback(filePart, mFeedback.getAreaId(), mFeedback.getCreatedDate().toString(), mFeedback.getDeadline(), mFeedback.getPreStatus(), mFeedback.getSuggestName(), mFeedback.getSuggest(), mFeedback.getTitle());

            Toast.makeText(requireContext(), "Berhasil!", Toast.LENGTH_LONG).show();
            onBackToHome();
        } else {
            // Handle the case when the image file is null
            Log.e(TAG, "Error: Image file is null");
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_IMAGE && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap bitmap = getBitmapFromUri(uri);
            if (bitmap != null) {
                mPhotoView.setImageBitmap(bitmap);
                // Save the URI for later use
                mPhotoUri = uri;
                mPhotoFile = getFileFromUri(uri);
            } else {
                // Handle the case when the bitmap is null
                Log.e(TAG, "Error loading image");
            }
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

    private File getFileFromUri(Uri uri) {
        if (uri == null) {
            // Handle the case when the URI is null
            return null;
        }

        String extension = "";
        String mimeType = getActivity().getContentResolver().getType(uri);
        if (mimeType != null) {
            extension = MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType);
        }

        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = getActivity().getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                file = new File(getContext().getCacheDir(), "temp_file." + extension);
                outputStream = new FileOutputStream(file);
                byte[] buffer = new byte[5242880];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }
}
