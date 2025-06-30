package com.example.prm392_labbooking.presentation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.services.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;

public class MainActivity extends AuthRequiredActivity {
    private CartManager cartManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoadingOverlay();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        cartManager = new CartManager(this);
        checkCartAndNotify();
//        // Test button for loading overlay
//        findViewById(R.id.btnTestLoading).setOnClickListener(v -> {
//            if (findViewById(R.id.loadingOverlay).getVisibility() == android.view.View.VISIBLE) {
//                hideLoading();
//            } else {
//                showLoading();
//            }
//        });

        // Show HomeFragment by default
        if (savedInstanceState == null) {
            NavigationManager.showHome(getSupportFragmentManager());
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
                if (item.isChecked()) return false; // Prevent re-clicking selected
                int id = item.getItemId();
                if (id == R.id.nav_home) {
                    NavigationManager.showHome(getSupportFragmentManager());
                    return true;
                } else if (id == R.id.nav_map) {
                    NavigationManager.showMap(getSupportFragmentManager());
                    return true;
                } else if (id == R.id.nav_cart) {
                    NavigationManager.showCart(getSupportFragmentManager());
                    return true;
                } else if (id == R.id.nav_settings) {
                    NavigationManager.showSettings(getSupportFragmentManager());
                    return true;
                }
                return false;
            }
        });

    }

    private void checkCartAndNotify() {
        if (!cartManager.getCartItems().isEmpty()) {
            Toast.makeText(this, "You have items in your cart!", Toast.LENGTH_LONG).show();
        }
    }
}
