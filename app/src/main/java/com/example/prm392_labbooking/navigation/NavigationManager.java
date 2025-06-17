package com.example.prm392_labbooking.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.prm392_labbooking.presentation.auth.LoginActivity;
import com.example.prm392_labbooking.presentation.auth.RegisterActivity;
import com.example.prm392_labbooking.presentation.auth.ForgotPasswordActivity;

public class NavigationManager {
    public static void goToLogin(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void goToRegister(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    public static void goToForgotPassword(Context context) {
        context.startActivity(new Intent(context, ForgotPasswordActivity.class));
    }

    public static void goBack(Activity activity) {
        activity.finish();
    }
}
