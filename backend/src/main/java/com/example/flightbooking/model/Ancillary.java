package com.example.flightbooking.model;

public enum Ancillary {
    EXTRA_BAGGAGE(35.0, "Extra Baggage (23kg)"),
    MEAL(15.0, "In-flight Meal"),
    PRIORITY_BOARDING(12.0, "Priority Boarding"),
    SEAT_UPGRADE(49.0, "Seat Upgrade"),
    TRAVEL_INSURANCE(25.0, "Travel Insurance"),
    LOUNGE_ACCESS(40.0, "Airport Lounge Access");

    public final double price;
    public final String label;

    Ancillary(double price, String label) {
        this.price = price;
        this.label = label;
    }
}
