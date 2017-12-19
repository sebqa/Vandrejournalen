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
    String profRole;

    public String getMidwifeName() {
        return midwifeName;
    }

    public void setMidwifeName(String midwifeName) {
        this.midwifeName = midwifeName;
    }

    public String getSpecialistName() {
        return specialistName;
    }

    public void setSpecialistName(String specialistName) {
        this.specialistName = specialistName;
    }

    String midwifeName;
    String specialistName;

    public String getProfRole() {
        return profRole;
    }

    public void setProfRole(String profRole) {
        this.profRole = profRole;
    }

    public String getProfCPR() {
        return profCPR;
    }

    public void setProfCPR(String profCPR) {
        this.profCPR = profCPR;
    }

    String profCPR;

    public String getPatientJournalID() {
        return patientJournalID;
    }

    public void setPatientJournalID(String patientJournalID) {
        this.patientJournalID = patientJournalID;
    }

    String patientJournalID;

    public String getJournalID() {
        return journalID;
    }

    public void setJournalID(String journalID) {
        this.journalID = journalID;
    }

    String journalID;


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
