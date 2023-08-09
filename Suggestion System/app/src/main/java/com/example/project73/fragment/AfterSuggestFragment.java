package com.example.project73.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;

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

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class AfterSuggestFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = "AfterSuggestFragment";
    private static final String ARG_FEEDBACK_ID = "id";
    private static final String ARG_PICAREA_ID = "id_picarea";
    private static final int REQUEST_CODE_IMAGE = 2;

    private Button saveButton, cancelButton;
    private ImageButton uploadButton;
    private ImageView imageView;
    private TextView titleTextView, areaTextView, suggestionTextView, nameTextView;
    private EditText workerNameText;
    private FeedbackViewModel mFeedbackViewModel;
    private PicAreaViewModel mPicAreaViewModel;
    private PicArea mPicArea;
    private Feedback mFeedback;
    private int mFeedbackId, mPicAreaId;
    private File mPhotoFile;
    private Uri mPhotoUri;

    public AfterSuggestFragment() {
    }

    public static AfterSuggestFragment newInstance() {
        return new AfterSuggestFragment();
    }

    public static AfterSuggestFragment newInstance(int feedbackId, int picareaId) {
        Bundle args = new Bundle();
        args.putInt(ARG_FEEDBACK_ID, feedbackId);
        args.putInt(ARG_PICAREA_ID, picareaId);
        Log.d(TAG, "Getting Id number from args : " + args.getInt(ARG_FEEDBACK_ID));
        AfterSuggestFragment fragment = new AfterSuggestFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private final ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    onImagePickerResult(result);
                }
            }
    );

    public void updateUI() {
        if (mFeedback != null && mPicArea != null) {
            Log.d(TAG, "updateUI(...) onCalled");
            Log.d(TAG, "retrieve Feedbacks : " + mFeedback.getAreaId());
            Log.d(TAG, "retrieve PicArea : " + mPicArea.getArea());

            areaTextView.setText(mPicArea.getArea());

            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserData", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            workerNameText.setText(sharedPreferences.getString("nama", ""));


            String suggest = "“" + mFeedback.getSuggest() + "”";
            String name = "-" + mFeedback.getSuggestName();
            suggestionTextView.setText(suggest);
            nameTextView.setText(name);
            titleTextView.setText(mFeedback.getTitle());
            Log.d(TAG, "suggestion : " + suggestionTextView.getText().toString()
                    + "\n name : " + nameTextView.getText().toString());
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

    @Nullable
    @Override
    @SuppressLint("MissingInflatedId")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggest_after, container, false);

        // initialize views
        areaTextView = view.findViewById(R.id.after_area_label);
        suggestionTextView = view.findViewById(R.id.after_suggest_label);
        nameTextView = view.findViewById(R.id.after_name_label);
        titleTextView = view.findViewById(R.id.after_title_label);
        cancelButton = view.findViewById(R.id.after_cancel_button);
        saveButton = view.findViewById(R.id.after_save_button);
        uploadButton = view.findViewById(R.id.after_camera_button);
        imageView = view.findViewById(R.id.after_upload_image_view);
        workerNameText = view.findViewById(R.id.after_workername_text);
        workerNameText.setEnabled(false);

        // event listener function
        cancelButton.setOnClickListener(v -> onBackToHomeFragment());
        saveButton.setOnClickListener(v -> doFinished());
        uploadButton.setOnClickListener(v -> openImagePicker());
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

    public void doFinished() {
        new AlertDialog.Builder(getContext())
                .setTitle("Attempt Work")
                .setMessage("Do you really want to attempt work?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(
                        android.R.string.yes,
                        (dialogInterface, i) -> {
                            Log.d(TAG, "Alert dialog! Yes button clicked");
                            performDoFinished();
                        })
                .setNegativeButton(android.R.string.no, null)
                .show();

    }

    public void performDoFinished() {
        Log.d(TAG, "Feedback Id successfully called with an Id : " + mFeedbackId);


        mFeedback.setWorkerName(workerNameText.getText().toString());

        // Convert the image bitmap to a byte array
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
        /*byte[] byteArray = outputStream.toByteArray();*/

        // Create a RequestBody using the byte array
        if (mPhotoFile != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpeg"), mPhotoFile);
            Log.d(TAG, "URI File name : " + mPhotoFile.toURI());

            // Create the MultipartBody.Part using the RequestBody and the file name
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", mPhotoFile.getName(), requestBody);
            Log.d(TAG, "File name : " + mPhotoFile.getName());

            // Saving the data
            mFeedbackViewModel.doFinish(filePart, mFeedbackId, mFeedback.getWorkerName());

            Toast.makeText(requireContext(), "Berhasil!", Toast.LENGTH_LONG).show();
            onBackToHomeFragment();
        } else {
            // Handle the case when the image file is null
            Log.e(TAG, "Error: Image file is null");
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void onImagePickerResult(ActivityResult result) {
        if (result.getResultCode() != Activity.RESULT_OK || result.getData() == null) {
            return;
        }

        Intent data = result.getData();
        Uri uri = data.getData();
        Bitmap bitmap = getBitmapFromUri(uri);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            // Save the URI for later use
            mPhotoUri = uri;
            mPhotoFile = getFileFromUri(uri);
        } else {
            // Handle the case when the bitmap is null
            Log.e(TAG, "Error loading image");
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

    public void onBackToHomeFragment() {
        Fragment fragment = HomeFragment.newInstance();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}
