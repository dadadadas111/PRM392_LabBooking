package com.example.prm392_labbooking.domain.usecase.booking;

import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import java.util.List;

public class GetCartItemsUseCase {
    private DatabaseHelper dbHelper;

    public GetCartItemsUseCase(DatabaseHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    public List<CartItem> execute() {
        return dbHelper.getCartItems();
    }
}