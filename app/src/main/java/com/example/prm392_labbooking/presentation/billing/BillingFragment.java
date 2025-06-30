package com.example.prm392_labbooking.presentation.billing;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.MainActivity;
import com.example.prm392_labbooking.adapters.BillingAdapter;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.usecase.booking.GetCartItemsUseCase;
import com.example.prm392_labbooking.domain.usecase.booking.SaveBookingUseCase;
import com.example.prm392_labbooking.utils.ValidationUtils;
import java.util.List;

public class BillingActivity extends AppCompatActivity {
    private RecyclerView rvBillingItems;
    private TextView tvTotalPrice;
    private EditText etCardholderName, etCardNumber, etExpiryDate, etCvv;
    private Button btnConfirmBooking;
    private DatabaseHelper dbHelper;
    private BillingAdapter adapter;
    private GetCartItemsUseCase getCartItemsUseCase;
    private SaveBookingUseCase saveBookingUseCase;
    private List<CartItem> cartItems;
    private double totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);

        // Initialize dependencies (mocked for simplicity)
        dbHelper = new DatabaseHelper(this);
        getCartItemsUseCase = new GetCartItemsUseCase(dbHelper);
        saveBookingUseCase = new SaveBookingUseCase(dbHelper);

        initViews();
        loadCartItems();
        setupRecyclerView();
        calculateTotalPrice();

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
    }

    private void initViews() {
        rvBillingItems = findViewById(R.id.rv_billing_items);
        tvTotalPrice = findViewById(R.id.tv_total_price);
        etCardholderName = findViewById(R.id.et_cardholder_name);
        etCardNumber = findViewById(R.id.et_card_number);
        etExpiryDate = findViewById(R.id.et_expiry_date);
        etCvv = findViewById(R.id.et_cvv);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
    }

    private void loadCartItems() {
        cartItems = getCartItemsUseCase.execute();
    }

    private void setupRecyclerView() {
        rvBillingItems.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BillingAdapter(cartItems);
        rvBillingItems.setAdapter(adapter);
    }

    private void calculateTotalPrice() {
        totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        tvTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void confirmBooking() {
        String name = etCardholderName.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().trim();
        String expiry = etExpiryDate.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();

        if (!ValidationUtils.isValidCardholderName(name)) {
            etCardholderName.setError("Enter a valid name");
            return;
        }
        if (!ValidationUtils.isValidCardNumber(cardNumber)) {
            etCardNumber.setError("Enter a 16-digit card number");
            return;
        }
        if (!ValidationUtils.isValidExpiryDate(expiry)) {
            etExpiryDate.setError("Enter a valid MM/YY date");
            return;
        }
        if (!ValidationUtils.isValidCvv(cvv)) {
            etCvv.setError("Enter a 3-digit CVV");
            return;
        }

        boolean success = saveBookingUseCase.execute(cartItems, totalPrice);
        if (success) {
            saveBookingUseCase.clearCart();
            Toast.makeText(this, "Booking confirmed successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}