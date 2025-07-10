package com.example.prm392_labbooking.navigation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.prm392_labbooking.presentation.MainActivity;
import com.example.prm392_labbooking.presentation.auth.LoginActivity;
import com.example.prm392_labbooking.presentation.auth.RegisterActivity;
import com.example.prm392_labbooking.presentation.auth.ForgotPasswordActivity;
import com.example.prm392_labbooking.presentation.home.HomeFragment;
import com.example.prm392_labbooking.presentation.map.MapFragment;
import com.example.prm392_labbooking.presentation.cart.CartFragment;
import com.example.prm392_labbooking.presentation.settings.SettingsFragment;
import com.example.prm392_labbooking.presentation.billing.BillingFragment;
import com.example.prm392_labbooking.presentation.chat.ChatActivity;
import com.example.prm392_labbooking.R;

public class NavigationManager {
    public static void goToLogin(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    public static void goToRegister(Context context) {
        context.startActivity(new Intent(context, RegisterActivity.class));
    }

    public static void goToForgotPassword(Context context)   {
        context.startActivity(new Intent(context, ForgotPasswordActivity.class));
    }

    public static void goToMain(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    public static void showHome(FragmentManager fm) {
        showFragment(fm, new HomeFragment());
    }

    public static void showMap(FragmentManager fm) {
        showFragment(fm, new MapFragment());
    }

    public static void showCart(FragmentManager fm) {
        showFragment(fm, new CartFragment());
    }

    public static void showSettings(FragmentManager fm) {
        showFragment(fm, new SettingsFragment());
    }

    public static void showBilling(FragmentManager fm, Bundle args) {
        BillingFragment fragment = new BillingFragment();
        if (args != null) fragment.setArguments(args);
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void showBilling(FragmentManager fm) {
        showBilling(fm, null);
    }

    private static void showFragment(FragmentManager fm, Fragment fragment) {
        fm.beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public static void goToChat(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    public static void goBack(Activity activity) {
        activity.finish();
    }
}