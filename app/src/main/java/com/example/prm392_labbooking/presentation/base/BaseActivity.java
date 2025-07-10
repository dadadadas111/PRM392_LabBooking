package com.example.prm392_labbooking.presentation.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.utils.NetworkUtils;

public abstract class BaseActivity extends AppCompatActivity {
    private FrameLayout loadingOverlay;
    private LinearLayout noInternetBar;
    private BroadcastReceiver networkReceiver;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable hideOnlineBarRunnable;
    private boolean wasOffline = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Do not setContentView here! Child must call setContentView first, then call initLoadingOverlay()
    }

    private void hideSystemUI() {
        // REMOVE immersive mode and navigation bar hiding logic to restore system navigation bar
        // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        //     getWindow().setDecorFitsSystemWindows(false);
        //     final android.view.WindowInsetsController insetsController = getWindow().getInsetsController();
        //     if (insetsController != null) {
        //         insetsController.hide(android.view.WindowInsets.Type.navigationBars() | android.view.WindowInsets.Type.statusBars());
        //         insetsController.setSystemBarsBehavior(
        //             android.view.WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        //         );
        //     }
        // } else {
        //     View decorView = getWindow().getDecorView();
        //     decorView.setSystemUiVisibility(
        //         View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        //         | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        //         | View.SYSTEM_UI_FLAG_FULLSCREEN
        //         | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
        //         | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        //     );
        // }
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

    @Override
    protected void onResume() {
        super.onResume();
        // Re-apply immersive mode when activity resumes
        hideSystemUI();
        if (noInternetBar == null) {
            noInternetBar = findViewById(getResources().getIdentifier("noInternetBar", "id", getPackageName()));
        }
        updateNoInternetBar();
        if (networkReceiver == null) {
            networkReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    updateNoInternetBar();
                }
            };
        }
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (networkReceiver != null) {
            unregisterReceiver(networkReceiver);
        }
    }

    private void updateNoInternetBar() {
        if (noInternetBar != null) {
            boolean isConnected = NetworkUtils.isNetworkAvailable(this);
            TextView noInternetText = noInternetBar.findViewById(getResources().getIdentifier("noInternetText", "id", getPackageName()));
            if (!isConnected) {
                // Show offline bar
                handler.removeCallbacksAndMessages(null);
                noInternetBar.setBackgroundColor(0xFFFFD700); // Yellow
                noInternetText.setText(getString(R.string.no_internet_connection));
                noInternetText.setTextColor(0xFFD32F2F);
                noInternetBar.setVisibility(View.VISIBLE);
                wasOffline = true;
            } else {
                if (wasOffline) {
                    // Show online bar for a few seconds
                    noInternetBar.setBackgroundColor(0xFF4CAF50); // Green
                    noInternetText.setText(getString(R.string.internet_connection_back));
                    noInternetText.setTextColor(0xFFFFFFFF);
                    noInternetBar.setVisibility(View.VISIBLE);
                    if (hideOnlineBarRunnable != null) handler.removeCallbacks(hideOnlineBarRunnable);
                    hideOnlineBarRunnable = new Runnable() {
                        @Override
                        public void run() {
                            noInternetBar.setVisibility(View.GONE);
                        }
                    };
                    handler.postDelayed(hideOnlineBarRunnable, 2000); // 2 seconds
                    wasOffline = false;
                } else {
                    noInternetBar.setVisibility(View.GONE);
                }
            }
        }
    }
}
