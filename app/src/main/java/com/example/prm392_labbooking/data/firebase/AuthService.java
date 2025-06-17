package com.example.prm392_labbooking.data.firebase;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface AuthService {
    Task<AuthResult> login(String email, String password);
    Task<AuthResult> register(String email, String password);
    void logout();
    boolean isLoggedIn();
    String getCurrentUserId();
}
