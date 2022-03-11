package com.covidsmartapp;

import java.util.Date;

public class LocationClass {
    private String location;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    public LocationClass(String location, String dateTime) {
        this.location = location;
        String[] array = dateTime.split("-");
        setDay(Integer.parseInt(array[0]));
        setMonth(Integer.parseInt(array[1]));
        setYear(Integer.parseInt(array[2]));
        setHour(Integer.parseInt(array[3]));
        setMinute(Integer.parseInt(array[4]));
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }
}
