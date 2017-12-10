package com.example.sebastian.vandrejournalen.calendar;

import java.util.Date;

/**
 * Created by ideap on 12/4/2017.
 */

public class Note {
    Date date;
    String text;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Note(){

    }

}
