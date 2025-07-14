package com.example.prm392_labbooking.presentation.chat;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.R;
import com.example.prm392_labbooking.data.firebase.FirebaseAuthService;
import com.example.prm392_labbooking.data.firebase.FirebaseChatServiceImpl;
import com.example.prm392_labbooking.data.repository.ChatRepositoryImpl;
import com.example.prm392_labbooking.domain.repository.ChatRepository;
import com.example.prm392_labbooking.presentation.base.AuthRequiredActivity;
import com.example.prm392_labbooking.services.MyMqttService;
import com.example.prm392_labbooking.utils.GeminiChatUtil;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttException;

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
    private ChatRepository chatRepository;
    private FirebaseAuthService authService;
    private String userId;
    private List<ChatMessage> cachedChatbotMessages = null;
    private List<ChatMessage> cachedSupportMessages = null;
    private MyMqttService mqttService;
    private final List<ChatMessage> supportMessages = new ArrayList<>();
    private String supportRequestTopic;
    private String supportResponseTopic;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        initLoadingOverlay();

        mqttService = new MyMqttService();
        new Thread(() -> mqttService.initialize()).start(); // Connect in background
        authService = new FirebaseAuthService();
        userId = authService.getCurrentUserId();
        supportRequestTopic = "support_chat/" + userId;
        supportResponseTopic = "support_responses/" + userId;

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
        supportRequestTopic = "support_chat/" + userId;
        supportResponseTopic = "support_responses/" + userId;

        // Load chat history for chatbot tab on start
        if (isChatbot) {
            loadChatHistory();
        } else {
            loadSupportHistory();
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
        btnSend.setOnClickListener(v -> {
            sendMessage();
            // Hide keyboard after sending
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
            }
        });
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
                                cachedChatbotMessages = null;
                                chatAdapter.notifyDataSetChanged();
                                saveChatHistory();
                                Toast.makeText(this, getString(R.string.menu_clear_chat), Toast.LENGTH_SHORT).show();
                            } else {
                                supportMessages.clear();
                                cachedSupportMessages = null;
                                chatMessages.clear();
                                chatAdapter.notifyDataSetChanged();
                                saveSupportHistory();
                                Toast.makeText(this, getString(R.string.menu_clear_chat), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton(getString(R.string.dialog_cancel), null)
                        .show();
                }
                return true;
            });
            popup.show();
        });
        etMessage.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                sendMessage();
                // Hide keyboard after sending
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });
        // Scroll chat to bottom and keep input visible when keyboard opens
        etMessage.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                rvChat.postDelayed(() -> {
                    if (!chatMessages.isEmpty()) {
                        rvChat.scrollToPosition(chatMessages.size() - 1);
                    }
                }, 200);
            }
        });
    }

    private void switchToChatbot() {
        isChatbot = true;
        if (cachedChatbotMessages != null) {
            chatMessages.clear();
            chatMessages.addAll(cachedChatbotMessages);
            chatAdapter.notifyDataSetChanged();
            if (!chatMessages.isEmpty()) {
                rvChat.scrollToPosition(chatMessages.size() - 1);
            }
        } else {
            loadChatHistory();
        }
    }

    private void switchToSupport() {
        isChatbot = false;
        subscribeSupportResponseTopic();
        if (cachedSupportMessages != null) {
            chatMessages.clear();
            chatMessages.addAll(cachedSupportMessages);
            chatAdapter.notifyDataSetChanged();
            if (!chatMessages.isEmpty()) {
                rvChat.scrollToPosition(chatMessages.size() - 1);
            }
        } else {
            loadSupportHistory();
        }
    }

    private void subscribeSupportResponseTopic() {
        try {
            if (!mqttService.isConnected()) {
                new Thread(() -> {
                    mqttService.initialize();
                    subscribeOnUiThread();
                }).start();
            } else {
                subscribeOnUiThread();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void subscribeOnUiThread() {
        runOnUiThread(() -> {
            try {
                mqttService.subscribe(supportResponseTopic, (topic, message) -> {
                    String msg = new String(message.getPayload());
                    runOnUiThread(() -> {
                        ChatMessage chatMsg = new ChatMessage(msg, false);
                        supportMessages.add(chatMsg);
                        if (!isChatbot) {
                            chatMessages.add(chatMsg);
                            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                            rvChat.scrollToPosition(chatMessages.size() - 1);
                        }
                        saveSupportHistory();
                    });
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    private void loadChatHistory() {
        if (userId == null || userId.isEmpty()) return;
        showLoading();
        chatRepository.loadChatHistory(userId, new ChatRepository.ChatHistoryListener() {
            @Override
            public void onHistoryLoaded(List<ChatMessage> messages) {
                hideLoading();
                chatMessages.clear();
                chatMessages.addAll(messages);
                cachedChatbotMessages = new ArrayList<>(messages);
                chatAdapter.notifyDataSetChanged();
                if (!chatMessages.isEmpty()) {
                    rvChat.scrollToPosition(chatMessages.size() - 1);
                }
            }
            @Override
            public void onError(Exception e) {
                hideLoading();
                Toast.makeText(ChatActivity.this, getString(R.string.error_load_chat_history), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveChatHistory() {
        if (userId == null || userId.isEmpty()) return;
        cachedChatbotMessages = new ArrayList<>(chatMessages);
        chatRepository.saveChatHistory(userId, chatMessages, new ChatRepository.SaveListener() {
            @Override
            public void onSaved() {
                // Optionally show a small indicator or log
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(ChatActivity.this, getString(R.string.error_save_chat_history), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String message = etMessage.getText().toString().trim();
        if (message.isEmpty()) return;
        if (isChatbot) {
            chatMessages.add(new ChatMessage(message, true));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
            etMessage.setText("");
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
            ChatMessage chatMsg = new ChatMessage(message, true);
            supportMessages.add(chatMsg);
            chatMessages.add(chatMsg);
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
            etMessage.setText("");
            try {
                mqttService.publish(supportRequestTopic, message);
            } catch (MqttException e) {
                e.printStackTrace();
            }
            saveSupportHistory();
        }
    }

    private void loadSupportHistory() {
        if (userId == null || userId.isEmpty()) return;
        showLoading();
        chatRepository.loadChatHistory(userId + "_support", new ChatRepository.ChatHistoryListener() {
            @Override
            public void onHistoryLoaded(List<ChatMessage> messages) {
                hideLoading();
                supportMessages.clear();
                supportMessages.addAll(messages);
                cachedSupportMessages = new ArrayList<>(messages);
                if (!isChatbot) {
                    chatMessages.clear();
                    chatMessages.addAll(messages);
                    chatAdapter.notifyDataSetChanged();
                    if (!chatMessages.isEmpty()) {
                        rvChat.scrollToPosition(chatMessages.size() - 1);
                    }
                }
            }
            @Override
            public void onError(Exception e) {
                hideLoading();
                Toast.makeText(ChatActivity.this, getString(R.string.error_load_support_chat), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveSupportHistory() {
        if (userId == null || userId.isEmpty()) return;
        cachedSupportMessages = new ArrayList<>(supportMessages);
        chatRepository.saveChatHistory(userId + "_support", supportMessages, new ChatRepository.SaveListener() {
            @Override
            public void onSaved() {}
            @Override
            public void onError(Exception e) {
                Toast.makeText(ChatActivity.this, getString(R.string.error_save_support_chat), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
