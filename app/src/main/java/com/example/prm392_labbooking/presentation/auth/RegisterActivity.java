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

public class RegisterActivity extends BaseActivity {
    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initLoadingOverlay();
        findViewById(R.id.backToLoginButton).setOnClickListener(v -> NavigationManager.goToLogin(this));
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        authRepository = new AuthRepositoryImpl(new FirebaseAuthService());
        registerButton.setOnClickListener(v -> onRegister());
    }

    private void onRegister() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        if (!ValidationUtils.isValidEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_email));
            emailEditText.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            passwordEditText.setError(getString(R.string.invalid_password));
            passwordEditText.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError(getString(R.string.passwords_not_match));
            confirmPasswordEditText.requestFocus();
            return;
        }
        showLoading();
        authRepository.register(email, password)
            .addOnCompleteListener(task -> {
                hideLoading();
                if (task.isSuccessful()) {
                    Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show();
                    NavigationManager.goToLogin(this);
                } else {
                    Toast.makeText(this, getString(R.string.register_failed), Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public void showLoading() {
        super.showLoading();
    }

    @Override
    public void hideLoading() {
        super.hideLoading();
    }
}
