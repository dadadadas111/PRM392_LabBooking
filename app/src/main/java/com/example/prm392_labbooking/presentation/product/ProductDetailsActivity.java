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

    private TextView txtProductName, txtDuration;
    private CheckBox cbWhiteboard, cbTV, cbNetwork;
    private Button btnAddToCart;

    private int editIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        // Gán layout elements
        txtProductName = findViewById(R.id.txtProductName);
        txtDuration = findViewById(R.id.txtDuration);
        cbWhiteboard = findViewById(R.id.cbWhiteboard);
        cbTV = findViewById(R.id.cbTV);
        cbNetwork = findViewById(R.id.cbNetwork);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String productName = intent.getStringExtra("product_name");
        String selectedFacilities = intent.getStringExtra("facilities");
        editIndex = intent.getIntExtra("editIndex", -1); // Mặc định -1 nếu là thêm mới

        // Hiển thị dữ liệu
        txtProductName.setText(productName);
        txtDuration.setText("Duration: 2 hours"); // Có thể tùy chỉnh linh hoạt

        // Tích sẵn các tiện nghi đã chọn nếu có
        if (selectedFacilities != null) {
            cbWhiteboard.setChecked(selectedFacilities.contains("Whiteboard"));
            cbTV.setChecked(selectedFacilities.contains("TV"));
            cbNetwork.setChecked(selectedFacilities.contains("Network"));
        }

        // Xử lý khi nhấn nút Add to Cart
        btnAddToCart.setOnClickListener(v -> {
            StringBuilder facilities = new StringBuilder();
            if (cbWhiteboard.isChecked()) facilities.append("Whiteboard ");
            if (cbTV.isChecked()) facilities.append("TV ");
            if (cbNetwork.isChecked()) facilities.append("Network ");

            // Tạo đối tượng sản phẩm
            Product product = new Product(productName);
            product.setFacilities(facilities.toString().trim());

            if (editIndex >= 0) {
                CartManager.getInstance().updateProduct(editIndex, product);
                Toast.makeText(this, "Updated in cart!", Toast.LENGTH_SHORT).show();
            } else {
                CartManager.getInstance().addToCart(product);
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
            }

            finish(); // Quay lại màn trước đó (CartFragment)
        });
    }
}
