package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;

@SuppressWarnings("deprecation")
public class MapUtils {
    private static final String PREF_MAP_TYPE = "map_type";
    private static GoogleMap currentGoogleMap;
    private static ImageButton currentCompass;
    private static ObjectAnimator compassAnimator;
      public static void setupLabMap(MapView mapView, double lat, double lon, String title) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                currentGoogleMap = googleMap;
                LatLng labLocation = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(labLocation).title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(labLocation, 18.0f));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                
                // Apply saved map type
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
                int mapType = prefs.getInt(PREF_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMapType(mapType);
                  // Set up camera change listener for compass rotation
                googleMap.setOnCameraMoveListener(() -> {
                    if (currentCompass != null) {
                        CameraPosition position = googleMap.getCameraPosition();
                        updateCompassRotationSmooth(position.bearing);
                    }
                });
            }
        });
    }

    public static void openInGoogleMaps(Context context, double lat, double lon, String label) {
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)", lat, lon, lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void recenterMap(MapView mapView, double lat, double lon) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng labLocation = new LatLng(lat, lon);
                googleMap.animateCamera(CameraUpdateFactory.newLatLng(labLocation));
            }
        });
    }    public static void openDirections(Context context, double destLat, double destLon) {
        // Use Google Maps directions from current location to lab
        String uri = String.format("https://www.google.com/maps/dir/?api=1&destination=%f,%f&travelmode=driving", destLat, destLon);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }
    
    public static void changeMapType(MapView mapView, int mapType) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMapType(mapType);
                
                // Save the preference
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
                prefs.edit().putInt(PREF_MAP_TYPE, mapType).apply();
            }
        });
    }
    
    public static String getMapTypeName(int mapType) {
        switch (mapType) {
            case GoogleMap.MAP_TYPE_SATELLITE:
                return "Satellite";
            case GoogleMap.MAP_TYPE_TERRAIN:
                return "Terrain";
            case GoogleMap.MAP_TYPE_HYBRID:
                return "Hybrid";
            case GoogleMap.MAP_TYPE_NORMAL:
            default:                return "Normal";
        }
    }
      public static void setupCompass(ImageButton compassButton) {
        currentCompass = compassButton;
        // Initialize compass rotation based on current map bearing
        if (currentGoogleMap != null) {
            CameraPosition position = currentGoogleMap.getCameraPosition();
            updateCompassRotationSmooth(position.bearing);
        }
    }
    
    public static void resetMapBearing() {
        if (currentGoogleMap != null && currentCompass != null) {
            CameraPosition currentPosition = currentGoogleMap.getCameraPosition();
            CameraPosition newPosition = new CameraPosition.Builder()
                    .target(currentPosition.target)
                    .zoom(currentPosition.zoom)
                    .bearing(0) // Reset to north
                    .tilt(currentPosition.tilt)
                    .build();
            
            // Animate compass back to 0 degrees smoothly
            animateCompassToRotation(0f);
            
            // Animate map camera
            currentGoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newPosition), 500, null);
        }
    }
    
    private static void updateCompassRotationSmooth(float bearing) {
        if (currentCompass != null) {
            // Cancel any existing animation
            if (compassAnimator != null && compassAnimator.isRunning()) {
                compassAnimator.cancel();
            }
            
            // Rotate compass opposite to map bearing to show correct direction
            currentCompass.setRotation(-bearing);
        }
    }
    
    private static void animateCompassToRotation(float targetRotation) {
        if (currentCompass != null) {
            // Cancel any existing animation
            if (compassAnimator != null && compassAnimator.isRunning()) {
                compassAnimator.cancel();
            }
            
            float currentRotation = currentCompass.getRotation();
            
            // Handle rotation direction to take shortest path
            float rotationDiff = targetRotation - currentRotation;
            if (rotationDiff > 180) {
                rotationDiff -= 360;
            } else if (rotationDiff < -180) {
                rotationDiff += 360;
            }
            
            compassAnimator = ObjectAnimator.ofFloat(currentCompass, "rotation", 
                    currentRotation, currentRotation + rotationDiff);
            compassAnimator.setDuration(500);
            compassAnimator.setInterpolator(new LinearInterpolator());
            compassAnimator.start();
        }
    }
}
