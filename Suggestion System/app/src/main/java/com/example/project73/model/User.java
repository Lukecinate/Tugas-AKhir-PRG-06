package com.example.project73.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("result")
    @Expose
    private String result;
    @SerializedName("NPK")
    @Expose
    private String npk;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("nama")
    @Expose
    private String nama;

    public User(String result, String npk, String username, String nama) {
        this.result = result;
        this.npk = npk;
        this.username = username;
        this.nama = nama;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getNpk() {
        return npk;
    }

    public void setNpk(String npk) {
        this.npk = npk;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }
}
