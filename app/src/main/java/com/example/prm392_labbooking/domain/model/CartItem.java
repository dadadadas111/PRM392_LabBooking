package com.example.prm392_labbooking.domain.model;

import java.util.List;

public class CartItem {
    // TODO: Làm rõ xem có cần thiết roomId không
    // Làm rõ chọn time slot như thế nào, ở màn hình nào. 
    // Product details? fix cứng slot hay cho tùy chọn?
    // Nếu tùy chọn thì tính tiền như thế nào?
    // Nếu theo slot thì có cho phép slot liên tiếp không?
    // Ví dụ : 8h-10h, 10h-12h, 14h-16h, 16h-18h, người dùng muốn 2 slot liên tiếp thì có được không?

    private String roomId; // delete
    private String date;
    private String timeSlot; // delete
    private int quantity;
    private double price; // = gia product * so slot + tong gia facility
    private List<String> features; // delete

    // new
    private Product product;
    private List<Facility> _facilities;
    private List<Slot> slots;

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
