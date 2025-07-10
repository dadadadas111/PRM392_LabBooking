package com.example.prm392_labbooking.presentation.cart;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.prm392_labbooking.domain.model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<CartItem> cart;
    private final SharedPreferences prefs;
    private final Gson gson;
    private static final String PREF_NAME = "CartPrefs";
    private static final String CART_KEY = "cart_items";

    private CartManager(Context context) {
        cart = new ArrayList<>();
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
        loadCartItems();
    }

    public static CartManager getInstance(Context context) {
        if (instance == null) {
            instance = new CartManager(context.getApplicationContext());
        }
        return instance;
    }

    // Add CartItem to cart and save
    public void addToCart(CartItem cartItem) {
        cart.add(cartItem);
        saveCartItems();
    }

    // Update CartItem at specific index and save
    public void updateCartItem(int index, CartItem cartItem) {
        if (index >= 0 && index < cart.size()) {
            cart.set(index, cartItem);
            saveCartItems();
        }
    }

    // Get all CartItems in cart
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cart);
    }

    // Remove CartItem from cart and save
    public void removeFromCart(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
            saveCartItems();
        }
    }

    // Clear all CartItems and save
    public void clearCart() {
        cart.clear();
        saveCartItems();
    }

    // Save cart to SharedPreferences
    private void saveCartItems() {
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(cart);
        editor.putString(CART_KEY, json);
        editor.apply();
    }

    // Load cart from SharedPreferences
    private void loadCartItems() {
        String json = prefs.getString(CART_KEY, null);
        if (json != null) {
            Type type = new TypeToken<List<CartItem>>(){}.getType();
            List<CartItem> savedItems = gson.fromJson(json, type);
            if (savedItems != null) {
                cart.clear();
                cart.addAll(savedItems);
            }
        }
    }
}