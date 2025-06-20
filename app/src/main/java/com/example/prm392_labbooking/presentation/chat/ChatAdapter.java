package com.example.prm392_labbooking.presentation.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_message, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class ChatViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvMessageLeft;
        private final TextView tvMessageRight;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessageLeft = itemView.findViewById(R.id.tv_message_left);
            tvMessageRight = itemView.findViewById(R.id.tv_message_right);
        }
        public void bind(ChatMessage message) {
            if (message.isUser()) {
                tvMessageLeft.setVisibility(View.GONE);
                tvMessageRight.setVisibility(View.VISIBLE);
                tvMessageRight.setText(message.getMessage());
            } else {
                tvMessageRight.setVisibility(View.GONE);
                tvMessageLeft.setVisibility(View.VISIBLE);
                tvMessageLeft.setText(message.getMessage());
            }
        }
    }
}
