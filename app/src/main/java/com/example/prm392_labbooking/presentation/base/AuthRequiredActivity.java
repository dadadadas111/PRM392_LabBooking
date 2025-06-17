package com.example.prm392_labbooking.presentation.base;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.example.prm392_labbooking.navigation.NavigationManager;

public abstract class AuthRequiredActivity extends BaseActivity {
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authRepository = new AuthRepositoryImpl(new FirebaseAuthService());
        if (!authRepository.isLoggedIn()) {
            NavigationManager.goToLogin(this);
            finish();
        }
    }
}
