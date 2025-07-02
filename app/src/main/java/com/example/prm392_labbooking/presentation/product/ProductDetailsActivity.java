package com.example.prm392_labbooking.presentation.product;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.presentation.cart.CartManager;

public class ProductDetailsActivity extends AppCompatActivity {

    private TextView txtProductName, txtDuration, txtPrice;
    private CheckBox cbWhiteboard, cbTV, cbNetwork;
    private Button btnAddToCart;
    private int editIndex = -1;
    private double basePrice = 50.0; // Base price
    private final double whiteboardPrice = 10.0;
    private final double tvPrice = 15.0;
    private final double networkPrice = 5.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Initialize layout elements
        txtProductName = findViewById(R.id.txtProductName);
        txtDuration = findViewById(R.id.txtDuration);
        txtPrice = findViewById(R.id.txtPrice); // Ensure this ID exists in activity_product_details.xml
        cbWhiteboard = findViewById(R.id.cbWhiteboard);
        cbTV = findViewById(R.id.cbTV);
        cbNetwork = findViewById(R.id.cbNetwork);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Get data from Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String selectedFacilities = intent.getStringExtra("facilities");
        editIndex = intent.getIntExtra("editIndex", -1);

        // Display data
        txtProductName.setText(productName);
        txtDuration.setText("Duration: 2 hours");

        // Check existing facilities
        if (selectedFacilities != null) {
            cbWhiteboard.setChecked(selectedFacilities.contains("Whiteboard"));
            cbTV.setChecked(selectedFacilities.contains("TV"));
            cbNetwork.setChecked(selectedFacilities.contains("Network"));
        }

        // Update price based on initial checkbox state
        updatePrice();

        // Add listeners to checkboxes to update price dynamically
        cbWhiteboard.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbTV.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());
        cbNetwork.setOnCheckedChangeListener((buttonView, isChecked) -> updatePrice());

        // Handle Add to Cart button
        btnAddToCart.setOnClickListener(v -> {
            StringBuilder facilities = new StringBuilder();
            if (cbWhiteboard.isChecked()) facilities.append("Whiteboard ");
            if (cbTV.isChecked()) facilities.append("TV ");
            if (cbNetwork.isChecked()) facilities.append("Network ");

            // Create product object
            Product product = new Product(productName);
            product.setFacilities(facilities.toString().trim());
            product.setPrice(calculatePrice());

            if (editIndex >= 0) {
                CartManager.getInstance().updateProduct(editIndex, product);
                Toast.makeText(this, "Updated in cart!", Toast.LENGTH_SHORT).show();
            } else {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            }

            finish();
        });
    }

    private void updatePrice() {
        double totalPrice = calculatePrice();
        txtPrice.setText(String.format("Price: $%.2f", totalPrice));
    }

    private double calculatePrice() {
        double totalPrice = basePrice;
        if (cbWhiteboard.isChecked()) totalPrice += whiteboardPrice;
        if (cbTV.isChecked()) totalPrice += tvPrice;
        if (cbNetwork.isChecked()) totalPrice += networkPrice;
        return totalPrice;
    }
}