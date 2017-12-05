package com.example.sebastian.vandrejournalen;

/**
 * Created by Sebastian on 10-11-2017.
 */

public class User {
    String role;
    String name;
    String token;
    String cpr;
    String pass;

    public User(){

    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    String institution;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCpr() {
        return cpr;
    }

    public void setCpr(String cpr) {
        this.cpr = cpr;
    }

    public String getPass() {
        return pass;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    String userID;

    boolean response;

    public User(String role, String cpr, String pass){
        this.role = role;
        this.cpr = cpr;
        this.pass = pass;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
