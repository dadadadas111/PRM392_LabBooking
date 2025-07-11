package com.example.prm392_labbooking.presentation.product;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.example.prm392_labbooking.utils.ValidationUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtProductName, txtPrice, txtDate, txtRemainingTime, txtErrorMessage;
    private CheckBox cbWhiteboard, cbTV, cbMicrophone, cbNetwork;
    private Button btnSelectSlots, btnAddToCart;
    private ImageButton btnBack;
    private LinearLayout timeslotList;
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
        txtRemainingTime = findViewById(R.id.txtRemainingTime);
        txtErrorMessage = findViewById(R.id.txtErrorMessage);
        cbWhiteboard = findViewById(R.id.cbWhiteboard);
        cbTV = findViewById(R.id.cbTV);
        cbMicrophone = findViewById(R.id.cbMicrophone);
        cbNetwork = findViewById(R.id.cbNetwork);
        btnSelectSlots = findViewById(R.id.btnSelectSlots);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBack = findViewById(R.id.btnBack);
        timeslotList = findViewById(R.id.timeslotList);

        // Get data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        double productPrice = intent.getDoubleExtra("product_price", 0.0);
        int productNumber = intent.getIntExtra("product_number", 0);
        int productImageResId = intent.getIntExtra("product_imageResId", R.drawable.ic_launcher_foreground);
        editIndex = intent.getIntExtra("editIndex", -1);

        // Initialize product with full info
        product = new Product(productName, productPrice, productNumber, productImageResId);

        // Backward compatible: booking info
        String bookingDateStr = intent.getStringExtra("booking_date");
        String bookingSlotsStr = intent.getStringExtra("booking_slots");
        String bookingFacilitiesStr = intent.getStringExtra("booking_facilities");
        if (bookingDateStr != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                selectedDate = sdf.parse(bookingDateStr);
                txtDate.setText(sdf.format(selectedDate));
            } catch (Exception e) { selectedDate = null; }
        }
        if (bookingSlotsStr != null && !bookingSlotsStr.isEmpty()) {
            selectedSlots = new ArrayList<>();
            String[] slotParts = bookingSlotsStr.split(",");
            for (String part : slotParts) {
                try {
                    int ordinal = Integer.parseInt(part);
                    selectedSlots.add(Slot.values()[ordinal]);
                } catch (Exception ignored) {}
            }
        }
        if (bookingFacilitiesStr != null && !bookingFacilitiesStr.isEmpty()) {
            selectedFacilities = new ArrayList<>();
            String[] facilityParts = bookingFacilitiesStr.split(",");
            for (String part : facilityParts) {
                try {
                    selectedFacilities.add(Facility.valueOf(part));
                } catch (Exception ignored) {}
            }
            // Set checkboxes
            cbWhiteboard.setChecked(selectedFacilities.contains(Facility.WHITE_BOARD));
            cbTV.setChecked(selectedFacilities.contains(Facility.TV));
            cbMicrophone.setChecked(selectedFacilities.contains(Facility.MICROPHONE));
            cbNetwork.setChecked(selectedFacilities.contains(Facility.NETWORK));
        }

        // Display product name
        txtProductName.setText(productName);
        // Optionally display image, price, etc. if you have UI elements
        // e.g. imgProduct.setImageResource(productImageResId);
        // txtPrice.setText(getString(R.string.total_label, productPrice));

        // Setup date picker
        txtDate.setOnClickListener(v -> showDatePickerDialog());

        // Setup slots selection dialog
        btnSelectSlots.setOnClickListener(v -> showSlotsSelectionDialog());

        // Update price and slots display based on initial state
        updatePrice();
        updateSelectedSlotsDisplay();
        updateRemainingTimeDisplay();

        // Add listeners to checkboxes
        setupListeners();

        // Handle Back button
        btnBack.setOnClickListener(v -> finish());

        // Handle Add to Cart button
        btnAddToCart.setOnClickListener(v -> {
            // Validate booking time using ValidationUtils
            if (!ValidationUtils.isValidBookingTime(selectedDate, selectedSlots)) {
                Toast.makeText(this, getString(R.string.error), Toast.LENGTH_SHORT).show();
                return;
            }

            if (selectedDate == null) {
                Toast.makeText(this, getString(R.string.choose_date), Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedSlots.isEmpty()) {
                Toast.makeText(this, getString(R.string.choose_time_slots), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
            } else {
                cartManager.addToCart(cartItem);
                Toast.makeText(this, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show();
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
                    reloadPriceAndSlots(); // Ensure price updates after date change
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
        builder.setTitle(getString(R.string.choose_time_slots));
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
        builder.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
            selectedSlots.clear();
            for (int i = 0; i < checkedItems.length; i++) {
                if (checkedItems[i]) {
                    selectedSlots.add(Slot.values()[i]);
                }
            }
            reloadPriceAndSlots(); // Ensure price updates after slot change
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updatePrice() {
        double totalPrice = calculatePrice();
        txtPrice.setText(getString(R.string.total_label, totalPrice));
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
        timeslotList.removeAllViews();
        if (selectedSlots.isEmpty()) {
            TextView tv = new TextView(this);
            tv.setText(getString(R.string.no_slots_selected));
            tv.setTextColor(getResources().getColor(R.color.colorError));
            timeslotList.addView(tv);
        } else {
            List<String> merged = ValidationUtils.getMergedSlotDisplayList(selectedSlots);
            for (String slotLabel : merged) {
                TextView tv = new TextView(this);
                tv.setText(slotLabel);
                tv.setTextColor(getResources().getColor(R.color.colorOnBackground));
                timeslotList.addView(tv);
            }
        }
    }

    private void setupListeners() {
        cbWhiteboard.setOnCheckedChangeListener((buttonView, isChecked) -> reloadPriceAndSlots());
        cbTV.setOnCheckedChangeListener((buttonView, isChecked) -> reloadPriceAndSlots());
        cbMicrophone.setOnCheckedChangeListener((buttonView, isChecked) -> reloadPriceAndSlots());
        cbNetwork.setOnCheckedChangeListener((buttonView, isChecked) -> reloadPriceAndSlots());
        btnSelectSlots.setOnClickListener(v -> showSlotsSelectionDialog());
    }

    private void reloadPriceAndSlots() {
        updatePrice();
        updateSelectedSlotsDisplay();
        updateRemainingTimeDisplay();
        updateAddToCartButtonState();
    }

    private void updateRemainingTimeDisplay() {
        long remaining = ValidationUtils.getRemainingTimeUntilBooking(selectedDate, selectedSlots);
        String label = ValidationUtils.getLabelRelativeRemainingTime(this, remaining);
        String text = getString(R.string.remaining_time) + ": " + label;
        txtRemainingTime.setText(text);
    }

    private void updateAddToCartButtonState() {
        boolean valid = selectedDate != null && selectedSlots != null && !selectedSlots.isEmpty() && ValidationUtils.isValidBookingTime(selectedDate, selectedSlots);
        btnAddToCart.setEnabled(valid);
        if (!valid) {
            txtErrorMessage.setText(getString(R.string.general_error_required_fields));
            txtErrorMessage.setVisibility(android.view.View.VISIBLE);
        } else {
            txtErrorMessage.setText("");
            txtErrorMessage.setVisibility(android.view.View.GONE);
        }
    }
}