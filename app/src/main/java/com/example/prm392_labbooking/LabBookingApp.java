package com.example.prm392_labbooking;

import android.app.Application;
import com.google.firebase.FirebaseApp;
import com.example.prm392_labbooking.services.PreloadManager;

public class LabBookingApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
    
    @Override
    public void onTerminate() {
        super.onTerminate();
        // Cleanup preloading services
        PreloadManager.getInstance().shutdown();
    }
}
