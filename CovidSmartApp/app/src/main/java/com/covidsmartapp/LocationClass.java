package com.covidsmartapp;

public class LocationClass {
    private int checkInDay, checkInMonth, checkInYear, checkInHour, checkInMinute;
    private boolean checkedOut;
    private String locationName;

    // Empty constructor for Firebase
    public LocationClass() {
    }

    public LocationClass(int checkInDay, int checkInMonth, int checkInYear, int checkInHour, int checkInMinute, boolean checkedOut, String locationName) {
        this.checkInDay = checkInDay;
        this.checkInMonth = checkInMonth;
        this.checkInYear = checkInYear;
        this.checkInHour = checkInHour;
        this.checkInMinute = checkInMinute;
        this.checkedOut = checkedOut;
        this.locationName = locationName;
    }

    public int getCheckInDay() {
        return checkInDay;
    }

    public void setCheckInDay(int checkInDay) {
        this.checkInDay = checkInDay;
    }

    public int getCheckInMonth() {
        return checkInMonth;
    }

    public void setCheckInMonth(int checkInMonth) {
        this.checkInMonth = checkInMonth;
    }

    public int getCheckInYear() {
        return checkInYear;
    }

    public void setCheckInYear(int checkInYear) {
        this.checkInYear = checkInYear;
    }

    public int getCheckInHour() {
        return checkInHour;
    }

    public void setCheckInHour(int checkInHour) {
        this.checkInHour = checkInHour;
    }

    public int getCheckInMinute() {
        return checkInMinute;
    }

    public void setCheckInMinute(int checkInMinute) {
        this.checkInMinute = checkInMinute;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
