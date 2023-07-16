package com.example.project73.helpers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private static final String ARG_DATE = "date";
    private static final String ARG_REQUEST_CODE = "requestCode";
    private static final String RESULT_DATE_KEY = "resultDateKey";

    public static Date getSelectedDate(Bundle result)
    {
        Date date = (Date) result.getSerializable(RESULT_DATE_KEY);
        return date;
    }

    public static DatePickerFragment newInstance(Date date, String requestCode){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE, date);
        args.putString(ARG_REQUEST_CODE, requestCode);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        DatePickerDialog.OnDateSetListener dateSetListener;

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                Date resultDate = new GregorianCalendar(year, month, dayOfMonth).getTime();

                //create our result Bundle
                Bundle result = new Bundle();
                result.putSerializable(RESULT_DATE_KEY, resultDate);

                String resultRequestCode = getArguments().getString(ARG_REQUEST_CODE, "");
                getParentFragmentManager().setFragmentResult(resultRequestCode, result);
            }
        };

        Date date = (Date) getArguments().getSerializable(ARG_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int initialYear = calendar.get(Calendar.YEAR);
        int initialMonth = calendar.get(Calendar.MONTH);
        int initialDay = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(
                requireContext(),
//                null,
                dateSetListener,
                initialYear,
                initialMonth,
                initialDay);
    }
}
