package com.example.prm392_labbooking.presentation;

import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;

public class MainActivity extends AuthRequiredActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        // Show HomeFragment by default
        if (savedInstanceState == null) {
            NavigationManager.showHome(getSupportFragmentManager());
        }

        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
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
}
