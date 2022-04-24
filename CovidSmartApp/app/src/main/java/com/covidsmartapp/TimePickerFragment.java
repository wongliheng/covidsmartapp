package com.covidsmartapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private TimePickerFragmentListener listener;

    public interface TimePickerFragmentListener {
        public void onTimeSet(int hour, int minute);
    }

    public TimePickerFragment.TimePickerFragmentListener getDatePickerListener() {
        return this.listener;
    }

    public void setTimePickerListener(TimePickerFragment.TimePickerFragmentListener listener) {
        this.listener = listener;
    }

    protected void notifyTimePickerListener(int hour, int minute) {
        if(this.listener != null) {
            this.listener.onTimeSet(hour, minute);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        TimePickerDialog dialog = new TimePickerDialog(getActivity(), 2, this, 0, 0, true);

        return dialog;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        notifyTimePickerListener(hour, minute);
    }
}

