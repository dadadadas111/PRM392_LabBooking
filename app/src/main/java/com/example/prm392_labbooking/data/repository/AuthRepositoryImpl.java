package com.example.prm392_labbooking.data.repository;

import com.example.prm392_labbooking.data.firebase.AuthService;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;

public class AuthRepositoryImpl implements AuthRepository {
    private final AuthService authService;

    public AuthRepositoryImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Task<AuthResult> login(String email, String password) {
        return authService.login(email, password);
    }

    @Override
    public Task<AuthResult> register(String email, String password) {
        return authService.register(email, password);
    }

    @Override
    public void logout() {
        authService.logout();
    }

    @Override
    public boolean isLoggedIn() {
        return authService.isLoggedIn();
    }

    @Override
    public String getCurrentUserId() {
        return authService.getCurrentUserId();
    }

    @Override
    public Task<AuthResult> signInWithCredential(AuthCredential credential) {
        return authService.signInWithCredential(credential);
    }

    @Override
    public Task<Void> resetPassword(String email) {
        return authService.resetPassword(email);
    }
}
