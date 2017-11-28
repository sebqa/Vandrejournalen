package com.example.sebastian.vandrejournalen;

/**
 * Created by Sebastian on 10-11-2017.
 */

public class User {
    String role;
    String name;
    String token;


    boolean response;

    public User(String role, String name, String token){
        this.role = role;
        this.name = name;
        this.token = token;
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

}
