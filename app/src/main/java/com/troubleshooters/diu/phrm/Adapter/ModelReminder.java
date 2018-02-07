package com.troubleshooters.diu.phrm.Adapter;

/**
 * Created by Arif on 06-02-18.
 */

public class ModelReminder {


String remindertext,hour,minute,day,year,month,rid;

    public ModelReminder(String remindertext, String hour, String minute, String day, String year, String month, String rid) {
        this.remindertext = remindertext;
        this.hour = hour;
        this.minute = minute;
        this.day = day;
        this.year = year;
        this.month = month;
        this.rid = rid;
    }

    public String getRemindertext() {
        return remindertext;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getDay() {
        return day;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public String getRid() {
        return rid;
    }
}
