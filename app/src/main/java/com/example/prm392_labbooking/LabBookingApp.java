package com.example.prm392_labbooking;

import android.app.Application;
import com.google.firebase.FirebaseApp;

public class LabBookingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
