package com.example.prm392_labbooking.presentation.chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;
import com.example.prm392_labbooking.utils.GeminiChatUtil;
import com.example.prm392_labbooking.utils.SecretLoader;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.repository.ChatRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.ChatRepository;
import com.example.prm392_labbooking.data.firebase.FirebaseChatServiceImpl;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
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
    private ProgressDialog progressDialog;
    private ChatRepository chatRepository;
    private FirebaseAuthService authService;
    private String userId;

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
        ImageButton btnMenu = findViewById(R.id.btn_menu);

        // Set initial tab state
        btnChatbot.setSelected(true);
        btnSupport.setSelected(false);
        btnChatbot.setTextColor(getColor(R.color.colorPrimary));
        btnSupport.setTextColor(0xFFB0B8C1);

        chatAdapter = new ChatAdapter(chatMessages);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        rvChat.setAdapter(chatAdapter);

        // Init repository and auth
        chatRepository = new ChatRepositoryImpl(new FirebaseChatServiceImpl());
        authService = new FirebaseAuthService();
        userId = authService.getCurrentUserId();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading chat history...");
        progressDialog.setCancelable(false);

        // Load chat history for chatbot tab on start
        if (isChatbot) {
            loadChatHistory();
        }

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
        btnMenu.setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(this, btnMenu);
            popup.getMenu().add(getString(R.string.menu_clear_chat));
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals(getString(R.string.menu_clear_chat))) {
                    new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.dialog_clear_chat_title))
                        .setMessage(getString(R.string.dialog_clear_chat_message))
                        .setPositiveButton(getString(R.string.dialog_yes), (dialog, which) -> {
                            if (isChatbot) {
                                chatMessages.clear();
                                chatAdapter.notifyDataSetChanged();
                                saveChatHistory();
                                Toast.makeText(this, getString(R.string.menu_clear_chat) + "d", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), null)
                        .show();
                }
                return true;
            });
            popup.show();
        });
    }

    private void loadChatHistory() {
        if (userId == null || userId.isEmpty()) return;
        progressDialog.show();
        chatRepository.loadChatHistory(userId, new ChatRepository.ChatHistoryListener() {
            @Override
            public void onHistoryLoaded(List<ChatMessage> messages) {
                progressDialog.dismiss();
                chatMessages.clear();
                chatMessages.addAll(messages);
                chatAdapter.notifyDataSetChanged();
                if (!chatMessages.isEmpty()) {
                    rvChat.scrollToPosition(chatMessages.size() - 1);
                }
            }
            @Override
            public void onError(Exception e) {
                progressDialog.dismiss();
                Toast.makeText(ChatActivity.this, "Failed to load chat history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChatHistory() {
        if (userId == null || userId.isEmpty()) return;
        chatRepository.saveChatHistory(userId, chatMessages, new ChatRepository.SaveListener() {
            @Override
            public void onSaved() {
                // Optionally show a small indicator or log
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ChatActivity.this, "Failed to save chat history", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void switchToChatbot() {
        isChatbot = true;
        loadChatHistory();
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
            chatMessages.add(new ChatMessage("...", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
            GeminiChatUtil.streamGeminiResponse(this, chatMessages.subList(0, chatMessages.size() - 1), message, new GeminiChatUtil.GeminiStreamCallback() {
                @Override
                public void onStreamUpdate(String resp) {
                    chatMessages.set(chatMessages.size() - 1, new ChatMessage(resp, false));
                    chatAdapter.notifyItemChanged(chatMessages.size() - 1);
                    LinearLayoutManager layoutManager = (LinearLayoutManager) rvChat.getLayoutManager();
                    if (layoutManager != null) {
                        int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                        if (lastVisible == chatMessages.size() - 2 || lastVisible == chatMessages.size() - 1) {
                            rvChat.scrollToPosition(chatMessages.size() - 1);
                        }
                    }
                }
                @Override
                public void onError(String errorMsg) {
                    chatMessages.set(chatMessages.size() - 1, new ChatMessage(errorMsg, false));
                    chatAdapter.notifyItemChanged(chatMessages.size() - 1);
                }
                @Override
                public void onStreamComplete() {
                    saveChatHistory();
                }
            });
        } else {
            chatMessages.add(new ChatMessage("(Support will reply soon)", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }
    }
}
