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
        TextView txtProductName, txtProductPrice, txtFacilities, txtSlots, txtDate, txtCartError, txtRemainingTime;
        ImageButton btnRemoveItem, btnEditItem;

        public CartViewHolder(View view) {
            super(view);
            imgProduct = view.findViewById(R.id.imgProduct);
            txtProductName = view.findViewById(R.id.txtProductName);
            txtProductPrice = view.findViewById(R.id.txtProductPrice);
            txtFacilities = view.findViewById(R.id.txtFacilities);
            txtSlots = view.findViewById(R.id.txtSlots);
            txtDate = view.findViewById(R.id.txtDate);
            btnRemoveItem = view.findViewById(R.id.btnRemoveItem);
            txtCartError = view.findViewById(R.id.txtCartError);
            txtRemainingTime = view.findViewById(R.id.txtRemainingTime);
            btnEditItem = view.findViewById(R.id.btnEditItem);
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

        // Display product image
        holder.imgProduct.setImageResource(item.getProduct().getImageResId());

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
        String facilities;
        if (item.getFacilities() == null || item.getFacilities().isEmpty()) {
            facilities = holder.itemView.getContext().getString(R.string.none);
        } else {
            facilities = facilitiesBuilder.length() > 0
                ? facilitiesBuilder.substring(0, facilitiesBuilder.length() - 2)
                : holder.itemView.getContext().getString(R.string.none);
        }
        holder.txtFacilities.setText(facilities);

        // Display slots
        String mergedSlots = com.example.prm392_labbooking.utils.ValidationUtils.getMergedSlotDisplay(item.getSlots());
        holder.txtSlots.setText(mergedSlots);

        // Error and remaining time logic
        boolean expired = !com.example.prm392_labbooking.utils.ValidationUtils.isValidBookingTime(item.getDate(), item.getSlots());
        if (expired) {
            holder.txtCartError.setText(holder.itemView.getContext().getString(R.string.cart_item_expired));
            holder.txtCartError.setVisibility(View.VISIBLE);
        } else {
            holder.txtCartError.setText("");
            holder.txtCartError.setVisibility(View.GONE);
        }
        long remaining = com.example.prm392_labbooking.utils.ValidationUtils.getRemainingTimeUntilBooking(item.getDate(), item.getSlots());
        String remainingLabel = com.example.prm392_labbooking.utils.ValidationUtils.getLabelRelativeRemainingTime(holder.itemView.getContext(), remaining);
        holder.txtRemainingTime.setText(holder.itemView.getContext().getString(R.string.remaining_time) + ": " + remainingLabel);
        holder.txtRemainingTime.setVisibility(View.VISIBLE);

        // Delete button
        holder.btnRemoveItem.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteItem(position);
            }
        });
        // Edit button
        holder.btnEditItem.setOnClickListener(v -> {
            android.content.Context context = holder.itemView.getContext();
            android.content.Intent intent = new android.content.Intent(context, com.example.prm392_labbooking.presentation.product.ProductDetailsActivity.class);
            intent.putExtra("product_name", item.getProduct().getName());
            intent.putExtra("product_price", item.getProduct().getPrice());
            intent.putExtra("product_number", item.getProduct().getNumber());
            intent.putExtra("product_imageResId", item.getProduct().getImageResId());
            intent.putExtra("editIndex", position);
            // Booking info
            SimpleDateFormat bookingSdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            intent.putExtra("booking_date", bookingSdf.format(item.getDate()));
            // Serialize slots as comma-separated ordinals
            StringBuilder slotOrdinals = new StringBuilder();
            for (Slot s : item.getSlots()) {
                slotOrdinals.append(s.ordinal()).append(",");
            }
            if (slotOrdinals.length() > 0) slotOrdinals.setLength(slotOrdinals.length() - 1);
            intent.putExtra("booking_slots", slotOrdinals.toString());
            // Serialize facilities as comma-separated names
            StringBuilder facilityNames = new StringBuilder();
            for (Facility f : item.getFacilities()) {
                facilityNames.append(f.name()).append(",");
            }
            if (facilityNames.length() > 0) facilityNames.setLength(facilityNames.length() - 1);
            intent.putExtra("booking_facilities", facilityNames.toString());
            context.startActivity(intent);
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