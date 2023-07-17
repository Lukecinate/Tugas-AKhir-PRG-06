package com.example.project73.helpers;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UploadHelper {
    @Multipart
    @POST("your/api/endpoint")
    abstract Call<ResponseBody> addFeedback(@Part("file\"; filename=\"image.jpg\"") RequestBody file,
                                            @Part("id") int id,
                                            @Part("createdDate") String createdDate,
                                            @Part("deadline") String deadline,
                                            @Part("preStatus") String preStatus,
                                            @Part("suggestName") String suggestName,
                                            @Part("suggest") String suggest,
                                            @Part("title") String title);
}
