package com.example.project73.api;

public class ApiUtils {

    public static final String API_URL = "http://192.168.224.2:8081/";
//    public static final String API_URL = "http://10.1.1.236:8081/";
//    public static final String API_URL = "http://192.168.224.2:8081/";

    private ApiUtils(){

    }
    public static FeedbackService getFeedbackService(){
        return RetrofitClient.getClient(API_URL).create(FeedbackService.class);
    }
    public static PicAreaService getPicAreaService(){
        return RetrofitClient.getClient(API_URL).create(PicAreaService.class);
    }
}
