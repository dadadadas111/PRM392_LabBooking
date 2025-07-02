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
import com.example.prm392_labbooking.navigation.NavigationManager;

@SuppressWarnings("deprecation")
public class LoginActivity extends BaseActivity {
    // UI elements
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;
    private Button googleSignInButton;
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
        initLoadingOverlay();
        initViews();
        setupGoogleSignIn();
        setListeners();
        setGoogleButtonText();
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
        // googleSignInButton.setText(getString(R.string.google_sign_in));
    }

    private void attemptLogin() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString();

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
        showLoading();
        loginButton.setEnabled(false);
        signInWithEmailAndPassword(email, password);
    }

    private void onForgotPassword() {
        NavigationManager.goToForgotPassword(this);
    }

    private void onRegister() {
        NavigationManager.goToRegister(this);
    }

    private void signInWithEmailAndPassword(String email, String password) {
        authRepository.login(email, password)
            .addOnCompleteListener(this, task -> {
                hideLoading();
                loginButton.setEnabled(true);
                if (task.isSuccessful()) {
                    Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show();
                    NavigationManager.goToMain(this);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.login_failed, task.getException() != null ? task.getException().getMessage() : ""), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getString(R.string.google_sign_in_failed, e.getMessage()), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(this, getString(R.string.google_sign_in_success), Toast.LENGTH_SHORT).show();
                    NavigationManager.goToMain(this);
                } else {
                    Toast.makeText(this, getString(R.string.google_sign_in_failed), Toast.LENGTH_SHORT).show();
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
