package com.example.project73.helpers;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String RESULT_TIME_KEY = "resultTimeKey";
    private static final String ARG_TIME = "time";

    public static LocalDateTime getSelectedTime(Bundle result) {
        return (LocalDateTime) result.getSerializable(RESULT_TIME_KEY);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static TimePickerFragment newInstance(LocalDateTime time, String requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);
        args.putString(ARG_REQUEST_CODE, requestCode);
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        // Initialize the time picker dialog listener
        TimePickerDialog.OnTimeSetListener timeSetListener = (view, hourOfDay, minute) -> {
            LocalDateTime resultTime = LocalDateTime.of(1970, 1, 1, hourOfDay, minute);
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
        LocalDateTime time = (LocalDateTime) getArguments().getSerializable(ARG_TIME);
        if (time != null) {
            initialHour = time.getHour();
            initialMinute = time.getMinute();
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
