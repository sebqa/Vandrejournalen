package com.example.sebastian.vandrejournalen.authentication;

/**
 * Created by Sebastian on 05-12-2017.
 */

public class LetID {

    String keyID;
    int keyTag;
    String userID;
    int inKeyCode;

    public int getInKeyCode() {
        return inKeyCode;
    }

    public void setInKeyCode(int inKeyCode) {
        this.inKeyCode = inKeyCode;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }


    public LetID(){

    }

    public String getKeyID() {
        return keyID;
    }

    public void setKeyID(String keyID) {
        this.keyID = keyID;
    }

    public int getKeyTag() {
        return keyTag;
    }

    public void setKeyTag(int keyTag) {
        this.keyTag = keyTag;
    }




}
