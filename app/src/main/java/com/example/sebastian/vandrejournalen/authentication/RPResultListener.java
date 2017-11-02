package com.example.sebastian.vandrejournalen.authentication;

/**
 * Created by Sebastian on 01-11-2017.
 */

interface RPResultListener {
    void onPermissionGranted();

    void onPermissionDenied();
}
