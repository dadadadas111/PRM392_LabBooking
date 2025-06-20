package com.example.prm392_labbooking.utils;

import android.content.Context;
import java.io.IOException;
import java.util.Properties;

public class SecretLoader {
    public static String getGeminiApiKey(Context context) {
        try {
            Properties properties = new Properties();
            properties.load(context.getAssets().open("secrets.properties"));
            return properties.getProperty("GEMINI_API_KEY", "");
        } catch (IOException e) {
            return "";
        }
    }
}
