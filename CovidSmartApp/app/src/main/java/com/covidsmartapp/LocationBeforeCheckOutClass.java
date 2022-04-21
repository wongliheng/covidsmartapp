package com.covidsmartapp;

public class LocationBeforeCheckOutClass {

    private String locationName, checkInDate, checkInTime, userID;
    private boolean checkedOut;
    private Long dateTime;

    // For Firebase
    public LocationBeforeCheckOutClass() {
    }

    public LocationBeforeCheckOutClass(String locationName, String checkInDate, String checkInTime, String userID, boolean checkedOut, Long dateTime) {
        this.locationName = locationName;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.userID = userID;
        this.checkedOut = checkedOut;
        this.dateTime = dateTime;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public boolean isCheckedOut() {
        return checkedOut;
    }

    public void setCheckedOut(boolean checkedOut) {
        this.checkedOut = checkedOut;
    }

    public Long getDateTime() {
        return dateTime;
    }

    public void setDateTime(Long dateTime) {
        this.dateTime = dateTime;
    }
}
