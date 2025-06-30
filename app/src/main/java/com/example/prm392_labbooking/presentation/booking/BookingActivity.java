package com.example.prm392_labbooking.presentation.booking;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class BookingActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        dbHelper = new DatabaseHelper(this);

        Button btnAddToCart = findViewById(R.id.btn_add_to_cart);
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm phòng mẫu vào giỏ hàng
                CartItem item = new CartItem();
                item.setPackageName("Seat Package (4 seats)");
                item.setDetails("With whiteboard");
                item.setPrice(50.0);

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("package_name", item.getPackageName());
                values.put("details", item.getDetails());
                values.put("price", item.getPrice());
                db.insert("cart", null, values);
            }
        });
    }
}