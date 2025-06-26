package com.example.prm392_labbooking.domain.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private List<Product> productList;

    // Constructor
    public ProductAdapter(List<Product> productList) {
        this.productList = productList;
    }

    // ViewHolder (định nghĩa 1 item)
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
        }
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_product.xml
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Gán dữ liệu vào từng item
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtName.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "You Click: " + product.getName(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}
