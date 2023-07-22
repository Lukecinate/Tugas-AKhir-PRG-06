package com.example.project73.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Feedback {
    private static final String DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm:ss";

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("area_id")
    @Expose
    private int areaId;

    @SerializedName("createdDate")
    @Expose
    private String createdDate;

    @SerializedName("deadline")
    @Expose
    private String deadline;

    @SerializedName("postPhoto")
    @Expose
    private String postPhoto;

    @SerializedName("prePhoto")
    @Expose
    private String prePhoto;

    @SerializedName("preStatus")
    @Expose
    private String preStatus;

    @SerializedName("postStatus")
    @Expose
    private String postStatus;

    @SerializedName("suggestName")
    @Expose
    private String suggestName;

    @SerializedName("suggestion")
    @Expose
    private String suggest;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("workerName")
    @Expose
    private String workerName;

    @SerializedName("modifiedDate")
    @Expose
    private String modifiedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Feedback() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);
        this.createdDate = currentDateTime.format(formatter);
        this.deadline = currentDateTime.format(formatter);
    }

    public Feedback(int id, int areaId, String createdDate, String deadline, String postPhoto, String prePhoto, String preStatus, String postStatus, String suggestName, String suggest, String title, String workerName, String modifiedDate) {
        this.id = id;
        this.areaId = areaId;
        this.createdDate = createdDate;
        this.deadline = deadline;
        this.postPhoto = postPhoto;
        this.prePhoto = prePhoto;
        this.preStatus = preStatus;
        this.postStatus = postStatus;
        this.suggestName = suggestName;
        this.suggest = suggest;
        this.title = title;
        this.workerName = workerName;
        this.modifiedDate = modifiedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPostPhoto() {
        return postPhoto;
    }

    public void setPostPhoto(String postPhoto) {
        this.postPhoto = postPhoto;
    }

    public String getPrePhoto() {
        return prePhoto;
    }

    public void setPrePhoto(String prePhoto) {
        this.prePhoto = prePhoto;
    }

    public String getPreStatus() {
        return preStatus;
    }

    public void setPreStatus(String preStatus) {
        this.preStatus = preStatus;
    }

    public String getPostStatus() {
        return postStatus;
    }

    public void setPostStatus(String postStatus) {
        this.postStatus = postStatus;
    }

    public String getSuggestName() {
        return suggestName;
    }

    public void setSuggestName(String suggestName) {
        this.suggestName = suggestName;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
