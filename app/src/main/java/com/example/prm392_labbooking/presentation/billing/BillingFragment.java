package com.example.prm392_labbooking.presentation.billing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.BillingAdapter;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.usecase.booking.SaveBookingUseCase;
import com.example.prm392_labbooking.presentation.cart.CartFragment;
import com.example.prm392_labbooking.services.CartManager;
import com.example.prm392_labbooking.utils.ValidationUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class BillingFragment extends Fragment {
    private RecyclerView rvBillingItems;
    private TextView tvTotalPrice;
    private EditText etCardholderName, etCardNumber, etExpiryDate, etCvv;
    private Button btnConfirmBooking;
    private ImageButton btnBack;
    private List<CartItem> cartItems;
    private double totalPrice;
    private SaveBookingUseCase saveBookingUseCase;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        DatabaseHelper dbHelper = new DatabaseHelper(requireContext());
        saveBookingUseCase = new SaveBookingUseCase(dbHelper);

        initViews(view);
        loadCartItems();
        setupRecyclerView();
        calculateTotalPrice();

        btnConfirmBooking.setOnClickListener(v -> confirmBooking());
        return view;
    }

    private void initViews(View view) {
        rvBillingItems = view.findViewById(R.id.rv_billing_items);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);
        etCardholderName = view.findViewById(R.id.et_cardholder_name);
        etCardNumber = view.findViewById(R.id.et_card_number);
        etExpiryDate = view.findViewById(R.id.et_expiry_date);
        etCvv = view.findViewById(R.id.et_cvv);
        btnConfirmBooking = view.findViewById(R.id.btn_confirm_booking);
        btnBack = view.findViewById(R.id.btn_back);
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> {
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        });
    }

    private void loadCartItems() {
        Bundle args = getArguments();
        if (args != null) {
            String cartItemsJson = args.getString("cartItemsJson");
            if (cartItemsJson != null) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<CartItem>>() {}.getType();
                cartItems = gson.fromJson(cartItemsJson, type);
            }
        }
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
    }

    private void setupRecyclerView() {
        rvBillingItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        BillingAdapter adapter = new BillingAdapter(cartItems);
        rvBillingItems.setAdapter(adapter);
    }

    @SuppressLint("DefaultLocale")
    private void calculateTotalPrice() {
        totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice();
        }
        tvTotalPrice.setText(String.format("Total: $%.2f", totalPrice));
    }

    private void confirmBooking() {
        String name = etCardholderName.getText().toString().trim();
        String cardNumber = etCardNumber.getText().toString().trim();
        String expiry = etExpiryDate.getText().toString().trim();
        String cvv = etCvv.getText().toString().trim();

        if (!ValidationUtils.isValidCardholderName(name)) {
            etCardholderName.setError("Enter a valid name");
            return;
        }
        if (!ValidationUtils.isValidCardNumber(cardNumber)) {
            etCardNumber.setError("Enter a 16-digit card number");
            return;
        }
        if (!ValidationUtils.isValidExpiryDate(expiry)) {
            etExpiryDate.setError("Enter valid MM/YY dates from this month to the future");
            return;
        }
        if (!ValidationUtils.isValidCvv(cvv)) {
            etCvv.setError("Enter a 3-digit CVV");
            return;
        }

        boolean success = saveBookingUseCase.execute(cartItems, totalPrice);
        if (success) {
            CartManager cartManager = new CartManager(requireContext());
            cartManager.clearCart(); // Xóa giỏ hàng từ SharedPreferences
            Toast.makeText(requireContext(), "Booking confirmed successfully!", Toast.LENGTH_SHORT).show();
            if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                getParentFragmentManager().popBackStack();
            }
        } else {
            Toast.makeText(requireContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Show bottom navigation when leaving BillingFragment
        if (getActivity() instanceof com.example.prm392_labbooking.presentation.MainActivity) {
            ((com.example.prm392_labbooking.presentation.MainActivity) getActivity()).showBottomNavigation();
        }
    }
}