package com.example.prm392_labbooking.domain.model;

import java.util.List;

public class Product {
    private String name;
    private double price; // Added price field

    // new fields
    private List<String> images;
    private List<Facility> _facilities;
    private int number;

    private int imageResId;
    public Product(String name) {
        this.name = name;
        this.price = 50.0; // Default base price
    }
    public Product(String name, double price, int number,int imageResId) {
        this.name = name;
        this.price = price;
        this.number = number; // Default base price
        this.imageResId = imageResId;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
    public int getImageResId() {
        return imageResId;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}