package com.example.kamilzemczak.mgrandroid.helper;

import java.util.Date;

public class MySingleton {

    private static MySingleton mySingleton;

    private String currentName;

    private Integer weight;
    private Integer height;
    private Date dateOfBirth;
    private String favourite;

    public static synchronized MySingleton getInstance() {
        if (mySingleton == null)
            mySingleton = new MySingleton();
        return mySingleton;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date date) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String about) {
        this.favourite = favourite;
    }
}