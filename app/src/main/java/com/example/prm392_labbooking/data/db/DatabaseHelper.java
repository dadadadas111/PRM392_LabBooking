package com.example.prm392_labbooking.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lab_booking.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CART = "cart";
    private static final String TABLE_BOOKINGS = "bookings";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "package_name TEXT, " +
                "details TEXT, " +
                "price REAL)";
        String createBookingsTable = "CREATE TABLE " + TABLE_BOOKINGS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "package_name TEXT, " +
                "details TEXT, " +
                "price REAL, " +
                "timestamp TEXT)";
        db.execSQL(createCartTable);
        db.execSQL(createBookingsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKINGS);
        onCreate(db);
    }

    public List<CartItem> getCartItems() {
        List<CartItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            CartItem item = new CartItem();
            // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//            item.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
//            item.setPackageName(cursor.getString(cursor.getColumnIndexOrThrow("package_name")));
//            item.setDetails(cursor.getString(cursor.getColumnIndexOrThrow("details")));
            item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
            items.add(item);
        }
        cursor.close();
        return items;
    }

    public boolean saveBooking(List<CartItem> items, double totalPrice) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (CartItem item : items) {
                ContentValues values = new ContentValues();
                // TODO for Son: Sửa lại CartItem cho đúng logic với Đông.
//                values.put("package_name", item.getPackageName());
//                values.put("details", item.getDetails());
                values.put("price", item.getPrice());
                values.put("timestamp", System.currentTimeMillis() + "");
                db.insert(TABLE_BOOKINGS, null, values);
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public void clearCart() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CART, null, null);
    }
}