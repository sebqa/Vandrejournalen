package com.example.sebastian.vandrejournalen.calendar;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Sebastian on 27-09-2017.
 */

public class Appointment implements Serializable {



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


    public String time;

    public Appointment(String date, String event){
        this.date = date;
        this.event = event;

    }
    public Appointment(int day, int month, int year,String time, String event){
        this.day = day;
        this.month = month;
        this.year = year;
        this.event = event;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
