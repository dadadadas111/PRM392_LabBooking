package com.example.prm392_labbooking.domain.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    public static final int VIEW_TYPE_GRID = 0;
    public static final int VIEW_TYPE_LIST = 1;
    private int viewType = VIEW_TYPE_GRID;

    public interface OnProductClickListener {
        void onProductClick(Product product, int position);
    }

    private final List<Product> productList;
    private final OnProductClickListener listener;

    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice, txtNumber;
        ImageView imgProduct;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtNumber = itemView.findViewById(R.id.txtNumber);
            imgProduct = itemView.findViewById(R.id.imgProduct);
        }
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layout = (viewType == VIEW_TYPE_LIST) ? R.layout.item_product_list : R.layout.item_product;
        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.txtName.setText(product.getName());
        holder.txtPrice.setText("Price: " + String.valueOf(product.getPrice()));
        holder.txtNumber.setText("Quantity: " + product.getNumber());
        holder.imgProduct.setImageResource(product.getImageResId());
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }
}