package com.example.juanjojg.rememberthepills.models;

import android.graphics.Bitmap;

public class Pill {
    private long pillID;
    private String pillName;
    private String pillDescription;
    private Bitmap pillPhoto;

    public Pill(long id, String name, String description, Bitmap photo) {
        this.pillID = id;
        this.pillName = name;
        this.pillDescription = description;
        this.pillPhoto = photo;
    }

    public Pill() {

    }

    public long getPillID() {
        return pillID;
    }

    public void setPillID(long pillID) {
        this.pillID = pillID;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public String getPillDescription() {
        return pillDescription;
    }

    public void setPillDescription(String pillDescription) {
        this.pillDescription = pillDescription;
    }

    public Bitmap getPillPhoto() {
        return pillPhoto;
    }

    public void setPillPhoto(Bitmap pillPhoto) {
        this.pillPhoto = pillPhoto;
    }

    public String toString() {
        return "Pill ID: " + this.getPillID() + "\n" +
                "Name: " + this.getPillName() + "\n" +
                "Description: " + this.getPillDescription();
    }
}
