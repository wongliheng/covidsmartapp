package com.covidsmartapp;

public class LocationClass {
    private String locationName, checkInDate, checkInTime;
    private boolean checkedOut;

    // For Firebase
    public LocationClass() {
    }

    public LocationClass(String locationName, String checkInDate, String checkInTime, boolean checkedOut) {
        this.locationName = locationName;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.checkedOut = checkedOut;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(String checkInDate) {
        this.checkInDate = checkInDate;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }
}
