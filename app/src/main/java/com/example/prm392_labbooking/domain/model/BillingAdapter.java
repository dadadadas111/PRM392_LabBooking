package com.example.prm392_labbooking.domain.model;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;

import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.BillingViewHolder> {
    private final List<CartItem> items;

    public BillingAdapter(List<CartItem> items) {
        this.items = items;
    }

    public static class BillingViewHolder extends RecyclerView.ViewHolder {
        TextView tvRoomId, tvDate, tvTimeSlot, tvFeatures, tvPrice;

        public BillingViewHolder(View view) {
            super(view);
            tvRoomId = view.findViewById(R.id.tv_room_id);
            tvDate = view.findViewById(R.id.tv_date);
            tvTimeSlot = view.findViewById(R.id.tv_time_slot);
            tvFeatures = view.findViewById(R.id.tv_features);
            tvPrice = view.findViewById(R.id.tv_price);
        }
    }

    @NonNull
    @Override
    public BillingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_billing, parent, false);
        return new BillingViewHolder(v);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvRoomId.setText("Room: " + (item.getRoomId() != null ? item.getRoomId() : "N/A"));
        holder.tvDate.setText("Date: " + (item.getDate() != null ? item.getDate() : "N/A"));
        holder.tvTimeSlot.setText("Time: " + (item.getTimeSlot() != null ? item.getTimeSlot() : "N/A"));
        holder.tvFeatures.setText("Features: " + (item.getFeatures() != null ? String.join(", ", item.getFeatures()) : "None"));
        holder.tvPrice.setText(String.format("Price: $%.2f", item.getPrice() * (item.getQuantity() != 0 ? item.getQuantity() : 1)));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}