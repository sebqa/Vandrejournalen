package com.example.sebastian.vandrejournalen;

/**
 * Created by Sebastian on 08-12-2017.
 */

public class Patient {
    public String getProfUserID() {
        return profUserID;
    }

    public void setProfUserID(String profUserID) {
        this.profUserID = profUserID;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    String profUserID;
    String cpr;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getPhoneprivate() {
        return phoneprivate;
    }

    public void setPhoneprivate(int phoneprivate) {
        this.phoneprivate = phoneprivate;
    }

    public int getPhonework() {
        return phonework;
    }

    public void setPhonework(int phonework) {
        this.phonework = phonework;
    }

    String userID;
    String name;
    String address;
    String email;
    int phoneprivate;
    int phonework;


    public boolean isCprExists() {
        return cprExists;
    }

    public void setCprExists(boolean cprExists) {
        this.cprExists = cprExists;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    boolean cprExists;
    String response;

    public Patient() {
    }


}
