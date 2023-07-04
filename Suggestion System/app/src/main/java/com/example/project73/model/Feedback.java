package com.example.project73.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

public class Feedback {

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("area_id")
    @Expose
    private PicArea areaId;

    @SerializedName("created_date")
    @Expose
    private LocalDateTime createdDate;

    @SerializedName("deadline")
    @Expose
    private LocalDateTime deadline;

    @SerializedName("post_photo")
    @Expose
    private String postPhoto;

    @SerializedName("pre_photo")
    @Expose
    private String prePhoto;

    @SerializedName("pre_status")
    @Expose
    private String preStatus;

    @SerializedName("post_status")
    @Expose
    private String postStatus;

    @SerializedName("suggest_name")
    @Expose
    private String suggestName;

    @SerializedName("suggest")
    @Expose
    private String suggest;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("worker_name")
    @Expose
    private String workerName;

    @SerializedName("modified_date")
    @Expose
    private Timestamp modifiedDate;

    public Feedback() {
    }

    public Feedback(int id, PicArea areaId, LocalDateTime createdDate, LocalDateTime deadline, String postPhoto, String prePhoto, String preStatus, String postStatus, String suggestName, String suggest, String title, String workerName, Timestamp modifiedDate) {
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

    public PicArea getAreaId() {
        return areaId;
    }

    public void setAreaId(PicArea areaId) {
        this.areaId = areaId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
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

    public Timestamp getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Timestamp modifiedDate) {
        this.modifiedDate = modifiedDate;
    }
}
