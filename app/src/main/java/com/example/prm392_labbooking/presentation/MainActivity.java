package com.example.prm392_labbooking.presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.presentation.cart.CartManager;
import com.example.prm392_labbooking.services.PreloadManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;
import com.example.prm392_labbooking.utils.LocaleUtils;
import com.example.prm392_labbooking.utils.ThemeUtils;

import java.util.List;

public class MainActivity extends AuthRequiredActivity {
    private static final int PERMISSION_REQUEST_CODE = 1001;
    private CartManager cartManager;
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleUtils.applyLocale(this); // Apply locale before theme
        ThemeUtils.applyTheme(this); // Apply theme before setContentView
        PreloadManager.getInstance().initialize(this); // Preload Google Maps, Firebase, and MapView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        cartManager = CartManager.getInstance(this);
        Intent intent = getIntent();
        if (intent == null || !intent.getBooleanExtra("open_cart", false)) {
            List<CartItem> cartItems = cartManager.getCartItems();
            CartItem soonestItem = null;
            long soonestTime = Long.MAX_VALUE;
            for (CartItem item : cartItems) {
                List<com.example.prm392_labbooking.domain.model.Slot> slots = item.getSlots();
                if (slots != null && !slots.isEmpty() && item.getDate() != null) {
                    long bookingTime = com.example.prm392_labbooking.notifications.CartExpiryScheduler.getSlotStartMillis(item.getDate(), slots.get(0));
                    if (bookingTime < soonestTime) {
                        soonestTime = bookingTime;
                        soonestItem = item;
                    }
                }
            }
            if (soonestItem != null) {
                com.example.prm392_labbooking.notifications.CartExpiryScheduler.pushImmediateNotification(this, soonestItem, cartItems.indexOf(soonestItem));
            }
        }

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

        requestNecessaryPermissions();
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void requestNecessaryPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.USE_EXACT_ALARM
        };
        List<String> permissionsToRequest = new java.util.ArrayList<>();
        for (String perm : permissions) {
            if (ContextCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(perm);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission denied: " + permissions[i], Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (cartManager == null) {
            cartManager = CartManager.getInstance(this);
        }
        // Check if launched from notification to open cart fragment
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("open_cart", false)) {
            NavigationManager.showCart(getSupportFragmentManager());
            // Optionally clear the flag so it doesn't repeat
            intent.removeExtra("open_cart");
        }
        else {

        }
    }

    public void hideBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(android.view.View.GONE);
        }
    }

    public void showBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        if (bottomNav != null) {
            bottomNav.setVisibility(android.view.View.VISIBLE);
        }
    }
}