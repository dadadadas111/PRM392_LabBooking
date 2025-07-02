package com.example.prm392_labbooking.domain.model;

public class Product {
    private String name;
    private String facilities;
    private double price; // Added price field

    public Product(String name) {
        this.name = name;
        this.price = 50.0; // Default base price
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacilities() {
        return facilities;
    }

    public void setFacilities(String facilities) {
        this.facilities = facilities;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}