package com.covidsmartapp;

public class UserClass {

    //Could remove this class
    public String fName, lName, email, password;
    public int phoneNumber;

    public UserClass() {

    }
    public UserClass(String fName, String lName, String email, String password, int phoneNumber) {
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
