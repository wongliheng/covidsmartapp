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

    public String getCases() {
        return String.valueOf(cases);
    }

    public void setCases(int cases) {
        this.cases = cases;
    }

    public String getDeaths() {
        return String.valueOf(deaths);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public String getActive() {
        return String.valueOf(active);
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getCritical() {
        return String.valueOf(critical);
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
