package com.example.prm392_labbooking.presentation.map;

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
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.BaseFragment;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.example.prm392_labbooking.utils.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

public class MapFragment extends BaseFragment {
    private MapView mapView;
    private ImageButton btnOpenInGoogleMaps, btnRecenter, btnMapLayers, btnShowDirections, btnCompass;
    private ImageView offscreenMarker;
    private FrameLayout mapContainer;
    private static final double LAB_LAT = 10.762622;    private static final double LAB_LON = 106.660172;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapContainer = (FrameLayout) root;
        mapView = root.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        btnOpenInGoogleMaps = root.findViewById(R.id.btnOpenInGoogleMaps);
        btnRecenter = root.findViewById(R.id.btnRecenter);
        btnMapLayers = root.findViewById(R.id.btnMapLayers);
        btnShowDirections = root.findViewById(R.id.btnShowDirections);        btnCompass = root.findViewById(R.id.btnCompass);
        offscreenMarker = root.findViewById(R.id.offscreenMarker);

        MapUtils.setupLabMap(mapView, LAB_LAT, LAB_LON, "LAB Location");
        MapUtils.setupCompass(btnCompass);
        MapUtils.setupOffscreenMarker(offscreenMarker, mapContainer);
        
        btnOpenInGoogleMaps.setOnClickListener(v -> MapUtils.openInGoogleMaps(requireContext(), LAB_LAT, LAB_LON, "LAB"));
        btnRecenter.setOnClickListener(v -> MapUtils.recenterMap(mapView, LAB_LAT, LAB_LON));
        btnShowDirections.setOnClickListener(v -> MapUtils.openDirections(requireContext(), LAB_LAT, LAB_LON));
        btnMapLayers.setOnClickListener(v -> showMapLayersDialog());
        btnCompass.setOnClickListener(v -> {
            MapUtils.resetMapBearing();
            Toast.makeText(requireContext(), getString(R.string.reset_compass), Toast.LENGTH_SHORT).show();
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
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
