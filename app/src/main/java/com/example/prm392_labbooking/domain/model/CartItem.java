package com.example.prm392_labbooking.domain.model;

import java.util.Date;
import java.util.List;

public class CartItem {
    private Date date;
    private double price; // = gia product * so slot + tong gia facility

    // new
    private Product product;
    private List<Facility> _facilities;
    private List<Slot> slots;
    private String error;

    public CartItem() {
    }

    // 🔹 Constructor đầy đủ
    public CartItem(Product product,List<Facility> facilities, List<Slot> slots, Date date, double price) {
        this.date = date;
        this.price = price;
        this.product = product;
        this._facilities = facilities;
        this.slots = slots;
    }

    // 🔹 Getter và Setter


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getPrice() {
        return price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Facility> getFacilities() {
        return _facilities;
    }


    public void setFacilities(List<Facility> _facilities) {
        this._facilities = _facilities;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
