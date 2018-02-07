package com.troubleshooters.diu.phrm.Adapter;

import android.widget.Toast;

/**
 * Created by Arif on 31-01-18.
 */

public class ModelReport {

    private String hospital,id,userName,appointment,comment,received,reportName;

    public ModelReport(String hospital, String id, String userName, String appointment, String comment, String received, String reportName) {
        this.hospital = hospital;
        this.id = id;
        this.userName = userName;
        this.appointment = appointment;
        this.comment = comment;
        this.received = received;
        this.reportName = reportName;

    }

    public String getHospital() {
        return hospital;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getAppointment() {
        return appointment;
    }

    public String getComment() {
        return comment;
    }

    public String getReceived() {
        return received;
    }

    public String getReportName() {
        return reportName;
    }
}
