package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapUtils {
    public static void setupLabMap(MapView mapView, double lat, double lon, String title) {
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                LatLng labLocation = new LatLng(lat, lon);
                googleMap.addMarker(new MarkerOptions().position(labLocation).title(title));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(labLocation, 20.0f));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
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
    }

    public static void openDirections(Context context, double destLat, double destLon) {
        // Use Google Maps directions from current location to lab
        String uri = String.format("https://www.google.com/maps/dir/?api=1&destination=%f,%f&travelmode=driving", destLat, destLon);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }
}
