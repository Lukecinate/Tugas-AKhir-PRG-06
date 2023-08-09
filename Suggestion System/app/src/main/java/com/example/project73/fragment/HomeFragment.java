package com.example.project73.fragment;

import static android.content.Context.MODE_PRIVATE;

import static com.example.project73.api.ApiUtils.API_URL;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;


public class HomeFragment extends Fragment {
    // Constants
    private static final String TAG = "HomeFragment";
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 1;

    // Views and ViewModels
    public FeedbackViewModel mFeedbackViewModel;
    public RecyclerView mFeedbackRecyclerView;
    private HomeFragment.FeedbackAdapter mFeedbackAdapter;
    private List<Feedback> mFeedbacks;
    private ImageButton mBeforeImageButton, mAfterImageButton, mOngoingImageButton,mDownloadButton;
    private EditText mSearchText;
    private TextView mBeforeStatus, mOngoingStatus, mAfterStatus;
    private Long mBefore, mOngoing, mAfter;

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;



    Handler handler = new Handler();
    Runnable searchRunnable;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {

    }

    public boolean isLoggedIn(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        return isLoggedIn;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateUI() {
        Feedback feedback = mFeedbacks.get(0);
        Log.d(TAG, "updateFeedbacks : " + feedback);

        mBeforeStatus.setText(String.valueOf(mBefore));
        mAfterStatus.setText(String.valueOf(mAfter));
        mOngoingStatus.setText(String.valueOf(mOngoing));

        mFeedbackAdapter = new FeedbackAdapter(getActivity().getSupportFragmentManager());
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




        //FeedbackHolder holder = new FeedbackHolder()


    }

    @Override
    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i(TAG, "HomeFragment.onCreateView() called");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        mBeforeStatus = v.findViewById(R.id.home_total_before_label);
        mAfterStatus = v.findViewById(R.id.home_total_after_label);
        mOngoingStatus = v.findViewById(R.id.home_total_ongoing_label);
        mDownloadButton = v.findViewById(R.id.download);
        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = "http://10.8.0.126:8080/reportServices?id=" + String.valueOf(mReport.getId_service());
                String url = API_URL+"reportSS";
                //NameFileDownload="Report ["+mReport.getNama_unit()+" By "+mReport.getCreate_by() +" Tanggal "+convertToIndonesianDateString(mReport.getCreate_date())+"]";
                downloadPDF();
                Toast.makeText(getActivity(), "ImageView clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        mFeedbackRecyclerView = v.findViewById(R.id.home_feedback_recycle_view);
        mFeedbackRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFeedbackRecyclerView.setAdapter(mFeedbackAdapter);

        mBeforeImageButton = v.findViewById(R.id.before_image_button);
        mBeforeImageButton.setOnClickListener(view -> {
            mFeedbackViewModel.getFeedbacksBefore().observe(getViewLifecycleOwner(),
                    feedbacks -> mFeedbackAdapter.updateFeedbacks(feedbacks));
            Log.d(TAG, "beforImageButton.onClicked() called");
        });

        mAfterImageButton = v.findViewById(R.id.home_after_image_button);
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
        mFeedbackViewModel.getFeedbacks().observe(getViewLifecycleOwner(), feedbacks -> {
            mFeedbacks = feedbacks;
            updateUI();
        });
        mFeedbackViewModel.countBeforeStatus().observe(getViewLifecycleOwner(), feedback -> mBefore = feedback);
        mFeedbackViewModel.countOngoingStatus().observe(getViewLifecycleOwner(), feedback -> mOngoing = feedback);
        mFeedbackViewModel.countAfterStatus().observe(getViewLifecycleOwner(), feedback -> mAfter = feedback);
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

                    Date date = sourceDate.parse(dateString);
                    String outputDate = dateFormat.format(date);
                    String outputTime = timeFormat.format(date);

                    if (feedback.getPreStatus() != null && !feedback.getPreStatus().isEmpty()) {
                        Glide.with(itemView.getContext())
                                .load(ApiUtils.API_IMAGE_URL + feedback.getPrePhoto())
                                .into(imageView);
                        titleTextView.setText(feedback.getTitle());
                        dateTextView.setText(outputDate);
                        timeTextView.setText(outputTime);
                        statusTextView.setText(feedback.getPreStatus());
                    }

                    if (feedback.getPostStatus() != null && !feedback.getPostStatus().isEmpty() && feedback.getPostStatus().equalsIgnoreCase("ongoing")) {
                        Glide.with(itemView.getContext())
                                .load(ApiUtils.API_IMAGE_URL + feedback.getPrePhoto())
                                .into(imageView);
                        titleTextView.setText(feedback.getTitle());
                        dateTextView.setText(outputDate);
                        timeTextView.setText(outputTime);
                        statusTextView.setText(feedback.getPostStatus());
                    }

                    if (feedback.getPostStatus() != null && !feedback.getPostStatus().isEmpty() && feedback.getPostStatus().equalsIgnoreCase("after")) {
                        Glide.with(itemView.getContext())
                                .load(ApiUtils.API_IMAGE_URL + feedback.getPostPhoto())
                                .into(imageView);
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
//                if(!isLoggedIn()) {
//                    Toast.makeText(getContext(), "Login is required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Log.d(TAG, "Send an Id number : " + feedbackId);
                Fragment fragment = DetailOngoingFragment.newInstance(feedbackId, picareaId);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();

            }else if(feedback.getPreStatus() != null && feedback.getPostStatus().equalsIgnoreCase("ongoing")){
//                if(!isLoggedIn()) {
//                    Toast.makeText(getContext(), "Login is required!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
                Log.d(TAG, "Send an Id number : " + feedbackId);
                Fragment fragment = AfterSuggestFragment.newInstance(feedbackId, picareaId);
                fm.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
            }else {
                Toast.makeText(getContext(), "Saran telah selesai dikerjakan", Toast.LENGTH_SHORT).show();
            }



        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public class FeedbackAdapter extends RecyclerView.Adapter<HomeFragment.FeedbackHolder> {
        private List<Feedback> feedbackList;
        private Long before, ongoing, after;
        private FragmentManager fm;

        public FeedbackAdapter(FragmentManager fragmentManager) {
            this.before = mBefore;
            this.after = mAfter;
            this.ongoing = mOngoing;
            this.feedbackList = mFeedbacks;
            this.fm = fragmentManager;
        }

        public FeedbackAdapter(List<Feedback> feedbacks, FragmentManager fragmentManager) {
            this.feedbackList = feedbacks;
            this.fm = fragmentManager;
        }

        public void updateFeedbacks(List<Feedback> feedbacks) {
            this.before = mBefore;
            this.after = mAfter;
            this.ongoing = mOngoing;
            this.feedbackList = feedbacks;
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

    private void downloadPDF() {
        String url = API_URL+"reportSS"; // Replace this URL with the actual PDF URL

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Request WRITE_EXTERNAL_STORAGE permission if not granted
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        } else {
            // Permission already granted, proceed with the download
            new DownloadFileTask().execute(url);
        }
    }

    private class DownloadFileTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... urls) {
            String fileUrl = urls[0];
            String fileName = "reportSS.pdf"; // File name for the downloaded PDF

            try {
                URL url = new URL(fileUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                // Get the file length to display download progress (optional)
                int fileLength = connection.getContentLength();

                File outputFile = new File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), fileName);

                InputStream inputStream = connection.getInputStream();
                OutputStream outputStream = new FileOutputStream(outputFile);

                byte[] buffer = new byte[1024];
                int len;
                long total = 0;
                while ((len = inputStream.read(buffer)) > 0) {
                    total += len;
                    publishProgress((int) (total * 100 / fileLength));
                    outputStream.write(buffer, 0, len);
                }

                outputStream.flush();
                outputStream.close();
                inputStream.close();

                return outputFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progress = values[0];
            // Update your progress bar or any UI element showing the download progress
        }

        @Override
        protected void onPostExecute(String filePath) {
            if (filePath != null) {
                // Download completed, show a toast message

                Toast.makeText(requireContext(), "File downloaded: " + filePath, Toast.LENGTH_SHORT).show();
            } else {
                // Download failed, show a toast message
                Toast.makeText(requireContext(), "Failed to download the file", Toast.LENGTH_SHORT).show();
                //Log("PDF: " ,filePath);
            }
        }
    }



    //    private void downloadPDF(String url) {
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder().url(url).build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                // Handle download failure
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(getContext(), "Failed to download PDF", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    // Save the PDF to a local file
//                    File pdfFile = new File(getContext().getExternalFilesDir(null), "ReportSS.pdf");
//                    BufferedSink sink = Okio.buffer(Okio.sink(pdfFile));
//                    sink.writeAll(response.body().source());
//                    sink.close();
//
//                    // Open the PDF using the PDF viewer library
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            openDownloadedPdf(pdfFile);
//                        }
//                    });
//                }
//            }
//        });
//    }
    private void openDownloadedPdf(File file) {
        Uri path;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // Use FileProvider for devices with Android N and above
            Context context = getContext();
            path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
            // Grant temporary read permission to the content URI
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // Handle the exception as per your requirement
                Toast.makeText(getContext(), "Tidak ada aplikasi untuk membuka file PDF", Toast.LENGTH_SHORT).show();
            }
        } else {
            // For devices below Android N, use the traditional approach
            path = Uri.fromFile(file);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(pdfIntent);
            } catch (ActivityNotFoundException e) {
                // Handle the exception as per your requirement
                Toast.makeText(getContext(), "Tidak ada aplikasi untuk membuka file PDF", Toast.LENGTH_SHORT).show();
            }
        }
    }

}