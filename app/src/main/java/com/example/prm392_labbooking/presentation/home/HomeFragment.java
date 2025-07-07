package com.example.prm392_labbooking.presentation.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.domain.model.ProductAdapter;
import com.example.prm392_labbooking.presentation.product.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        // Danh sách sản phẩm
        productList = new ArrayList<>();
        productList.add(new Product("One Sheet",40,5,R.drawable.seat1));
        productList.add(new Product("Tow Sheet",50,6,R.drawable.seat2));
        productList.add(new Product("Three Sheet",60,7,R.drawable.seat3));
        productList.add(new Product("Four Sheet",70,8,R.drawable.seat4));
        productList.add(new Product("Five Sheet",80,9,R.drawable.seat5));
        productList.add(new Product("Six Sheet",90,10,R.drawable.seat6));
        productList.add(new Product("Table 4-Sheet",100,11,R.drawable.seat7));
        productList.add(new Product("Table 6-Sheet",110,12,R.drawable.seat8));
        productList.add(new Product("Table 12-Sheet",120,13,R.drawable.seat9));

        // Adapter có truyền listener để xử lý khi click
        adapter = new ProductAdapter(productList, (product, position) -> {
            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
            intent.putExtra("product_name", product.getName());
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        return view;
    }
}
