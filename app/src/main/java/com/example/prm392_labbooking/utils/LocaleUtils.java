package com.example.prm392_labbooking.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import java.util.Locale;

public class LocaleUtils {
    public static final String PREFS_NAME = "app_prefs";
    public static final String KEY_LANGUAGE = "app_language";

    public static void setLocale(Context context, String languageCode) {
        persistLanguage(context, languageCode);
        updateResources(context, languageCode);
    }

    public static String getSavedLanguage(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_LANGUAGE, "vi");
    }

    public static void persistLanguage(Context context, String languageCode) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply();
    }

    public static void updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    public static void applyLocale(Context context) {
        String lang = getSavedLanguage(context);
        setLocale(context, lang);
    }
}
