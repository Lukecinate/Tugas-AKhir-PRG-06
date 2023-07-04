package com.example.project73.api;

public class ApiUtils {

    public static final String API_URL = "http://10.0.2.2:8080/";

    private ApiUtils(){

    }
    public static FeedbackService getFeedbackService(){
        return RetrofitClient.getClient(API_URL).create(FeedbackService.class);
    }
    public static PicAreaService getPicAreaService(){
        return RetrofitClient.getClient(API_URL).create(PicAreaService.class);
    }
}
