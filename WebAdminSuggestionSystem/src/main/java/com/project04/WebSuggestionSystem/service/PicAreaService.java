package com.project04.WebSuggestionSystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project04.WebSuggestionSystem.model.PicArea;
import com.project04.WebSuggestionSystem.repository.PicAreaRepository;

@Service
public class PicAreaService {
    @Autowired
    PicAreaRepository picAreaRepository;
    private boolean isSuccess = true;

    public PicArea getPicArea(int id){
        PicArea res = picAreaRepository.findById(id).orElse(null);
        return res;
    }

    public List<PicArea> getAll(){
        if(picAreaRepository.findAll().isEmpty()){
            return null;
        }

        return picAreaRepository.findAll();
    }

    public Long countPicAreas(){
        if(picAreaRepository.countData().toString().isEmpty()){
            return null;
        }

        return picAreaRepository.countData();
    }

    public boolean save(PicArea picArea){
        PicArea res =  picAreaRepository.save(picArea);

        if(res == null) isSuccess = false;

        return isSuccess;
    }

    public boolean update(int id, PicArea picArea){
        PicArea res = getPicArea(id);

        if(res == null) isSuccess = false;

        res.setArea(picArea.getArea());
        res.setPicName(picArea.getPicName());

        return isSuccess;
    }

    public boolean delete(int id){
        PicArea res = getPicArea(id);

        if(res == null) isSuccess = false;

        picAreaRepository.deleteById(id);

        return isSuccess;
    }

}
