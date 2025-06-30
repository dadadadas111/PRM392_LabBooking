package com.example.prm392_labbooking.presentation.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.navigation.NavigationManager;

public class CartFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Button btnPay = view.findViewById(R.id.btn_pay);
        btnPay.setOnClickListener(v -> NavigationManager.showBilling(getParentFragmentManager()));

        return view;
    }
}