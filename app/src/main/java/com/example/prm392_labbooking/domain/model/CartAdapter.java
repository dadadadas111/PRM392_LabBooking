package com.example.prm392_labbooking.domain.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> items;
    private OnCartActionListener listener;

    public interface OnCartActionListener {
        void onDeleteItem(int position);
    }
    public CartAdapter(List<CartItem> items, OnCartActionListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoom, txtTime, txtFeature,txtPriceItem;
        ImageButton btnDeleteItem;
        public CartViewHolder(View view) {
            super(view);
            txtRoom = view.findViewById(R.id.txtRoom);
            txtTime = view.findViewById(R.id.txtTime);
            txtFeature = view.findViewById(R.id.txtFeature);
            txtPriceItem = view.findViewById(R.id.txtPriceItem);
            btnDeleteItem = view.findViewById(R.id.btnDeleteItem);
        }
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.txtRoom.setText("Room: " + item.getRoomId());
        holder.txtTime.setText(item.getDate() + " | " + item.getTimeSlot());
        holder.txtFeature.setText("Features: " + item.getFeatures());
        holder.txtPriceItem.setText(String.format("$%.2f", item.getPrice()));

        holder.btnDeleteItem.setOnClickListener(v -> {
            if(listener != null){
                listener.onDeleteItem(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
    public void clearCart() {
        items.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyItemRemoved(position);
    }
}
