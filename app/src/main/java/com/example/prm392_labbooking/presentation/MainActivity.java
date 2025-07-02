package com.example.prm392_labbooking.presentation;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.services.CartManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;
import com.example.prm392_labbooking.utils.LocaleUtils;
import com.example.prm392_labbooking.utils.ThemeUtils;

public class MainActivity extends AuthRequiredActivity {
    private DatabaseHelper dbHelper;

    private CartManager cartManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LocaleUtils.applyLocale(this); // Apply locale before theme
        ThemeUtils.applyTheme(this); // Apply theme before setContentView
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initLoadingOverlay();

        dbHelper = new DatabaseHelper(this);
        initializeSampleData();

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

    private void initializeSampleData() {
        CartItem item1 = new CartItem();
        // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//        item1.setPackageName("Seat Package (4 seats)");
//        item1.setDetails("With whiteboard");
        item1.setPrice(50.0);

        CartItem item2 = new CartItem();
        // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//        item2.setPackageName("Table Package (6 seats)");
//        item2.setDetails("With TV and network");
        item2.setPrice(75.0);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cart", null, null); // Xóa dữ liệu cũ nếu có
        ContentValues values1 = new ContentValues();
        // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//        values1.put("package_name", item1.getPackageName());
//        values1.put("details", item1.getDetails());
        values1.put("price", item1.getPrice());
        db.insert("cart", null, values1);

        ContentValues values2 = new ContentValues();
        // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//        values2.put("package_name", item2.getPackageName());
//        values2.put("details", item2.getDetails());
        values2.put("price", item2.getPrice());
        db.insert("cart", null, values2);
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