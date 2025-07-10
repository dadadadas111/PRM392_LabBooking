package com.example.prm392_labbooking.domain.model;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final List<CartItem> cartList;
    private final OnCartActionListener listener;

    public interface OnCartActionListener {
        void onDeleteItem(int position);
    }

    public CartAdapter(List<CartItem> cartList, OnCartActionListener listener) {
        this.cartList = cartList;
        this.listener = listener;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice, txtFacilities, txtSlots, txtDate;
        ImageButton btnRemoveItem;

        public CartViewHolder(View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProduct);
            txtProductName = view.findViewById(R.id.txtProductName);
            txtProductPrice = view.findViewById(R.id.txtProductPrice);
            txtFacilities = view.findViewById(R.id.txtFacilities);
            txtSlots = view.findViewById(R.id.txtSlots);
            txtDate = view.findViewById(R.id.txtDate);
            btnRemoveItem = view.findViewById(R.id.btnRemoveItem);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartList.get(position);

        // Display product name and price
        holder.txtProductName.setText(item.getProduct().getName());
        holder.txtProductPrice.setText(String.format("$%.2f", item.getPrice()));

        // Image placeholder (since images were removed)


        // Display date
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(item.getDate());
        holder.txtDate.setText(formattedDate);

        // Display facilities
        StringBuilder facilitiesBuilder = new StringBuilder();
        for (Facility f : item.getFacilities()) {
            int resId = 0;
            switch (f) {
                case WHITE_BOARD:
                    resId = R.string.facility_white_board;
                    break;
                case TV:
                    resId = R.string.facility_tv;
                    break;
                case MICROPHONE:
                    resId = R.string.facility_microphone;
                    break;
                case NETWORK:
                    resId = R.string.facility_network;
                    break;
            }
            if (resId != 0) {
                facilitiesBuilder.append(holder.itemView.getContext().getString(resId)).append(", ");
            }
        }
        String facilities = facilitiesBuilder.length() > 0
                ? facilitiesBuilder.substring(0, facilitiesBuilder.length() - 2)
                : "";
        holder.txtFacilities.setText(facilities);

        // Display slots
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        StringBuilder slotBuilder = new StringBuilder();
        for (Slot s : item.getSlots()) {
            slotBuilder.append(s.getStart().format(formatter))
                    .append(" - ")
                    .append(s.getEnd().format(formatter))
                    .append(", ");
        }
        String slots = slotBuilder.length() > 0
                ? slotBuilder.substring(0, slotBuilder.length() - 2)
                : "";
        holder.txtSlots.setText(slots);

        // Delete button
        holder.btnRemoveItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void clearCart() {
        cartList.clear();
        notifyDataSetChanged();
    }
}