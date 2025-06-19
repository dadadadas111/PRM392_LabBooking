package com.example.prm392_labbooking.presentation.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private FrameLayout loadingOverlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not setContentView here! Child must call setContentView first, then call initLoadingOverlay()
    }

    protected void initLoadingOverlay() {
        loadingOverlay = findViewById(getResources().getIdentifier("loadingOverlay", "id", getPackageName()));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingOverlay = null;
    }

    public void showLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
    }
}
