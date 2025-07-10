package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.view.View;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.VisibleRegion;
import com.example.prm392_labbooking.services.PreloadManager;
import com.example.prm392_labbooking.utils.PreloadUtils;

@SuppressWarnings("deprecation")
public class MapUtils {
    private static final String TAG = "MapUtils";
    private static final String PREF_MAP_TYPE = "map_type";
    private static GoogleMap currentGoogleMap;
    private static ImageButton currentCompass;
    private static ObjectAnimator compassAnimator;
    private static ImageView offscreenMarker;
    private static LatLng labLocation;
    private static FrameLayout mapContainer;
      public static void setupLabMap(MapView mapView, double lat, double lon, String title) {
        // Check preload status
        PreloadUtils.logPreloadStatus();
        Log.d(TAG, "Preload status: " + PreloadUtils.getReadinessDescription());
        
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d(TAG, "Map ready callback triggered");
                currentGoogleMap = googleMap;
                labLocation = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(labLocation).title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(labLocation, 18.0f));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                
                // Apply saved map type
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
                int mapType = prefs.getInt(PREF_MAP_TYPE, GoogleMap.MAP_TYPE_NORMAL);
                googleMap.setMapType(mapType);                  // Set up camera change listener for compass rotation and offscreen marker
                googleMap.setOnCameraMoveListener(() -> {
                    if (currentCompass != null) {
                        CameraPosition position = googleMap.getCameraPosition();
                        updateCompassRotationSmooth(position.bearing);
                    }
                    updateOffscreenMarker();
                });
                
                googleMap.setOnCameraIdleListener(() -> {
                    updateOffscreenMarker();
                });
                
                Log.d(TAG, "Map setup completed successfully");
            }
        });
    }

    public static void openInGoogleMaps(Context context, double lat, double lon, String label) {
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)", lat, lon, lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void openInGoogleMaps(Context context, String placeName) {
        // Use geo:0,0?q=placeName for best Google Maps search
        String uri = "geo:0,0?q=" + Uri.encode(placeName);
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
    
    public static void openDirections(Context context, String placeName) {
        // Use Google Maps directions with place name
        String uri = "https://www.google.com/maps/dir/?api=1&destination=" + Uri.encode(placeName) + "&travelmode=driving";
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
                    currentRotation, currentRotation + rotationDiff);            compassAnimator.setDuration(500);
            compassAnimator.setInterpolator(new LinearInterpolator());
            compassAnimator.start();
        }
    }
    
    public static void setupOffscreenMarker(ImageView marker, FrameLayout container) {
        offscreenMarker = marker;
        mapContainer = container;
        
        // Set click listener to pan to lab location
        offscreenMarker.setOnClickListener(v -> {
            if (currentGoogleMap != null && labLocation != null) {
                currentGoogleMap.animateCamera(CameraUpdateFactory.newLatLng(labLocation));
            }
        });
    }
    
    private static void updateOffscreenMarker() {
        if (currentGoogleMap == null || offscreenMarker == null || labLocation == null || mapContainer == null) {
            return;
        }
        
        VisibleRegion visibleRegion = currentGoogleMap.getProjection().getVisibleRegion();
        boolean isVisible = visibleRegion.latLngBounds.contains(labLocation);
        
        if (isVisible) {
            offscreenMarker.setVisibility(View.GONE);
        } else {
            offscreenMarker.setVisibility(View.VISIBLE);
            positionOffscreenMarker();
        }
    }
      private static void positionOffscreenMarker() {
        if (currentGoogleMap == null || offscreenMarker == null || labLocation == null || mapContainer == null) {
            return;
        }
        
        Projection projection = currentGoogleMap.getProjection();
        Point screenPoint = projection.toScreenLocation(labLocation);
        
        // Get container dimensions
        int containerWidth = mapContainer.getWidth();
        int containerHeight = mapContainer.getHeight();
        
        // Marker dimensions (bigger now)
        int markerSize = 48; // dp (increased from 32dp)
        DisplayMetrics metrics = offscreenMarker.getContext().getResources().getDisplayMetrics();
        int markerSizePx = (int) (markerSize * metrics.density);
        
        // Enhanced collision avoidance margins
        int leftMargin = (int) (80 * metrics.density); // Space for compass and layers
        int rightMargin = (int) (80 * metrics.density); // Space for right buttons  
        int topMargin = (int) (40 * metrics.density); // Status bar + padding
        int bottomMargin = (int) (40 * metrics.density); // Navigation bar + bottom buttons
        
        // Calculate safe boundaries
        int safeLeft = leftMargin;
        int safeRight = containerWidth - rightMargin - markerSizePx;
        int safeTop = topMargin;
        int safeBottom = containerHeight - bottomMargin - markerSizePx;
        
        // Calculate position with enhanced logic
        int x, y;
        boolean isHorizontallyOut = screenPoint.x < safeLeft || screenPoint.x > safeRight;
        boolean isVerticallyOut = screenPoint.y < safeTop || screenPoint.y > safeBottom;
        
        if (isHorizontallyOut && isVerticallyOut) {
            // Lab is in a corner - place marker in opposite corner
            if (screenPoint.x < safeLeft) {
                x = safeLeft;
            } else {
                x = safeRight;
            }
            
            if (screenPoint.y < safeTop) {
                y = safeTop;
            } else {
                y = safeBottom;
            }
        } else if (isHorizontallyOut) {
            // Lab is horizontally out - place on left or right edge
            if (screenPoint.x < safeLeft) {
                x = safeLeft;
            } else {
                x = safeRight;
            }
            y = Math.max(safeTop, Math.min(screenPoint.y - markerSizePx / 2, safeBottom));
        } else if (isVerticallyOut) {
            // Lab is vertically out - place on top or bottom edge
            x = Math.max(safeLeft, Math.min(screenPoint.x - markerSizePx / 2, safeRight));
            if (screenPoint.y < safeTop) {
                y = safeTop;
            } else {
                y = safeBottom;
            }
        } else {
            // This shouldn't happen if marker is only shown when lab is out of view
            x = safeLeft;
            y = safeTop;
        }
        
        // Calculate angle to point towards lab
        float angle = calculateAngleToLab(x + markerSizePx / 2, y + markerSizePx / 2);
        offscreenMarker.setRotation(angle);
        
        // Position the marker
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) offscreenMarker.getLayoutParams();
        params.leftMargin = x;
        params.topMargin = y;
        offscreenMarker.setLayoutParams(params);
    }    private static float calculateAngleToLab(int markerX, int markerY) {
        if (currentGoogleMap == null || labLocation == null) {
            return 0;
        }
        
        Projection projection = currentGoogleMap.getProjection();
        Point labPoint = projection.toScreenLocation(labLocation);
        
        // Calculate direction from marker to lab
        int deltaX = labPoint.x - markerX;
        int deltaY = labPoint.y - markerY;
        
        // Calculate angle in degrees (atan2 returns radians)
        double angleRad = Math.atan2(deltaY, deltaX);
        float angleDeg = (float) Math.toDegrees(angleRad);
        
        // Convert to rotation for upward-pointing arrow
        // atan2 gives 0° for right, we want 0° for up
        // Add 90° instead of subtracting to point in correct direction
        float rotation = angleDeg + 90;
        
        // Normalize to 0-360 range
        if (rotation < 0) {
            rotation += 360;
        } else if (rotation >= 360) {
            rotation -= 360;
        }
        
        return rotation;
    }
}
