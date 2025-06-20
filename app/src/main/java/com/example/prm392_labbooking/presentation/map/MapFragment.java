package com.example.prm392_labbooking.presentation.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.BaseFragment;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.example.prm392_labbooking.utils.MapUtils;
import org.osmdroid.config.Configuration;
import org.osmdroid.views.MapView;

public class MapFragment extends BaseFragment {
    private MapView mapView;
    private ImageButton btnOpenInGoogleMaps, btnRecenter, btnOtherAction;
    private AuthRepository authRepository;
    private static final double LAB_LAT = 10.762622;
    private static final double LAB_LON = 106.660172;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Configuration.getInstance().setUserAgentValue(requireContext().getPackageName());
        View root = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = root.findViewById(R.id.mapView);
        btnOpenInGoogleMaps = root.findViewById(R.id.btnOpenInGoogleMaps);
        btnRecenter = root.findViewById(R.id.btnRecenter);
        btnOtherAction = root.findViewById(R.id.btnOtherAction);
        authRepository = new AuthRepositoryImpl(new FirebaseAuthService());

        MapUtils.setupLabMap(mapView, LAB_LAT, LAB_LON, "LAB Location");
        btnOpenInGoogleMaps.setOnClickListener(v -> MapUtils.openInGoogleMaps(requireContext(), LAB_LAT, LAB_LON, "LAB"));
        btnRecenter.setOnClickListener(v -> MapUtils.recenterMap(mapView, LAB_LAT, LAB_LON));
        btnOtherAction.setOnClickListener(v -> {
            // Placeholder for another map action
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mapView != null) {
            mapView.onDetach();
        }
    }
}
