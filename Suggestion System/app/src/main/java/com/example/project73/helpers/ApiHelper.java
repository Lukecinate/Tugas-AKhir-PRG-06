package com.example.project73.helpers;

import android.content.Context;

import com.example.project73.api.ApiUtils;
import com.example.project73.api.PicAreaService;

public class ApiHelper {
    private static ApiHelper INSTANCE;
    private final PicAreaService mApiService;

    private ApiHelper(Context context) {
        mApiService = ApiUtils.getPicAreaService();
    }

    public static void initialize(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ApiHelper(context);
        }
    }

    public static ApiHelper get() {
        return INSTANCE;
    }
}
