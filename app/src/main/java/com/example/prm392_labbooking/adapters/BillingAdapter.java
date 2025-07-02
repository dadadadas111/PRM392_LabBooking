package com.example.prm392_labbooking.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.CartItem;
import java.util.List;

public class BillingAdapter extends RecyclerView.Adapter<BillingAdapter.ViewHolder> {
    private List<CartItem> cartItems;

    public BillingAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_billing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.tvPackageName.setText(item.getPackageName());
        holder.tvPackageDetails.setText(item.getDetails());
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPackageName, tvPackageDetails, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPackageName = itemView.findViewById(R.id.tv_package_name);
            tvPackageDetails = itemView.findViewById(R.id.tv_package_details);
            tvPrice = itemView.findViewById(R.id.tv_price);
        }
    }
}