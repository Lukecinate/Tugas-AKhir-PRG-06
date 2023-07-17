package com.example.project73.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String RESULT_DATE_KEY = "resultDateKey";

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static LocalDateTime getSelectedDate(Bundle result) {
        LocalDateTime date = (LocalDateTime) result.getSerializable(RESULT_DATE_KEY);
        return date;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static DatePickerFragment newInstance(LocalDateTime date, String requestCode) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putString(ARG_REQUEST_CODE, requestCode);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        DatePickerDialog.OnDateSetListener dateSetListener;

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                LocalDateTime resultDate = LocalDateTime.of(year, month + 1, dayOfMonth, 0, 0);

                // Create our result Bundle
                Bundle result = new Bundle();
                result.putSerializable(RESULT_DATE_KEY, resultDate);

                String resultRequestCode = getArguments().getString(ARG_REQUEST_CODE, "");
                getParentFragmentManager().setFragmentResult(resultRequestCode, result);
            }
        };

        LocalDateTime date = (LocalDateTime) getArguments().getSerializable(ARG_DATE);
        int initialYear = date.getYear();
        int initialMonth = date.getMonthValue() - 1; // Month value is 0-based for DatePickerDialog
        int initialDay = date.getDayOfMonth();

        return new DatePickerDialog(
                requireContext(),
                dateSetListener,
                initialYear,
                initialMonth,
                initialDay);
    }
}
