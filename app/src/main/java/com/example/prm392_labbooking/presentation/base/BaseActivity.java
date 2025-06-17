package com.example.prm392_labbooking.presentation.base;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    private FrameLayout loadingOverlay;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add a dimmed overlay with a centered ProgressBar
        loadingOverlay = new FrameLayout(this);
        loadingOverlay.setBackgroundColor(0x88000000); // semi-transparent black
        loadingOverlay.setVisibility(View.GONE);
        progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
        progressBar.setIndeterminate(true);
        FrameLayout.LayoutParams pbParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        pbParams.gravity = android.view.Gravity.CENTER;
        loadingOverlay.addView(progressBar, pbParams);
        addContentView(loadingOverlay, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loadingOverlay = null;
        progressBar = null;
    }

    protected void showLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
    }
}
