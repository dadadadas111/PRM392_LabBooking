package com.example.prm392_labbooking.presentation.auth;

import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.presentation.base.BaseActivity;

public class ForgotPasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        findViewById(R.id.backToLoginButton).setOnClickListener(v -> NavigationManager.goToLogin(this));
        // TODO: Implement forgot password logic
        Toast.makeText(this, getString(R.string.forgot_password), Toast.LENGTH_SHORT).show();
    }
}
