package com.troubleshooters.diu.phrm.Adapter;

import java.lang.ref.SoftReference;

/**
 * Created by Arif on 08-01-18.
 */

public class Model_medicin_details {

    private String medicines;
    private String hour;
    private String minute;
    private String days;
    private String date;
    private String month;
    private String year;
    private String NID;
    private String AMorPM;
    private String type;
    private String numberofalarm;


    public Model_medicin_details(String medicines, String hour, String minute, String days, String date, String month, String year, String NID,String AMorPM,String type,String numberofalarm) {
        this.medicines = medicines;
        this.hour = hour;
        this.minute = minute;
        this.date = date;
        this.days = days;
        this.month = month;
        this.year = year;
        this.NID=NID;
        this.AMorPM=AMorPM;
        this.type=type;
        this.numberofalarm=numberofalarm;
    }

    public String getMedicines() {
        return medicines;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getDays() {
        return days;
    }

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getNID() {
        return NID;
    }

    public void setMedicines(String medicines) {
        this.medicines = medicines;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setNID(String NID) {
        this.NID = NID;
    }


    public String getAMorPM() {
        return AMorPM;
    }

    public void setAMorPM(String AMorPM) {
        this.AMorPM = AMorPM;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberofalarm() {
        return numberofalarm;
    }

    public void setNumberofalarm(String numberofalarm) {
        this.numberofalarm = numberofalarm;
    }
}
