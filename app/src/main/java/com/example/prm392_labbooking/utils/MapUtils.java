package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class MapUtils {
    public static void setupLabMap(MapView mapView, double lat, double lon, String title) {
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(20.0);
        mapView.getController().setCenter(new GeoPoint(lat, lon));
        Marker marker = new Marker(mapView);
        marker.setPosition(new GeoPoint(lat, lon));
        marker.setTitle(title);
        mapView.getOverlays().add(marker);
    }

    public static void openInGoogleMaps(Context context, double lat, double lon, String label) {
        String uri = String.format("geo:%f,%f?q=%f,%f(%s)", lat, lon, lat, lon, label);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        context.startActivity(intent);
    }

    public static void recenterMap(MapView mapView, double lat, double lon) {
        if (mapView != null) {
            // Animate smoothly to the new center
            mapView.getController().animateTo(new org.osmdroid.util.GeoPoint(lat, lon));
        }
    }
}
