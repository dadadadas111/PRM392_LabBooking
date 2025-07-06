package com.example.prm392_labbooking.domain.model;

import java.util.List;

public class Product {
    private String name;
    private String facilities; // delete later
    private double price; // Added price field

    // new fields
    private List<String> images;
    private List<Facility> _facilities;
    private int number;

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