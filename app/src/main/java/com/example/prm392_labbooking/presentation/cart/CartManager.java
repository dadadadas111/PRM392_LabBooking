package com.example.prm392_labbooking.presentation.cart;

import com.example.prm392_labbooking.domain.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private final List<Product> cart;

    private CartManager() {
        cart = new ArrayList<>();
    }

    public static CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    // Thêm sản phẩm vào giỏ
    public void addToCart(Product product) {
        cart.add(product);
    }

    // Cập nhật sản phẩm (dùng khi sửa lại từ ProductDetailsActivity)
    public void updateProduct(int index, Product product) {
        if (index >= 0 && index < cart.size()) {
            cart.set(index, product);
        }
    }

    // Lấy toàn bộ sản phẩm trong giỏ
    public List<Product> getCart() {
        return cart;
    }

    // Xoá sản phẩm khỏi giỏ nếu cần
    public void removeFromCart(int index) {
        if (index >= 0 && index < cart.size()) {
            cart.remove(index);
        }
    }

    // Xoá tất cả
    public void clearCart() {
        cart.clear();
    }
}
