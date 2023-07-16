package com.example.project73.mvvm;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.project73.model.PicArea;
import com.example.project73.repository.PicAreaRepository;

import java.util.List;


public class PicAreaViewModel extends ViewModel {
    private static final String TAG = "PicAreaViewModel";

    private MutableLiveData<PicArea> picAreaMutableLiveData;
    public PicAreaRepository picAreaRepository;

    public PicAreaViewModel(){
        Log.d(TAG, "PicAreaViewModel() constructor onCalled");
        picAreaRepository = PicAreaRepository.get();
    }

    public LiveData<List<PicArea>> getPicAreas(){
        Log.d(TAG, "PicAreaViewModel.getPicAreas() onCalled");
        return picAreaRepository.getPicAreas();
    }

    public MutableLiveData<PicArea> getPicArea(String id){
        Log.d(TAG, "PicAreaViewModel.getPicArea() onCalled");
        picAreaMutableLiveData = picAreaRepository.getPicArea(id);
        return picAreaMutableLiveData;
    }

}
