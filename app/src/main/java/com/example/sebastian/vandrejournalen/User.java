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
