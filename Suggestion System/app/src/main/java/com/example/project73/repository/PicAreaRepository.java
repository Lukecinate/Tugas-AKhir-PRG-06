package com.example.project73.repository;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.lifecycle.MutableLiveData;

import com.example.project73.activity.MainActivity;
import com.example.project73.api.ApiUtils;
import com.example.project73.api.PicAreaService;
import com.example.project73.model.PicArea;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PicAreaRepository {
    private static final String TAG = "PicAreaRepository";

    private static PicAreaRepository INSTANCE;
    private PicAreaService picAreaService;

    public PicAreaRepository(Context context) {
        picAreaService = ApiUtils.getPicAreaService();
    }

    public static void initialize(Context context){
        if (INSTANCE == null){
            INSTANCE = new PicAreaRepository(context);
        }
    }

    public static PicAreaRepository get(){
        return INSTANCE;
    }

    public MutableLiveData<List<PicArea>> getPicAreas(){
        MutableLiveData<List<PicArea>> picAreas = new MutableLiveData<>();

        Call<List<PicArea>> call = picAreaService.getAllPicArea();
        call.enqueue(new Callback<List<PicArea>>() {
            @Override
            public void onResponse(Call<List<PicArea>> call, Response<List<PicArea>> response) {
                if (response.isSuccessful()){
                    picAreas.setValue(response.body());
                    Log.d(TAG, "getFeedbacks.onResponse() " );
                }
            }

            @Override
            public void onFailure(Call<List<PicArea>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        return picAreas;
    }

    public MutableLiveData<PicArea> getPicArea(String areaId){
        MutableLiveData<PicArea> picArea = new MutableLiveData<>();

        Call<PicArea> call = picAreaService.getPicArea(Integer.parseInt(areaId));
        call.enqueue(new Callback<PicArea>() {
            @Override
            public void onResponse(Call<PicArea> call, Response<PicArea> response) {
                if (response.isSuccessful()){
                    picArea.setValue(response.body());
                    Log.d(TAG, "getFeedbacks.onResponse() " );
                }
            }

            @Override
            public void onFailure(Call<PicArea> call, Throwable t) {
                Log.e(TAG, t.getMessage());
            }
        });

        return picArea;
    }

}
