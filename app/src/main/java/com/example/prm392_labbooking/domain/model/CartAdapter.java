package com.example.prm392_labbooking.domain.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.Product;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    public interface OnItemClickListener {
        void onClick(Product product, int position);
    }

    private final List<Product> cartItems;
    private final OnItemClickListener listener;

    public CartAdapter(List<Product> cartItems, OnItemClickListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtFacilities, txtPrice;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtFacilities = itemView.findViewById(R.id.txtFacilities);
            txtPrice = itemView.findViewById(R.id.txtPrice); // Ensure this ID exists in item_product.xml
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Product product = cartItems.get(position);
        holder.txtName.setText(product.getName());
        holder.txtFacilities.setVisibility(View.VISIBLE);
        holder.txtFacilities.setText("Facilities: " + product.getFacilities());
        holder.txtPrice.setText(String.format("Price: $%.2f", product.getPrice()));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(product, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }
}