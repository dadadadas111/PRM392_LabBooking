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
                noInternetText.setText("No internet connection. Some features may not work.");
                noInternetText.setTextColor(0xFFD32F2F);
                noInternetBar.setVisibility(View.VISIBLE);
                wasOffline = true;
            } else {
                if (wasOffline) {
                    // Show online bar for a few seconds
                    noInternetBar.setBackgroundColor(0xFF4CAF50); // Green
                    noInternetText.setText("Internet connection is back.");
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
