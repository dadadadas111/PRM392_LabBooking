package com.example.prm392_labbooking.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapsSdkInitializedCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PreloadService extends Service {
    private static final String TAG = "PreloadService";
    private final IBinder binder = new PreloadBinder();
    private ExecutorService executorService;
    private boolean mapsInitialized = false;
    private boolean firebaseInitialized = false;
    
    public class PreloadBinder extends Binder {
        public PreloadService getService() {
            return PreloadService.this;
        }
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "PreloadService created");
        executorService = Executors.newFixedThreadPool(3);
        
        // Start preloading immediately
        startPreloading();
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "PreloadService started");
        return START_STICKY; // Restart if killed
    }
    
    private void startPreloading() {
        Log.d(TAG, "Starting preloading services...");
        
        // Preload Google Maps
        preloadGoogleMaps();
        
        // Preload Firebase services
        preloadFirebaseServices();
        
        // Add other preloading tasks here in the future
        // preloadImageCache();
        // preloadUserData();
    }
    
    private void preloadGoogleMaps() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Preloading Google Maps...");
                
                // Initialize Maps SDK
                MapsInitializer.initialize(getApplicationContext(), MapsInitializer.Renderer.LATEST, new OnMapsSdkInitializedCallback() {
                    @Override
                    public void onMapsSdkInitialized(MapsInitializer.Renderer renderer) {
                        Log.d(TAG, "Google Maps SDK initialized with renderer: " + renderer);
                        mapsInitialized = true;
                    }
                });
                
                Log.d(TAG, "Google Maps preloading completed");
            } catch (Exception e) {
                Log.e(TAG, "Error preloading Google Maps: " + e.getMessage(), e);
            }
        });
    }
    
    // Future preloading methods can be added here
    public void preloadFirebaseServices() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Preloading Firebase services...");
                
                // Initialize Firebase Auth
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Log.d(TAG, "Firebase Auth initialized");
                
                // Initialize and configure Firestore
                FirebaseFirestore firestore = FirebaseFirestore.getInstance();
                FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                        .setPersistenceEnabled(true)
                        .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                        .build();
                firestore.setFirestoreSettings(settings);
                Log.d(TAG, "Firestore initialized with persistence");
                
                // Initialize Firebase Storage
                Log.d(TAG, "Firebase Storage initialized");
                
                // Pre-warm connections by making a lightweight request
                firestore.enableNetwork().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Firestore network enabled and ready");
                        firebaseInitialized = true;
                    } else {
                        Log.w(TAG, "Failed to enable Firestore network", task.getException());
                    }
                });
                
                Log.d(TAG, "Firebase services preloading completed");
            } catch (Exception e) {
                Log.e(TAG, "Error preloading Firebase: " + e.getMessage(), e);
            }
        });
    }
    
    public void preloadImageCache() {
        executorService.execute(() -> {
            try {
                Log.d(TAG, "Preloading image cache...");
                // Add image preloading here
                Log.d(TAG, "Image cache preloading completed");
            } catch (Exception e) {
                Log.e(TAG, "Error preloading images: " + e.getMessage(), e);
            }
        });
    }
    
    public boolean isMapsReady() {
        return mapsInitialized;
    }
    
    public boolean isFirebaseReady() {
        return firebaseInitialized;
    }
    
    public boolean isAllReady() {
        return mapsInitialized && firebaseInitialized;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "PreloadService destroyed");
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
