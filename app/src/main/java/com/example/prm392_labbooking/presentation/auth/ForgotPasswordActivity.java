package com.example.prm392_labbooking.presentation.auth;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.presentation.base.BaseActivity;
import com.example.prm392_labbooking.utils.ValidationUtils;

public class ForgotPasswordActivity extends BaseActivity {
    private EditText emailEditText;
    private Button resetPasswordButton;
    private AuthRepository authRepository;
    private FrameLayout loadingOverlay;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewById(R.id.backToLoginButton).setOnClickListener(v -> NavigationManager.goToLogin(this));
        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        authRepository = new AuthRepositoryImpl(new FirebaseAuthService());
        loadingOverlay = findViewById(R.id.loadingOverlay);
        resetPasswordButton.setOnClickListener(v -> onResetPassword());
    }

    private void onResetPassword() {
        String email = emailEditText.getText().toString().trim();
        if (!ValidationUtils.isValidEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_email));
            emailEditText.requestFocus();
            return;
        }
        showLoading();
        authRepository.resetPassword(email)
            .addOnCompleteListener(task -> {
                hideLoading();
                if (task.isSuccessful()) {
                    Toast.makeText(this, getString(R.string.reset_password_email_sent), Toast.LENGTH_SHORT).show();
                    NavigationManager.goToLogin(this);
                } else {
                    Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void showLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
    }
}
