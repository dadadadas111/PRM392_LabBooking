package com.example.prm392_labbooking.presentation.home;

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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        productList = new ArrayList<>();
        productList.add(new Product("One Sheet"));
        productList.add(new Product("Tow Sheet"));
        productList.add(new Product("Three Sheet"));
        productList.add(new Product("Four Sheet"));
        productList.add(new Product("Five Sheet"));
        productList.add(new Product("Six Sheet"));
        productList.add(new Product("Table 4-Sheet"));
        productList.add(new Product("Table 6-Sheet"));
        productList.add(new Product("Table 12-Sheet"));
        adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
