package com.example.sebastian.vandrejournalen;

/**
 * Created by Sebastian on 10-11-2017.
 */

public class User {
    String role;
    String name;
    String token;
    String cpr;
    String password;
    String address;
    String email;
    int phoneprivate;
    int phonework;
    String institution;
    String userID;
    boolean response;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }public User(){

    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }



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

    public String getPassword() {
        return password;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public User(String role, String cpr, String password){
        this.role = role;
        this.cpr = cpr;
        this.password = password;
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

    public void setPassword(String password) {
        this.password = password;
    }
}
