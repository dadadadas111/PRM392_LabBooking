package com.example.prm392_labbooking.presentation.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment extends Fragment {
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);
        if (root instanceof FrameLayout) {
            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.GONE);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = android.view.Gravity.CENTER;
            ((FrameLayout) root).addView(progressBar, params);
        }
        return root;
    }

    protected void showLoading() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    protected void hideLoading() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }
}
