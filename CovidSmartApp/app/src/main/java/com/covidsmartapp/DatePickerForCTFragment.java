package com.covidsmartapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class DatePickerForCTFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    private DatePickerFragmentListener listener;

    public interface DatePickerFragmentListener {
        public void onDateSet(int year, int month, int day);
    }

    public DatePickerFragmentListener getDatePickerListener() {
        return this.listener;
    }

    public void setDatePickerListener(DatePickerFragmentListener listener) {
        this.listener = listener;
    }

    protected void notifyDatePickerListener(int year, int month, int day) {
        if(this.listener != null) {
            this.listener.onDateSet(year, month, day);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return dialog;
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        notifyDatePickerListener(year, month, day);
    }
}
