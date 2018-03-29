package com.troubleshooters.diu.phrm.Adapter;

import java.io.File;

/**
 * Created by Arif on 26-03-18.
 */

public class ModelReportInfo {

    String Hn,RT,DT;
    File fIle;


    public ModelReportInfo(String Hn, String RT, String DT, File file) {
        this.Hn = Hn;
        this.RT = RT;
        this.DT = DT;
        this.fIle=file;

    }

    public File getfIle() {
        return fIle;
    }

    public String getHn() {
        return Hn;
    }

    public String getRT() {
        return RT;
    }

    public String getDT() {
        return DT;
    }
}
