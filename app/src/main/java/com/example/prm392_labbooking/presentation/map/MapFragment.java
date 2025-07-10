package com.example.prm392_labbooking.presentation.map;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.BaseFragment;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.example.prm392_labbooking.utils.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import android.location.Location;

public class MapFragment extends BaseFragment {
    private MapView mapView;
    private ImageButton btnOpenInGoogleMaps, btnRecenter, btnMapLayers, btnShowDirections, btnCompass;
    private ImageView offscreenMarker;
    private FrameLayout mapContainer;
    private ImageButton btnToggleMapActions;
    private boolean mapActionsVisible = true;
    private static final String LAB_NAME = "Đại học FPT Hà Nội";
    private static final double LAB_LAT = 21.012487572296216;    private static final double LAB_LON = 105.52542694866874;
    private FusedLocationProviderClient fusedLocationClient;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapContainer = root.findViewById(R.id.mapContainer);
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        btnOpenInGoogleMaps = root.findViewById(R.id.btnOpenInGoogleMaps);
        btnRecenter = root.findViewById(R.id.btnRecenter);
        btnMapLayers = root.findViewById(R.id.btnMapLayers);
        btnShowDirections = root.findViewById(R.id.btnShowDirections);        btnCompass = root.findViewById(R.id.btnCompass);
        offscreenMarker = root.findViewById(R.id.offscreenMarker);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                // Enable blue dot for current location if permission granted
                if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    googleMap.setMyLocationEnabled(true);
                } else {
                    // Optionally, show a message or request permission again
                }
            }
        });

        MapUtils.setupLabMap(mapView, LAB_LAT, LAB_LON, LAB_NAME);
        MapUtils.setupCompass(btnCompass);
        MapUtils.setupOffscreenMarker(offscreenMarker, mapContainer);
        
        btnOpenInGoogleMaps.setOnClickListener(v -> MapUtils.openInGoogleMaps(requireContext(), LAB_NAME));
        btnRecenter.setOnClickListener(v -> recenterToUserLocation());
        btnShowDirections.setOnClickListener(v -> MapUtils.openDirections(requireContext(), LAB_NAME));
        btnMapLayers.setOnClickListener(v -> showMapLayersDialog());
        btnCompass.setOnClickListener(v -> {
            MapUtils.resetMapBearing();
            Toast.makeText(requireContext(), getString(R.string.reset_compass), Toast.LENGTH_SHORT).show();
        });
        
        btnToggleMapActions = new ImageButton(requireContext());
        btnToggleMapActions.setImageResource(R.drawable.ic_eye_open); // Add this drawable
        btnToggleMapActions.setBackgroundResource(R.drawable.round_button_selector);
        btnToggleMapActions.setContentDescription(getString(R.string.toggle_map_actions));
        FrameLayout.LayoutParams eyeParams = new FrameLayout.LayoutParams(96, 96);
        eyeParams.leftMargin = 16;
        eyeParams.topMargin = 16;
        btnToggleMapActions.setLayoutParams(eyeParams);
        mapContainer.addView(btnToggleMapActions);

        btnToggleMapActions.setOnClickListener(v -> {
            mapActionsVisible = !mapActionsVisible;
            int visibility = mapActionsVisible ? View.VISIBLE : View.GONE;
            btnMapLayers.setVisibility(visibility);
            btnCompass.setVisibility(visibility);
            btnRecenter.setVisibility(visibility);
            btnOpenInGoogleMaps.setVisibility(visibility);
            btnShowDirections.setVisibility(visibility);
            // Change icon
            btnToggleMapActions.setImageResource(mapActionsVisible ? R.drawable.ic_eye_open : R.drawable.ic_eye_closed);
        });
        return root;
    }

    private void recenterToUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
                if (location != null && mapView != null) {
                    mapView.getMapAsync(googleMap -> {
                        com.google.android.gms.maps.model.LatLng userLatLng = new com.google.android.gms.maps.model.LatLng(location.getLatitude(), location.getLongitude());
                        com.google.android.gms.maps.model.LatLng labLatLng = new com.google.android.gms.maps.model.LatLng(LAB_LAT, LAB_LON);
                        com.google.android.gms.maps.model.LatLngBounds.Builder builder = new com.google.android.gms.maps.model.LatLngBounds.Builder();
                        builder.include(userLatLng);
                        builder.include(labLatLng);
                        com.google.android.gms.maps.model.LatLngBounds bounds = builder.build();
                        int padding = 120; // pixels, adjust as needed
                        googleMap.animateCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngBounds(bounds, padding));
                    });
                } else {
                    Toast.makeText(requireContext(), getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(requireContext(), getString(R.string.location_permission_required), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            android.view.View nav = getActivity().findViewById(R.id.bottom_navigation);
            if (nav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) nav).setSelectedItemId(R.id.nav_map);
            }
        }
        if (mapView != null) mapView.onResume();
    }

    @Override
    public void onPause() {
        if (mapView != null) mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        if (mapView != null) mapView.onDestroy();
        super.onDestroyView();
    }    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) mapView.onLowMemory();
    }
    
    private void showMapLayersDialog() {
        View dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_map_layers, null);
        
        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create();
          // Set up click listeners for each layer option
        dialogView.findViewById(R.id.layer_normal).setOnClickListener(v -> {
            MapUtils.changeMapType(mapView, GoogleMap.MAP_TYPE_NORMAL);
            Toast.makeText(requireContext(), getString(R.string.switched_to_normal), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        dialogView.findViewById(R.id.layer_satellite).setOnClickListener(v -> {
            MapUtils.changeMapType(mapView, GoogleMap.MAP_TYPE_SATELLITE);
            Toast.makeText(requireContext(), getString(R.string.switched_to_satellite), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        dialogView.findViewById(R.id.layer_terrain).setOnClickListener(v -> {
            MapUtils.changeMapType(mapView, GoogleMap.MAP_TYPE_TERRAIN);
            Toast.makeText(requireContext(), getString(R.string.switched_to_terrain), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        dialogView.findViewById(R.id.layer_hybrid).setOnClickListener(v -> {
            MapUtils.changeMapType(mapView, GoogleMap.MAP_TYPE_HYBRID);
            Toast.makeText(requireContext(), getString(R.string.switched_to_hybrid), Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
        
        dialog.show();
    }
}
