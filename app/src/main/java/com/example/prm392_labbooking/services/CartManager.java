package com.example.prm392_labbooking.services;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.model.Facility;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.domain.model.Slot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<CartItem> sampleCartItems() {
        List<CartItem> sampleItems = new ArrayList<>();

        Product roomA = new Product("Room A", 100000, 100000, 0);
        Product roomB = new Product("Room B", 150000, 150000, 0);
        Product roomC = new Product("Room C", 150000, 150000, 0);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            sampleItems.add(new CartItem(
                    roomA,
                    Arrays.asList(Facility.WHITE_BOARD, Facility.MICROPHONE),
                    Arrays.asList(Slot.SLOT_08_09, Slot.SLOT_14_15),
                    sdf.parse("2025-07-01"),
                    calculateTotalPrice(roomA, Arrays.asList(Facility.WHITE_BOARD, Facility.MICROPHONE), Arrays.asList(Slot.SLOT_08_09, Slot.SLOT_14_15))
            ));

            sampleItems.add(new CartItem(
                    roomB,
                    Arrays.asList(Facility.TV),
                    Arrays.asList(Slot.SLOT_13_14),
                    sdf.parse("2025-07-01"),
                    calculateTotalPrice(roomB, Arrays.asList(Facility.TV), Arrays.asList(Slot.SLOT_13_14))
            ));

            sampleItems.add(new CartItem(
                    roomC,
                    Arrays.asList(Facility.MICROPHONE),
                    Arrays.asList(Slot.SLOT_11_12, Slot.SLOT_12_13),
                    sdf.parse("2025-07-02"),
                    calculateTotalPrice(roomC, Arrays.asList(Facility.MICROPHONE), Arrays.asList(Slot.SLOT_11_12, Slot.SLOT_12_13))
            ));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return sampleItems;
    }

    public void saveCartItems(List<CartItem> items) {
        String json = gson.toJson(items);
        prefs.edit().putString(CART_KEY, json).apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addItem(CartItem newItem) {
        List<CartItem> items = getCartItems();

        for (CartItem existingItem : items) {
            // So sánh phòng
            if (existingItem.getProduct().getName().equals(newItem.getProduct().getName())) {

                // So sánh ngày (so sánh theo yyyy-MM-dd)
                boolean sameDate = isSameDay(existingItem.getDate(), newItem.getDate());

                if (sameDate) {
                    // Kiểm tra nếu có bất kỳ slot nào trùng
                    for (Slot newSlot : newItem.getSlots()) {
                        if (existingItem.getSlots().contains(newSlot)) {
                            // Đã có phòng này với slot trùng → Không thêm
                            return;
                        }
                    }
                }
            }
        }

        // Nếu không trùng thì thêm vào
        items.add(newItem);
        saveCartItems(items);
    }


    public void clearCart() {
        prefs.edit().remove(CART_KEY).apply();
    }
    public void clearAndSave(List<CartItem> list) {
        list.clear();
        saveCartItems(list);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addSampleCartItems() {
        List<CartItem> sampleItems = new ArrayList<>();

        Product roomA = new Product("Room A", 100000, 100000, 0);
        Product roomB = new Product("Room B", 150000, 150000, 0);
        Product roomC = new Product("Room C", 150000, 150000, 0);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            sampleItems.add(new CartItem(
                    roomA,
                    Arrays.asList(Facility.WHITE_BOARD, Facility.MICROPHONE),
                    Arrays.asList(Slot.SLOT_08_09, Slot.SLOT_14_15),
                    sdf.parse("2025-07-01"),
                    120000
            ));

            sampleItems.add(new CartItem(
                    roomB,
                    Arrays.asList(Facility.TV),
                    Arrays.asList(Slot.SLOT_13_14),
                    sdf.parse("2025-07-01"),
                    150000
            ));

            sampleItems.add(new CartItem(
                    roomC,
                    Arrays.asList(Facility.MICROPHONE),
                    Arrays.asList(Slot.SLOT_11_12, Slot.SLOT_12_13),
                    sdf.parse("2025-07-02"),
                    220000
            ));


            saveCartItems(sampleItems); // Ghi vào SharedPreferences
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (CartItem item : sampleItems){
            double priceNew = calculateTotalPrice(item.getProduct(),item.getFacilities(),
                    item.getSlots());
            item.setPrice(priceNew);
        }
        saveCartItems(sampleItems); // Ghi vào SharedPreferences
    }

    public static double calculateTotalPrice(Product product,List<Facility> facilities,
                                             List<Slot> slots){
        double basePrice = product.getPrice();
        double facilityPrice = 0;
        for (Facility facility :facilities){
            facilityPrice += facility.getPrice();
        }

        int slotCount = slots.size();
        return (basePrice + facilityPrice) * slotCount;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isSameDay(Date d1, Date d2) {
        LocalDate localDate1 = d1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = d2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }



}
