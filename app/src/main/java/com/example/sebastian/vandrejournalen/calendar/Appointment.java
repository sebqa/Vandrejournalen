package com.example.sebastian.vandrejournalen.calendar;

import java.util.Date;

/**
 * Created by Sebastian on 09-12-2017.
 */

public class Appointment {
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getProfUserID() {
        return profUserID;
    }

    public void setProfUserID(String profUserID) {
        this.profUserID = profUserID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    Date date;
    String profUserID;
    String userID;

    public Appointment() {
    }
}
