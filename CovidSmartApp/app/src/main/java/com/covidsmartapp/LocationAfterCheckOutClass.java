package com.covidsmartapp;

public class LocationAfterCheckOutClass {

    private String locationName, checkInDate, checkInTime, userID, checkOutDate, checkOutTime;
    private boolean checkedOut;
    private Long dateTime;

    // For firebase
    public LocationAfterCheckOutClass() {
    }

    public LocationAfterCheckOutClass(String locationName, String checkInDate, String checkInTime, String userID,
                                      String checkOutDate, String checkOutTime, boolean checkedOut, Long dateTime) {
        this.locationName = locationName;
        this.checkInDate = checkInDate;
        this.checkInTime = checkInTime;
        this.userID = userID;
        this.checkOutDate = checkOutDate;
        this.checkOutTime = checkOutTime;
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

    public String getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(String checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
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
