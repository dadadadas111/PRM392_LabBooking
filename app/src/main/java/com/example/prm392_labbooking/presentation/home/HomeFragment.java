package com.example.prm392_labbooking.presentation.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.domain.model.Product;
import com.example.prm392_labbooking.domain.model.ProductAdapter;
import com.example.prm392_labbooking.presentation.product.ProductDetailsActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Handler;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // HERO CAROUSEL SETUP
        ViewPager2 heroViewPager = view.findViewById(R.id.heroViewPager);
        ImageButton btnPrev = view.findViewById(R.id.hero_btn_prev);
        ImageButton btnNext = view.findViewById(R.id.hero_btn_next);
        Handler heroHandler = new Handler();
        final int HERO_COUNT = 3;
        final int[] currentHero = {0};
        List<HeroPagerAdapter.HeroCard> heroCards = Arrays.asList(
            new HeroPagerAdapter.HeroCard(
                getString(R.string.hero_title),
                getString(R.string.hero_subtitle)
            ),
            new HeroPagerAdapter.HeroCard(
                getString(R.string.hero_title_2),
                getString(R.string.hero_subtitle_2)
            ),
            new HeroPagerAdapter.HeroCard(
                getString(R.string.hero_title_3),
                getString(R.string.hero_subtitle_3)
            )
        );
        HeroPagerAdapter heroAdapter = new HeroPagerAdapter(requireContext(), heroCards);
        heroViewPager.setAdapter(heroAdapter);
        heroViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentHero[0] = position;
            }
        });
        btnPrev.setOnClickListener(v -> {
            int prev = (currentHero[0] - 1 + HERO_COUNT) % HERO_COUNT;
            heroViewPager.setCurrentItem(prev, true);
        });
        btnNext.setOnClickListener(v -> {
            int next = (currentHero[0] + 1) % HERO_COUNT;
            heroViewPager.setCurrentItem(next, true);
        });
        // Auto-scroll every 4s
        Runnable heroAutoScroll = new Runnable() {
            @Override
            public void run() {
                int next = (currentHero[0] + 1) % HERO_COUNT;
                heroViewPager.setCurrentItem(next, true);
                heroHandler.postDelayed(this, 4000);
            }
        };
        heroHandler.postDelayed(heroAutoScroll, 4000);

        recyclerView = view.findViewById(R.id.recyclerViewProducts);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        final androidx.recyclerview.widget.LinearLayoutManager listLayoutManager = new androidx.recyclerview.widget.LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.product_grid_spacing_small)));

        // Toggle button logic
        final ImageButton btnToggleView = view.findViewById(R.id.btnToggleView);
        final int[] currentViewType = {ProductAdapter.VIEW_TYPE_GRID};
        btnToggleView.setImageResource(R.drawable.ic_view_list);

        btnToggleView.setOnClickListener(v -> {
            if (currentViewType[0] == ProductAdapter.VIEW_TYPE_GRID) {
                // Switch to list view
                currentViewType[0] = ProductAdapter.VIEW_TYPE_LIST;
                recyclerView.setLayoutManager(listLayoutManager);
                adapter.setViewType(ProductAdapter.VIEW_TYPE_LIST);
                btnToggleView.setImageResource(R.drawable.ic_view_grid);
            } else {
                // Switch to grid view
                currentViewType[0] = ProductAdapter.VIEW_TYPE_GRID;
                recyclerView.setLayoutManager(gridLayoutManager);
                adapter.setViewType(ProductAdapter.VIEW_TYPE_GRID);
                btnToggleView.setImageResource(R.drawable.ic_view_list);
            }
        });

//        // Cart summary logic
//        TextView cartSummary = view.findViewById(R.id.home_cart_summary);
//        CartManager cartManager = new CartManager(requireContext());
//        int cartCount = cartManager.getCartItems().size();
//        if (cartCount > 0) {
//            cartSummary.setText(getString(R.string.cart_summary, cartCount));
//            cartSummary.setVisibility(View.VISIBLE);
//        } else {
//            cartSummary.setVisibility(View.GONE);
//        }

        // Product list
        productList = new ArrayList<>();
        productList.add(new Product("product_1",40,5,R.drawable.seat1));
        productList.add(new Product("product_2",50,6,R.drawable.seat2));
        productList.add(new Product("product_3",60,7,R.drawable.seat3));
        productList.add(new Product("product_4",70,8,R.drawable.seat4));
        productList.add(new Product("product_5",80,9,R.drawable.seat5));
        productList.add(new Product("product_6",90,10,R.drawable.seat6));
        productList.add(new Product("product_7",100,11,R.drawable.seat7));
        productList.add(new Product("product_8",110,12,R.drawable.seat8));
        productList.add(new Product("product_9",120,13,R.drawable.seat9));

        adapter = new ProductAdapter(productList, (product, position) -> {
            Intent intent = new Intent(getContext(), ProductDetailsActivity.class);
            int nameResId = this.getResources().getIdentifier(product.getName(), "string", this.requireActivity().getPackageName());
            intent.putExtra("product_name", this.getString(nameResId));
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_number", product.getNumber());
            intent.putExtra("product_imageResId", product.getImageResId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Remove these lines, as hero_btn_left and hero_btn_right are now inside the ViewPager2 adapter
        // view.findViewById(R.id.hero_btn_left).setOnClickListener(v -> {
        //     recyclerView.post(() -> recyclerView.smoothScrollToPosition(0));
        // });
        // view.findViewById(R.id.hero_btn_right).setOnClickListener(v -> {
        //     recyclerView.post(() -> {
        //         recyclerView.smoothScrollToPosition(0);
        //         recyclerView.findViewHolderForAdapterPosition(0);
        //     });
        // });

        // Add bottom padding to avoid overlap with bottom navigation
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom() + getResources().getDimensionPixelSize(R.dimen.bottom_nav_height));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null) {
            android.view.View nav = getActivity().findViewById(R.id.bottom_navigation);
            if (nav instanceof com.google.android.material.bottomnavigation.BottomNavigationView) {
                ((com.google.android.material.bottomnavigation.BottomNavigationView) nav).setSelectedItemId(R.id.nav_home);
            }
        }
    }

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;
        public SpaceItemDecoration(int space) { this.space = space; }
        @Override
        public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) < 2) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }
}
