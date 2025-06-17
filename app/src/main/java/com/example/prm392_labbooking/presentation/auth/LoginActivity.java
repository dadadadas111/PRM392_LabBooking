package com.example.prm392_labbooking.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.BaseActivity;
import com.example.prm392_labbooking.utils.ValidationUtils;

public class LoginActivity extends BaseActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registerText = findViewById(R.id.registerText);

        loginButton.setOnClickListener(v -> attemptLogin());
        forgotPasswordText.setOnClickListener(v -> onForgotPassword());
        registerText.setOnClickListener(v -> onRegister());
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

        if (!ValidationUtils.isValidEmail(email)) {
            emailEditText.setError("Vui lòng nhập email hợp lệ");
            emailEditText.requestFocus();
            return;
        }
        if (!ValidationUtils.isValidPassword(password)) {
            passwordEditText.setError("Mật khẩu phải có ít nhất 6 ký tự");
            passwordEditText.requestFocus();
            return;
        }
        showLoading();
        loginButton.setEnabled(false);

        // TODO: Replace with real authentication logic (API/Firebase)

    }

    private void onForgotPassword() {
        Toast.makeText(this, "Quên mật khẩu?", Toast.LENGTH_SHORT).show();
        // TODO: Implement forgot password flow
    }

    private void onRegister() {
        Toast.makeText(this, "Chưa có tài khoản? Đăng ký", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to registration screen
    }
}
