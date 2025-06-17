package com.example.prm392_labbooking.presentation.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.BaseActivity;
import com.example.prm392_labbooking.utils.ValidationUtils;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.AuthRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.AuthRepository;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.android.gms.common.SignInButton;

public class LoginActivity extends BaseActivity {
    // UI elements
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;
    private SignInButton googleSignInButton;
    private FrameLayout loadingOverlay;

    // Logic fields
    private GoogleSignInClient googleSignInClient;
    private AuthRepository authRepository;

    // Constants
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setupGoogleSignIn();
        setListeners();
        setGoogleButtonText();
        loadingOverlay = findViewById(R.id.loadingOverlay);
    }

    private void initViews() {
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registerText = findViewById(R.id.registerText);
        googleSignInButton = findViewById(R.id.googleSignInButton);
        authRepository = new AuthRepositoryImpl(new FirebaseAuthService());
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void setListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        forgotPasswordText.setOnClickListener(v -> onForgotPassword());
        registerText.setOnClickListener(v -> onRegister());
        googleSignInButton.setOnClickListener(v -> signInWithGoogle());
    }

    private void setGoogleButtonText() {
        for (int i = 0; i < googleSignInButton.getChildCount(); i++) {
            View v = googleSignInButton.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setText("Đăng nhập với Google");
                break;
            }
        }
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
        signInWithEmailAndPassword(email, password);
    }

    private void onForgotPassword() {
        Toast.makeText(this, "Quên mật khẩu?", Toast.LENGTH_SHORT).show();
        // TODO: Implement forgot password flow
    }

    private void onRegister() {
        Toast.makeText(this, "Chưa có tài khoản? Đăng ký", Toast.LENGTH_SHORT).show();
        // TODO: Navigate to registration screen
    }

    private void signInWithEmailAndPassword(String email, String password) {
        authRepository.login(email, password)
            .addOnCompleteListener(this, task -> {
                hideLoading();
                loginButton.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void signInWithGoogle() {
        showLoading();
        // Always sign out first to force account chooser
        googleSignInClient.signOut().addOnCompleteListener(this, task -> {
            Intent signInIntent = googleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideLoading();
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account.getIdToken());
                }
            } catch (ApiException e) {
                Toast.makeText(this, "Google sign in failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        showLoading();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        authRepository.signInWithCredential(credential)
            .addOnCompleteListener(this, task -> {
                hideLoading();
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Đăng nhập Google thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Đăng nhập Google thất bại!", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    protected void showLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    protected void hideLoading() {
        if (loadingOverlay != null) loadingOverlay.setVisibility(View.GONE);
    }
}
