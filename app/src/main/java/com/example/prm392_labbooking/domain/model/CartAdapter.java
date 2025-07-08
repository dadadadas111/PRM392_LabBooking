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
import java.util.stream.Collectors;

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
        ImageView imgProduct;
        TextView txtProductName, txtProductPrice, txtFacilities, txtSlots,txtDate;
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
        CartItem item = items.get(position);

        // Hiển thị tên và giá sản phẩm
        holder.txtProductName.setText(item.getProduct().getName());
        holder.txtProductPrice.setText(String.format("$%.2f", item.getPrice()));

        // Nếu bạn có ảnh sản phẩm thực tế, xử lý tại đây
        // holder.imgProduct.setImageResource(...) hoặc dùng Glide/Picasso nếu ảnh URL

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(item.getDate());
        holder.txtDate.setText(formattedDate);


        // Hiển thị tiện ích (Facility enum)
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

        // Nút xóa
        holder.btnRemoveItem.setOnClickListener(v -> {
            if (listener != null) {
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
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }
}
