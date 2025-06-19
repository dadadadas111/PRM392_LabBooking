package com.example.prm392_labbooking.presentation.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AuthRequiredActivity {
    private TextView btnChatbot, btnSupport;
    private EditText etMessage;
    private ImageButton btnSend;
    private RecyclerView rvChat;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages = new ArrayList<>();
    private boolean isChatbot = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        btnChatbot = findViewById(R.id.btn_chatbot);
        btnSupport = findViewById(R.id.btn_support);
        etMessage = findViewById(R.id.et_message);
        btnSend = findViewById(R.id.btn_send);
        rvChat = findViewById(R.id.rv_chat);

        // Set initial tab state
        btnChatbot.setSelected(true);
        btnSupport.setSelected(false);
        btnChatbot.setTextColor(getColor(R.color.colorPrimary));
        btnSupport.setTextColor(0xFFB0B8C1);

        chatAdapter = new ChatAdapter(chatMessages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        btnChatbot.setOnClickListener(v -> {
            if (!btnChatbot.isSelected()) {
                btnChatbot.setSelected(true);
                btnSupport.setSelected(false);
                btnChatbot.setTextColor(getColor(R.color.colorPrimary));
                btnSupport.setTextColor(0xFFB0B8C1);
                switchToChatbot();
            }
        });
        btnSupport.setOnClickListener(v -> {
            if (!btnSupport.isSelected()) {
                btnSupport.setSelected(true);
                btnChatbot.setSelected(false);
                btnSupport.setTextColor(getColor(R.color.colorPrimary));
                btnChatbot.setTextColor(0xFFB0B8C1);
                switchToSupport();
            }
        });
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void switchToChatbot() {
        isChatbot = true;
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();
    }

    private void switchToSupport() {
        isChatbot = false;
        chatMessages.clear();
        chatAdapter.notifyDataSetChanged();
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) return;
        chatMessages.add(new ChatMessage(message, true));
        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
        rvChat.scrollToPosition(chatMessages.size() - 1);
        etMessage.setText("");
        if (isChatbot) {
            // TODO: Call Gemini API and stream response
            chatMessages.add(new ChatMessage("(Gemini is thinking...)", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
        } else {
            // TODO: Integrate with real support
            chatMessages.add(new ChatMessage("(Support will reply soon)", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }
    }
}
