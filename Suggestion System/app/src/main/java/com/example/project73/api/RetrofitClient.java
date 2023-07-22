package com.example.project73.api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.project73.helpers.LocalDateTimeConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDateTime;
import java.util.Date;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@RequiresApi(api = Build.VERSION_CODES.O)
public class RetrofitClient {

    private static Retrofit sRetrofit = null;
    public static Retrofit getClient(String url){
        if(sRetrofit == null){
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeConverter())
                    .create();

            sRetrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return sRetrofit;
    }
}
