package com.example.prm392_labbooking.utils;

import android.util.Log;
import com.example.prm392_labbooking.services.PreloadManager;

public class PreloadUtils {
    private static final String TAG = "PreloadUtils";
    
    public static void logPreloadStatus() {
        PreloadManager manager = PreloadManager.getInstance();
        boolean mapsReady = manager.isMapsReady();
        boolean firebaseReady = manager.isFirebaseReady();
        boolean allReady = manager.isAllReady();
        
        Log.i(TAG, "=== Preload Status ===");
        Log.i(TAG, "Maps Ready: " + mapsReady);
        Log.i(TAG, "Firebase Ready: " + firebaseReady);
        Log.i(TAG, "All Services Ready: " + allReady);
        Log.i(TAG, "====================");
    }
    
    public static boolean isReadyForMaps() {
        return PreloadManager.getInstance().isMapsReady();
    }
    
    public static boolean isReadyForFirebase() {
        return PreloadManager.getInstance().isFirebaseReady();
    }
    
    public static boolean isFullyReady() {
        return PreloadManager.getInstance().isAllReady();
    }
    
    public static String getReadinessDescription() {
        PreloadManager manager = PreloadManager.getInstance();
        StringBuilder sb = new StringBuilder();
        
        if (manager.isMapsReady()) {
            sb.append("Maps ✓ ");
        } else {
            sb.append("Maps ⏳ ");
        }
        
        if (manager.isFirebaseReady()) {
            sb.append("Firebase ✓");
        } else {
            sb.append("Firebase ⏳");
        }
        
        return sb.toString();
    }
}
