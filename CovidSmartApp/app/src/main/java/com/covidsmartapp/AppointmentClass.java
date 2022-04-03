package com.covidsmartapp;

import com.google.firebase.Timestamp;

public class AppointmentClass {

    private String appointmentType, location, date, time, status;
    private long dateTime;

    // For firebase
    public AppointmentClass() {
    }

    public AppointmentClass(String appointmentType, String location, String date, String time, String status, long dateTime) {
        this.appointmentType = appointmentType;
        this.location = location;
        this.date = date;
        this.time = time;
        this.status = status;
        this.dateTime = dateTime;
    }

    public String getAppointmentType() {
        return appointmentType;
    }

    public void setAppointmentType(String appointmentType) {
        this.appointmentType = appointmentType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getDateTime() {
        return dateTime;
    }

    public void setDateTime(long dateTime) {
        this.dateTime = dateTime;
    }
}
