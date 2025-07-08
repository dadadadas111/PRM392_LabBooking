package com.example.prm392_labbooking.presentation.cart;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.CartAdapter;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.services.CartManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private CartManager cartManager;
    private List<CartItem> cartList;

    private TextView txtSubtotal, txtTax, txtTotal;
    private Button btnLoadSample, btnCheckout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Ánh xạ view
        recyclerCart = view.findViewById(R.id.recyclerCartItems);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtTax = view.findViewById(R.id.txtTax);
        txtTotal = view.findViewById(R.id.txtTotal);
//        btnLoadSample = view.findViewById(R.id.btnLoadSample);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        cartManager = new CartManager(requireContext());
        cartList = cartManager.getCartItems();
//        cartList = cartManager.sampleCartItems();
        // Adapter xử lý xoá từng item
        adapter = new CartAdapter(cartList, position -> {
            cartList.remove(position);
            cartManager.saveCartItems(cartList); // Cập nhật SharedPreferences
            adapter.notifyItemRemoved(position);
            updateSummary();
        });

        if (cartList.isEmpty()){
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f); // visually indicate disabled
            android.widget.Toast.makeText(requireContext(), getString(R.string.cart_empty_message), android.widget.Toast.LENGTH_SHORT).show();
        }

        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(adapter);

        // Nút load sample data
//        btnLoadSample.setOnClickListener(v -> {
//            cartManager.addSampleCartItems();
//            cartList.clear();
//            cartList.addAll(cartManager.getCartItems());
//            adapter.notifyDataSetChanged();
//            updateSummary();
//        });

        // Chuyển danh sách product thành JSON và đẩy sang Billing
        btnCheckout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String cartItemsJson = gson.toJson(cartList);
            bundle.putString("cartItemsJson", cartItemsJson);
            NavigationManager.showBilling(requireActivity().getSupportFragmentManager(), bundle);
        });

        updateSummary();
        return view;
    }

    private void updateSummary() {
        double subtotal = 0.0;
        for (CartItem item : cartList) {
            subtotal += item.getPrice();
        }

        double taxRate = 0.08;
        double tax = subtotal * taxRate;
        double total = subtotal + tax;

        txtSubtotal.setText(String.format("$%.2f", subtotal));
        txtTax.setText(String.format("$%.2f", tax));
        txtTotal.setText(String.format("$%.2f", total));
    }
}
