package com.example.prm392_labbooking.presentation.cart;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.CartAdapter;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.presentation.product.ProductDetailsActivity;

import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private CartAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Lấy danh sách sản phẩm trong giỏ hàng
        List<Product> cartItems = CartManager.getInstance().getCart();

        // Khởi tạo adapter và truyền vào callback khi người dùng click item
        adapter = new CartAdapter(cartItems, (product, position) -> {
            Intent intent = new Intent(requireContext(), ProductDetailsActivity.class);
            intent.putExtra("product_name", product.getName());
            intent.putExtra("facilities", product.getFacilities());
            intent.putExtra("editIndex", position); // dùng để chỉnh sửa sản phẩm
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.notifyDataSetChanged(); // Làm mới danh sách khi quay lại Fragment
        }
    }
}
