package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class MapPreloader {
    private static MapView preloadedMapView;
    private static boolean isPreloaded = false;

    public static void preload(Context context) {
        if (isPreloaded) return;
        preloadedMapView = new MapView(context.getApplicationContext());
        preloadedMapView.onCreate(new Bundle());
        preloadedMapView.onResume();
        preloadedMapView.getMapAsync(googleMap -> {
            Log.d("MapPreloader", "MapView preloaded and ready");
            isPreloaded = true;
        });
    }

    public static boolean isMapPreloaded() {
        return isPreloaded;
    }

    public static void destroy() {
        if (preloadedMapView != null) {
            preloadedMapView.onPause();
            preloadedMapView.onDestroy();
            preloadedMapView = null;
            isPreloaded = false;
        }
    }
}
