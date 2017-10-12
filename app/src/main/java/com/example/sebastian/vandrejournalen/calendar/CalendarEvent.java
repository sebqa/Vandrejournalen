package com.example.sebastian.vandrejournalen.calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sebastian on 27-09-2017.
 */

public class CalendarEvent implements Serializable {



    public String event;
    public String date;
    int day;
    int month;
    int year;

    public int getGestationsalder() {
        return gestationsalder;
    }

    public void setGestationsalder(int gestationsalder) {
        this.gestationsalder = gestationsalder;
    }

    public float getVægt() {
        return vægt;
    }

    public void setVægt(float vægt) {
        this.vægt = vægt;
    }

    public String getBlodtryk() {
        return blodtryk;
    }

    public void setBlodtryk(String blodtryk) {
        this.blodtryk = blodtryk;
    }

    public String getUrinASLeuNit() {
        return urinASLeuNit;
    }

    public void setUrinASLeuNit(String urinASLeuNit) {
        this.urinASLeuNit = urinASLeuNit;
    }

    public String getØdem() {
        return Ødem;
    }

    public void setØdem(String ødem) {
        Ødem = ødem;
    }

    public int gestationsalder;
    public float vægt;
    public String blodtryk;
    public String urinASLeuNit;
    public String Ødem;

    public CalendarEvent(String date, String event){
        this.date = date;
        this.event = event;

    }
    public CalendarEvent(int day, int month, int year){
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
