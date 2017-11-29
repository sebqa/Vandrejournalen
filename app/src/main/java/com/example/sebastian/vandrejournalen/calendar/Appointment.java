package com.example.sebastian.vandrejournalen.calendar;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Sebastian on 27-09-2017.
 */

public class Appointment implements Serializable {



    public String event;
    public Date date;
    int day;
    int month;
    int year;


    public void setDate(Date date) {
        this.date = date;
    }

    public String getGestationsalder() {
        return gestationsalder;
    }

    public void setGestationsalder(String gestationsalder) {
        this.gestationsalder = gestationsalder;
    }

    public float getVaegt() {
        return vaegt;
    }

    public void setVaegt(float vaegt) {
        this.vaegt = vaegt;
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

    public String getOedem() {
        return oedem;
    }

    public void setOedem(String oedem) {
        this.oedem = oedem;
    }

    public float getSymfyseFundus() {
        return symfyseFundus;
    }

    public void setSymfyseFundus(float symfyseFundus) {
        this.symfyseFundus = symfyseFundus;
    }

    public String getFosterpraes() {
        return fosterpraes;
    }

    public void setFosterpraes(String fosterpraes) {
        this.fosterpraes = fosterpraes;
    }

    public String getFosterskoen() {
        return fosterskoen;
    }

    public void setFosterskoen(String fosterskoen) {
        this.fosterskoen = fosterskoen;
    }

    public String getFosteraktivitet() {
        return fosteraktivitet;
    }

    public void setFosteraktivitet(String fosteraktivitet) {
        this.fosteraktivitet = fosteraktivitet;
    }

    public String getUndersoegelsessted() {
        return undersoegelsessted;
    }

    public void setUndersoegelsessted(String undersoegelsessted) {
        this.undersoegelsessted = undersoegelsessted;
    }

    public String getInitialer() {
        return initialer;
    }

    public void setInitialer(String initialer) {
        this.initialer = initialer;
    }

    public String gestationsalder = "";
    public float vaegt = 0.0f;
    public String blodtryk = "";
    public String urinASLeuNit = "";
    public String oedem = "";
    public float symfyseFundus = 0.0f;
    public String fosterpraes = "";
    public String fosterskoen = "";
    public String fosteraktivitet = "";
    public String undersoegelsessted = "";
    public String initialer = "";

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String fullName;


    public String time;

    public Appointment(Date date, String event){
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

    public Date getDate() {
        return date;
    }

    public void setDate(int day,int month,int year, int hour, int min) {
        this.date = new GregorianCalendar(year, month-1, day, hour, min).getTime();
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
