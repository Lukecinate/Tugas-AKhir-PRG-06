package com.example.project73.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PicArea {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("area")
    @Expose
    private String area;

    @SerializedName("picName")
    @Expose
    private String picName;

    public PicArea() {
    }

    public PicArea(int id, String area, String picName) {
        this.id = id;
        this.area = area;
        this.picName = picName;
    }

    public PicArea(int i, String s) {
        this.id = i;
        this.area = s;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }
}
