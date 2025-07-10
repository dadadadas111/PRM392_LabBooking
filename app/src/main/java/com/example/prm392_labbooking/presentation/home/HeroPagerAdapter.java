package com.example.prm392_labbooking.presentation.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_labbooking.R;

import java.util.List;

public class HeroPagerAdapter extends RecyclerView.Adapter<HeroPagerAdapter.HeroViewHolder> {
    public static class HeroCard {
        public final String title;
        public final String subtitle;
        public HeroCard(String title, String subtitle ) {
            this.title = title;
            this.subtitle = subtitle;
        }
    }
    private final List<HeroCard> cards;
    private final Context context;
    public HeroPagerAdapter(Context context, List<HeroCard> cards) {
        this.context = context;
        this.cards = cards;
    }
    @NonNull
    @Override
    public HeroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hero_card, parent, false);
        return new HeroViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull HeroViewHolder holder, int position) {
        HeroCard card = cards.get(position);
        holder.title.setText(card.title);
        holder.subtitle.setText(card.subtitle);
    }
    @Override
    public int getItemCount() {
        return cards.size();
    }
    static class HeroViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        HeroViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.hero_title);
            subtitle = itemView.findViewById(R.id.hero_subtitle);
        }
    }
}
