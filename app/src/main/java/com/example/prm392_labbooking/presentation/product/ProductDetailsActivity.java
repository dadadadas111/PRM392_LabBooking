package com.example.prm392_labbooking.presentation.product;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.model.Facility;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.domain.model.Slot;
import com.example.prm392_labbooking.presentation.cart.CartManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtProductName, txtPrice, txtDate, txtSelectedSlots;
    private CheckBox cbWhiteboard, cbTV, cbMicrophone, cbNetwork;
    private Button btnSelectSlots, btnAddToCart;
    private ImageButton btnBack;
    private int editIndex = -1;
    private Product product;
    private List<Slot> selectedSlots = new ArrayList<>();
    private List<Facility> selectedFacilities = new ArrayList<>();
    private Date selectedDate;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize layout elements
        txtProductName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtPrice);
        txtDate = findViewById(R.id.txtDate);
        txtSelectedSlots = findViewById(R.id.txtSelectedSlots);
        cbWhiteboard = findViewById(R.id.cbWhiteboard);
        cbTV = findViewById(R.id.cbTV);
        cbMicrophone = findViewById(R.id.cbMicrophone);
        cbNetwork = findViewById(R.id.cbNetwork);
        btnSelectSlots = findViewById(R.id.btnSelectSlots);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        editIndex = intent.getIntExtra("editIndex", -1);

        // Initialize product
        product = new Product(productName);

        // Display product name
        txtProductName.setText(productName);

        // Setup date picker
        txtDate.setOnClickListener(v -> showDatePickerDialog());

        // Setup slots selection dialog
        btnSelectSlots.setOnClickListener(v -> showSlotsSelectionDialog());

        // Update price and slots display based on initial state
        updatePrice();
        updateSelectedSlotsDisplay();

        // Add listeners to checkboxes
        cbWhiteboard.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbTV.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbMicrophone.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbNetwork.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());

        // Handle Back button
        btnBack.setOnClickListener(v -> finish());

        // Handle Add to Cart button
        btnAddToCart.setOnClickListener(v -> {
            if (selectedDate == null) {
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSlots.isEmpty()) {
                Toast.makeText(this, "Please select at least one time slot", Toast.LENGTH_SHORT).show();
                return;
            }

            // Get selected facilities
            selectedFacilities.clear();
            if (cbWhiteboard.isChecked()) selectedFacilities.add(Facility.WHITE_BOARD);
            if (cbTV.isChecked()) selectedFacilities.add(Facility.TV);
            if (cbMicrophone.isChecked()) selectedFacilities.add(Facility.MICROPHONE);
            if (cbNetwork.isChecked()) selectedFacilities.add(Facility.NETWORK);

            // Create CartItem
            CartItem cartItem = new CartItem(
                    product,
                    selectedFacilities,
                    selectedSlots,
                    selectedDate,
                    calculatePrice()
            );

            // Add or update cart
            CartManager cartManager = CartManager.getInstance(this);
            if (editIndex >= 0) {
                cartManager.updateCartItem(editIndex, cartItem);
                Toast.makeText(this, "Updated in cart!", Toast.LENGTH_SHORT).show();
            } else {
                cartManager.addToCart(cartItem);
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    Calendar selectedCalendar = Calendar.getInstance();
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    selectedDate = selectedCalendar.getTime();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    txtDate.setText(sdf.format(selectedDate));
                }, year, month, day);

        // Restrict past dates
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showSlotsSelectionDialog() {
        // Convert Slot enum to CharSequence array for display
        CharSequence[] slotNames = new CharSequence[Slot.values().length];
        for (int i = 0; i < Slot.values().length; i++) {
            slotNames[i] = Slot.values()[i].getFormattedTime();
        }

        // Initialize checked items based on current selection
        boolean[] checkedItems = new boolean[Slot.values().length];
        for (int i = 0; i < Slot.values().length; i++) {
            checkedItems[i] = selectedSlots.contains(Slot.values()[i]);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Time Slots");
        builder.setMultiChoiceItems(
                slotNames,
                checkedItems,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                }
        );
        builder.setPositiveButton("OK", (dialog, which) -> {
            selectedSlots.clear();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selectedSlots.add(Slot.values()[i]);
                }
            }
            updatePrice();
            updateSelectedSlotsDisplay();
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePrice() {
        double totalPrice = calculatePrice();
        txtPrice.setText(String.format("Price: $%.2f", totalPrice));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private double calculatePrice() {
        double totalPrice = product.getPrice();

        // Multiply by number of slots
        int slotCount = selectedSlots.size();
        totalPrice *= slotCount;

        // Add facilities prices
        if (cbWhiteboard.isChecked()) totalPrice += Facility.WHITE_BOARD.getPrice();
        if (cbTV.isChecked()) totalPrice += Facility.TV.getPrice();
        if (cbMicrophone.isChecked()) totalPrice += Facility.MICROPHONE.getPrice();
        if (cbNetwork.isChecked()) totalPrice += Facility.NETWORK.getPrice();

        return totalPrice;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateSelectedSlotsDisplay() {
        if (selectedSlots.isEmpty()) {
            txtSelectedSlots.setText("No slots selected");
        } else {
            String slotsText = selectedSlots.stream()
                    .map(Slot::getFormattedTime)
                    .collect(Collectors.joining(", "));
            txtSelectedSlots.setText(slotsText);
        }
    }
}