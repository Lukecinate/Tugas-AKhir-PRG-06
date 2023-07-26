package com.project04.WebSuggestionSystem.service;

import com.project04.WebSuggestionSystem.model.PicArea;
import com.project04.WebSuggestionSystem.repository.PicAreaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PicAreaService {
    @Autowired
    PicAreaRepository picAreaRepository;
    private boolean isSuccess = true;

    public PicArea getPicArea(int id){
        PicArea res = picAreaRepository.getReferenceById(id);
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

        if(res == null){
            isSuccess = false;
            return isSuccess;
        }else {
            return isSuccess;
        }
    }

    public boolean update(int id, PicArea picArea){
        PicArea res = getPicArea(id);

        res.setArea(picArea.getArea());
        res.setPicName(picArea.getPicName());

        if(res == null){
            isSuccess = false;
            return isSuccess;
        }else {
            return isSuccess;
        }
    }

    public boolean delete(int id){
        PicArea res = getPicArea(id);

        if(res == null){
            isSuccess = false;
            return isSuccess;
        }else {
            picAreaRepository.deleteById(id);
            return isSuccess;
        }
    }

}
