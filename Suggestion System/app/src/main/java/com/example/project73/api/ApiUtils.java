package com.example.project73.api;

import android.os.Build;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ApiUtils {

//    public static final String API_URL = "http://10.8.2.91:8080/";
    public static final String API_URL = "http://10.1.1.236:8080/";
//    public static final String API_URL = "http://192.168.224.2:8081/";
    public static final String API_IMAGE_URL = API_URL + "uploads/";

    private ApiUtils(){

    }

    public static FeedbackService getFeedbackService(){
        return RetrofitClient.getClient(API_URL).create(FeedbackService.class);
    }

    public static PicAreaService getPicAreaService(){
        return RetrofitClient.getClient(API_URL).create(PicAreaService.class);
    }
}
