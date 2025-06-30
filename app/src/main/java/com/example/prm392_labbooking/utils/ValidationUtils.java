package com.example.prm392_labbooking.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ValidationUtils {
    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return !TextUtils.isEmpty(password) && password.length() >= 6;
    }

    public static boolean isValidCardholderName(String name) {
        return name != null && name.matches("^[a-zA-Z ]+$") && !name.trim().isEmpty();
    }

    public static boolean isValidCardNumber(String number) {
        return number != null && number.matches("^\\d{16}$");
    }

    public static boolean isValidExpiryDate(String expiry) {
        if (expiry == null || !expiry.matches("^\\d{2}/\\d{2}$")) {
            return false;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        sdf.setLenient(false);
        try {
            Date expiryDate = sdf.parse(expiry);
            Date currentDate = new Date();
            return expiryDate.after(currentDate);
        } catch (ParseException e) {
            return false;
        }
    }

    public static boolean isValidCvv(String cvv) {
        return cvv != null && cvv.matches("^\\d{3}$");
    }
}
