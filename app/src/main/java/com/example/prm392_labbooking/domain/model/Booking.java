package com.example.prm392_labbooking.domain.model;

import java.util.List;

public class Booking {
    public String id;
    public String userId;
    public String packageId;
    public String packageName;
    public int capacity;
    public List<String> facilities; // e.g., ["whiteboard", "tv"]
    public String date;
    public String time;
    public String status;
    public double price;
    // Add more fields as needed
}
