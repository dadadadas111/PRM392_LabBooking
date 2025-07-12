package com.example.prm392_labbooking.domain.model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onBindViewHolder(@NonNull BillingViewHolder holder, int position) {
        CartItem item = items.get(position);
        holder.tvRoomId.setText(item.getProduct().getName());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(item.getDate());
        holder.tvDate.setText(formattedDate);
        // Use merged slot display logic from ValidationUtils
        String mergedSlots = com.example.prm392_labbooking.utils.ValidationUtils.getMergedSlotDisplay(item.getSlots());
        holder.tvTimeSlot.setText(mergedSlots);
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
        holder.tvFeatures.setText(facilities);
        // Use string resource for price label
        holder.tvPrice.setText(holder.itemView.getContext().getString(R.string.product_price_label, item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}