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

    public String getJournalID() {
        return journalID;
    }

    public void setJournalID(String journalID) {
        this.journalID = journalID;
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
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(String appointmentID) {
        this.appointmentID = appointmentID;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getPhonework() {
        return phonework;
    }

    public void setPhonework(int phonework) {
        this.phonework = phonework;
    }

    public int getPhoneprivate() {
        return phoneprivate;
    }

    public void setPhoneprivate(int phoneprivate) {
        this.phoneprivate = phoneprivate;
    }

    Date date;
    String profUserID;
    String userID;
    String journalID;
    String appointmentID;
    String name;
    int day;
    int month;
    int year;
    String address;
    String email;
    int phonework;
    int phoneprivate;

    public Appointment() {
    }
}
