package com.example.prm392_labbooking.domain.model;

import java.util.List;

public class CartItem {
    private String roomId;
    private String date;
    private String timeSlot;
    private int quantity;
    private double price;
    private List<String> features;
    public CartItem() {
    }

    // 🔹 Constructor đầy đủ
    public CartItem(String roomId, String date, String timeSlot, double price, int quantity, List<String> features) {
        this.roomId = roomId;
        this.date = date;
        this.timeSlot = timeSlot;
        this.quantity = quantity;
        this.features = features;
        this.price = price;
    }

    // 🔹 Getter và Setter
    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
