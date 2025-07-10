package com.example.prm392_labbooking.presentation.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.presentation.base.BaseActivity;
import com.example.prm392_labbooking.utils.LocaleUtils;
import com.example.prm392_labbooking.utils.ThemeUtils;

public class SettingsFragment extends Fragment {
    private FirebaseAuthService authService = new FirebaseAuthService();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        TextView resetPasswordBtn = view.findViewById(R.id.btn_reset_password);
        TextView logoutBtn = view.findViewById(R.id.btn_login_logout);
        TextView chatSupportBtn = view.findViewById(R.id.btn_chat_support);
        Spinner themeSpinner = view.findViewById(R.id.spinner_theme);
        Spinner languageSpinner = view.findViewById(R.id.spinner_language);

        resetPasswordBtn.setOnClickListener(v -> showResetPasswordDialog());
        logoutBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.logout)
                    .setMessage(R.string.logout_confirm)
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        BaseActivity activity = (BaseActivity) requireActivity();
                        activity.showLoading();
                        authService.logout();
                        activity.hideLoading();
                        Toast.makeText(getContext(), R.string.logout, Toast.LENGTH_SHORT).show();
                        NavigationManager.goToLogin(requireActivity());
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        });
        chatSupportBtn.setOnClickListener(v -> NavigationManager.goToChat(requireContext()));

        int savedTheme = ThemeUtils.getSavedTheme(requireContext());
        themeSpinner.setSelection(savedTheme);
        themeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != savedTheme) {
                    ThemeUtils.saveTheme(requireContext(), position);
                    ThemeUtils.setTheme(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // Language selection logic
        String savedLang = LocaleUtils.getSavedLanguage(requireContext());
        int langPos = savedLang.equals("en") ? 1 : 0;
        languageSpinner.setSelection(langPos);
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
                String langCode = position == 1 ? "en" : "vi";
                if (!LocaleUtils.getSavedLanguage(requireContext()).equals(langCode)) {
                    LocaleUtils.setLocale(requireContext(), langCode);
                    requireActivity().recreate();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        return view;
    }

    private void showResetPasswordDialog() {
        EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        input.setHint(R.string.email);

        new AlertDialog.Builder(getContext())
                .setTitle(R.string.forgot_password)
                .setMessage(R.string.send_reset_password_request)
                .setView(input)
                .setPositiveButton(R.string.ok, (dialog, which) -> {
                    String email = input.getText().toString().trim();
                    if (!email.isEmpty()) {
                        BaseActivity activity = (BaseActivity) requireActivity();
                        activity.showLoading();
                        authService.resetPassword(email)
                                .addOnCompleteListener(aVoid -> activity.hideLoading())
                                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), R.string.reset_password_email_sent, Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e -> Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getContext(), R.string.invalid_email, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            android.view.View nav = getActivity().findViewById(R.id.bottom_navigation);
            if (nav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) nav).setSelectedItemId(R.id.nav_settings);
            }
        }
    }
}
