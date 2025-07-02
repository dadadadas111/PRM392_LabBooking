package com.example.prm392_labbooking.presentation.billing;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.adapters.BillingAdapter;
import com.example.prm392_labbooking.data.db.DatabaseHelper;
import com.example.prm392_labbooking.domain.model.CartItem;
import com.example.prm392_labbooking.domain.usecase.booking.GetCartItemsUseCase;
import com.example.prm392_labbooking.domain.usecase.booking.SaveBookingUseCase;
import com.example.prm392_labbooking.utils.ValidationUtils;
import java.util.List;

public class BillingFragment extends Fragment {
    private RecyclerView rvBillingItems;
    private TextView tvTotalPrice;
    private EditText etCardholderName, etCardNumber, etExpiryDate, etCvv;
    private Button btnConfirmBooking;
    private DatabaseHelper dbHelper;
    private BillingAdapter adapter;
    private GetCartItemsUseCase getCartItemsUseCase;
    private SaveBookingUseCase saveBookingUseCase;
    private List<CartItem> cartItems;
    private double totalPrice;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_billing, container, false);

        // Hide bottom navigation
        if (getActivity() instanceof com.example.prm392_labbooking.presentation.MainActivity) {
            ((com.example.prm392_labbooking.presentation.MainActivity) getActivity()).hideBottomNavigation();
        }

        dbHelper = new DatabaseHelper(requireContext());
        getCartItemsUseCase = new GetCartItemsUseCase(dbHelper);
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
    }

    private void loadCartItems() {
        cartItems = getCartItemsUseCase.execute();
    }

    private void setupRecyclerView() {
        rvBillingItems.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new BillingAdapter(cartItems);
        rvBillingItems.setAdapter(adapter);
    }

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
            etExpiryDate.setError("Enter a valid MM/YY date");
            return;
        }
        if (!ValidationUtils.isValidCvv(cvv)) {
            etCvv.setError("Enter a 3-digit CVV");
            return;
        }

        boolean success = saveBookingUseCase.execute(cartItems, totalPrice);
        if (success) {
            saveBookingUseCase.clearCart();
            Toast.makeText(requireContext(), "Booking confirmed successfully!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack(); // Quay lại Cart hoặc Home
        } else {
            Toast.makeText(requireContext(), "Booking failed. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}