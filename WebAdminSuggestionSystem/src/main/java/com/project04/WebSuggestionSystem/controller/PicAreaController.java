package com.project04.WebSuggestionSystem.controller;

import com.project04.WebSuggestionSystem.model.PicArea;
import com.project04.WebSuggestionSystem.model.Result;
import com.project04.WebSuggestionSystem.service.PicAreaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
public class PicAreaController {
    @Autowired
    PicAreaService picAreaService;
    boolean succeed;

    @GetMapping("/getAllPicArea")
    public List<PicArea> getAllPicArea(){
        return picAreaService.getAll();
    }

    @GetMapping("/getAllPic")
    public List<PicArea> getAllPic(){
        return picAreaService.getAll();
    }

    @GetMapping("/getPicArea")
    public PicArea getPicArea(@RequestParam("id") int id){
        return picAreaService.getPicArea(id);
    }

    @PostMapping("/savePicArea")
    public Object savePicArea(HttpServletResponse response, @RequestParam(name = "area") String areaParam, @RequestParam(name = "picName") String picNameParam){
        PicArea picArea = new PicArea(0, areaParam, picNameParam);
        succeed = picAreaService.save(picArea);

        if (succeed){
            return new Result(200, "Successfully!");
        }else {
            return new Result(500, "Failure!" + response.toString());
        }
    }

    @PutMapping("/updatePicArea")
    public Object updatePicArea(HttpServletResponse response, @RequestParam("id") int id, @RequestParam(name = "updateArea") String areaParam, @RequestParam(name = "updatePicName") String picNameParam){
        PicArea picArea = picAreaService.getPicArea(id);

        picArea.setArea(areaParam);
        picArea.setPicName(picNameParam);

        succeed = picAreaService.save(picArea);
        if (succeed){
            return new Result(200, "Successfully!");
        }else {
            return new Result(500, "Failure!" + response.toString());
        }
    }

    @DeleteMapping("/deletePicArea")
    public Object deletePicArea(HttpServletResponse response, @RequestParam("id") int id){
        succeed = picAreaService.delete(id);

        if (succeed){
            return new Result(200, "Successfully!");
        }else {
            return new Result(500, "Failure!" + response.toString());
        }
    }

    @GetMapping("/countPicArea")
    public Long countPicAreas(){
        return picAreaService.countPicAreas();
    }
}
