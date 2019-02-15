package com.example.shaimaaderbaz.orthoclinic.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Surgery implements Serializable {

    private int id;
    private String patientId;
    private String title;
    private String address;
    private String date;
    private String time;
    private transient List<Uri> images;
    private String patientName;


    public List<Uri> getImages() {
        return images;
    }

    public Surgery(String patientId, String name, String title, String address, String date, String time, List<Uri> images) {
        this.patientId = patientId;
        this.title = title;
        this.address = address;
        this.date = date;
        this.time = time;
        this.images = images;
        this.patientName = name;

    }

    public String getPatientId() {
        return patientId;
    }


    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPatientName() {
        return patientName;
    }
}
