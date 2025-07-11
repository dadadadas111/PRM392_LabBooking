package com.example.prm392_labbooking.presentation.cart;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.CartAdapter;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.navigation.NavigationManager;
import com.example.prm392_labbooking.notifications.CartExpiryScheduler;
import com.example.prm392_labbooking.utils.ValidationUtils;
import com.google.gson.Gson;

import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView recyclerCart;
    private CartAdapter adapter;
    private CartManager cartManager;
    private List<CartItem> cartList;
    private TextView txtSubtotal, txtTax, txtTotal;
    private Button btnCheckout;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        // Initialize views
        recyclerCart = view.findViewById(R.id.recyclerCartItems);
        txtSubtotal = view.findViewById(R.id.txtSubtotal);
        txtTax = view.findViewById(R.id.txtTax);
        txtTotal = view.findViewById(R.id.txtTotal);
        btnCheckout = view.findViewById(R.id.btnCheckout);

        // Initialize CartManager and cart list
        cartManager = CartManager.getInstance(requireContext());
        cartList = cartManager.getCartItems();

        // Set up adapter with delete action
        adapter = new CartAdapter(cartList, position -> {
            CartItem item = cartList.get(position);
            CartExpiryScheduler.cancelExpiryAlarms(requireContext(), item, position);
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.remove))
                .setMessage(getString(R.string.cart_remove_confirm))
                .setPositiveButton(getString(R.string.ok), (dialog, which) -> {
                    cartManager.removeFromCart(position); // Remove from CartManager and save
                    cartList.clear();
                    cartList.addAll(cartManager.getCartItems()); // Sync cartList with CartManager
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position, cartList.size()); // Update remaining items
                    updateSummary();
                })
                .setNegativeButton(getString(R.string.cancel), null)
                .show();
        });

        // Disable checkout button if cart is empty
        if (cartList.isEmpty()) {
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f);
            Toast.makeText(requireContext(), getString(R.string.cart_empty_message), Toast.LENGTH_SHORT).show();
        } else {
            btnCheckout.setEnabled(true);
            btnCheckout.setAlpha(1.0f);
        }

        recyclerCart.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCart.setAdapter(adapter);

        // Handle checkout
        btnCheckout.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            Gson gson = new Gson();
            String cartItemsJson = gson.toJson(cartList);
            bundle.putString("cartItemsJson", cartItemsJson);
            NavigationManager.showBilling(requireActivity().getSupportFragmentManager(), bundle);
        });

        updateSummary();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            View nav = getActivity().findViewById(R.id.bottom_navigation);
            if (nav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) nav).setSelectedItemId(R.id.nav_cart);
            }
        }
        // Refresh cart list and UI
        cartList.clear();
        cartList.addAll(cartManager.getCartItems());
        adapter.notifyDataSetChanged();
        updateSummary();

        // Push notification for all cart items when fragment is resumed
//        for (int i = 0; i < cartList.size(); i++) {
//            CartExpiryScheduler.pushImmediateNotification(requireContext(), cartList.get(i), i);
//        }
    }

    private void updateSummary() {
        double subtotal = 0.0;
        boolean hasExpired = false;
        for (CartItem item : cartList) {
            subtotal += item.getPrice();
            if (!ValidationUtils.isValidBookingTime(item.getDate(), item.getSlots())) {
                item.setError(getString(R.string.cart_item_expired));
                hasExpired = true;
            } else {
                item.setError("");
            }
        }

        double taxRate = 0.08;
        double tax = subtotal * taxRate;
        double total = subtotal + tax;

        txtSubtotal.setText(String.format("$%.2f", subtotal));
        txtTax.setText(String.format("$%.2f", tax));
        txtTotal.setText(String.format("$%.2f", total));

        // Update checkout button state
        if (cartList.isEmpty() || hasExpired) {
            btnCheckout.setEnabled(false);
            btnCheckout.setAlpha(0.5f);
        } else {
            btnCheckout.setEnabled(true);
            btnCheckout.setAlpha(1.0f);
        }
        adapter.notifyDataSetChanged();

        // After adding/editing cart items, schedule expiry alarms
        for (int i = 0; i < cartList.size(); i++) {
            CartExpiryScheduler.scheduleExpiryAlarms(requireContext(), cartList.get(i), i);
        }
    }
}