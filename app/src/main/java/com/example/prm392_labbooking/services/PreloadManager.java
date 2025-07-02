package com.example.prm392_labbooking.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.example.prm392_labbooking.utils.MapPreloader;

public class PreloadManager {
    private static final String TAG = "PreloadManager";
    private static PreloadManager instance;
    private Context context;
    private PreloadService preloadService;
    private boolean serviceBound = false;
    
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "PreloadService connected");
            PreloadService.PreloadBinder binder = (PreloadService.PreloadBinder) service;
            preloadService = binder.getService();
            serviceBound = true;
        }
        
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "PreloadService disconnected");
            serviceBound = false;
            preloadService = null;
        }
    };
    
    private PreloadManager() {}
    
    public static synchronized PreloadManager getInstance() {
        if (instance == null) {
            instance = new PreloadManager();
        }
        return instance;
    }
    
    public void initialize(Context context) {
        this.context = context.getApplicationContext();
        MapPreloader.preload(this.context); // Preload Google MapView in background
        startPreloadService();
    }
    
    private void startPreloadService() {
        Intent intent = new Intent(context, PreloadService.class);
        context.startService(intent);
        context.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "PreloadService started and bound");
    }
    
    public boolean isMapsReady() {
        return serviceBound && preloadService != null && preloadService.isMapsReady();
    }
    
    public boolean isFirebaseReady() {
        return serviceBound && preloadService != null && preloadService.isFirebaseReady();
    }
    
    public boolean isAllReady() {
        return serviceBound && preloadService != null && preloadService.isAllReady();
    }
    
    public void preloadFirebaseServices() {
        if (serviceBound && preloadService != null) {
            preloadService.preloadFirebaseServices();
        }
    }
    
    public void preloadImageCache() {
        if (serviceBound && preloadService != null) {
            preloadService.preloadImageCache();
        }
    }
    
    public void shutdown() {
        if (serviceBound && context != null) {
            context.unbindService(serviceConnection);
            serviceBound = false;
        }
        if (context != null) {
            context.stopService(new Intent(context, PreloadService.class));
        }
        Log.d(TAG, "PreloadService shutdown");
    }
}
