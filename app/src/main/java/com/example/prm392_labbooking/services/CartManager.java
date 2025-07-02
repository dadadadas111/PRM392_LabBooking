package com.example.prm392_labbooking.services;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.prm392_labbooking.domain.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartManager {
    private static final String PREF_NAME = "MeetingCartPrefs";
    private static final String CART_KEY = "CART_ITEMS";

    private SharedPreferences prefs;
    private Gson gson;

    public CartManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public List<CartItem> getCartItems() {
        String json = prefs.getString(CART_KEY, null);
        if (json == null) return new ArrayList<>();

        Type type = new TypeToken<List<CartItem>>() {}.getType();
        return gson.fromJson(json, type);
    }

    public void saveCartItems(List<CartItem> items) {
        String json = gson.toJson(items);
        prefs.edit().putString(CART_KEY, json).apply();
    }

    public void addItem(CartItem item) {
        List<CartItem> items = getCartItems();
        items.add(item);
        saveCartItems(items);
    }

    public void clearCart() {
        prefs.edit().remove(CART_KEY).apply();
    }
    public void clearAndSave(List<CartItem> list) {
        list.clear();
        saveCartItems(list);
    }


    public void addSampleCartItems() {
        List<CartItem> sampleItems = new ArrayList<>();

        sampleItems.add(new CartItem(
                "Room A",
                "2025-07-01",
                "08:00 - 10:00",
                120000,
                1,
                Arrays.asList("Whiteboard", "Projector")
        ));

        sampleItems.add(new CartItem(
                "Room B",
                "2025-07-01",
                "10:30 - 12:30",
                120000,
                2,
                Arrays.asList("HDMI", "TV")
        ));

        sampleItems.add(new CartItem(
                "Room C",
                "2025-07-02",
                "13:00 - 15:00",
                120000,
                1,
                Arrays.asList("Microphone", "WiFi")
        ));

        saveCartItems(sampleItems); // Ghi vào SharedPreferences
    }

}
