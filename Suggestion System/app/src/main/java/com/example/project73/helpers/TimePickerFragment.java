package com.example.project73.helpers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import java.sql.Time;
import java.util.Date;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String RESULT_TIME_KEY = "resultTimeKey";
    private static final String ARG_TIME = "time";
    private TimePicker mTimePicker;

    public static Time getSelectedTime(Bundle result)
    {
        //TIme time = (Time) result.getSerializable(RESULT_TIME_KEY);
        //return time;
        return (Time) result.getSerializable(RESULT_TIME_KEY);
    }

    public static TimePickerFragment newInstance(Date time, String requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        args.putString(ARG_REQUEST_CODE, requestCode);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Initialize the time picker dialog listener
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            Time resultTime = new Time(hourOfDay, minute, 0);
            Bundle result = new Bundle();
            result.putSerializable(RESULT_TIME_KEY, resultTime);

            String resultRequestCode = getArguments().getString(ARG_REQUEST_CODE, "");
            getParentFragmentManager().setFragmentResult(resultRequestCode, result);
        };

        // Set the initial time to the current time
        Calendar calendar = Calendar.getInstance();
        int initialHour = calendar.get(Calendar.HOUR_OF_DAY);
        int initialMinute = calendar.get(Calendar.MINUTE);

        // If a time was passed in as an argument, use that instead
        Date time = (Date) getArguments().getSerializable(ARG_TIME);
        if (time != null) {
            calendar.setTime(time);
            initialHour = calendar.get(Calendar.HOUR_OF_DAY);
            initialMinute = calendar.get(Calendar.MINUTE);
        }

        // Create the time picker dialog
        boolean is24HourView = true;
        return new TimePickerDialog(
                requireContext(),
                timeSetListener,
                initialHour,
                initialMinute,
                is24HourView);
    }

}
