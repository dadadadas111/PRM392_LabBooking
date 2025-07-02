package com.example.prm392_labbooking.domain.usecase.booking;

import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import java.util.List;

public class SaveBookingUseCase {
    private DatabaseHelper dbHelper;

    public SaveBookingUseCase(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public boolean execute(List<CartItem> items, double totalPrice) {
        return dbHelper.saveBooking(items, totalPrice);
    }

    public void clearCart() {
        dbHelper.clearCart();
    }
}