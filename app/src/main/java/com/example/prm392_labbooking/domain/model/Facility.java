package com.example.prm392_labbooking.domain.model;

public enum Facility {
    WHITE_BOARD("WHITE_BOARD", 10.0),
    TV("TV", 15.0),
    MICROPHONE("MICROPHONE", 8.0),
    NETWORK("NETWORK", 5.0);

    private final String code;
    private final double price;

    Facility(String code, double price) {
        this.code = code;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public double getPrice() {
        return price;
    }

    // Get all facilities as string list
    public static void printAllFacilities() {
        for (Facility facility : Facility.values()) {
            System.out.println(facility.getCode() + " - $" + facility.getPrice());
        }
    }
}

