package com.covidsmartapp;

public class CovidDataModel {

    private int cases;
    private int deaths;
    private int active;
    private int critical;
    private String country;
    private String flag;

    public CovidDataModel() {}

    public CovidDataModel(int cases, int deaths, int active, int critical, String country, String flag) {
        this.cases = cases;
        this.deaths = deaths;
        this.active = active;
        this.critical = critical;
        this.country = country;
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "CovidDataModel{" +
                "cases=" + cases +
                ", deaths=" + deaths +
                ", active=" + active +
                ", critical=" + critical +
                ", country='" + country + '\'' +
                ", flag='" + flag + '\'' +
                '}';
    }

    public int getCases() {
        return cases;
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCritical() {
        return critical;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
