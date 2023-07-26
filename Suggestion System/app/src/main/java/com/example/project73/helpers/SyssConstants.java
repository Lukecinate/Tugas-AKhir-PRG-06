package com.example.project73.helpers;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;

public class SyssConstants {

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    @SuppressLint("SimpleDateFormat")
    public static final SimpleDateFormat FULL_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static final String NOTIFICATION_CHANNEL_ID = "Syss Notification";


}
