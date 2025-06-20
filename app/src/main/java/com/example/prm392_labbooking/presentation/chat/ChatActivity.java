package com.example.prm392_labbooking.presentation.chat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.example.prm392_labbooking.utils.SecretLoader;
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
            chatMessages.add(new ChatMessage("...", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
            streamGeminiResponse(message, chatMessages.size() - 1);
        } else {
            chatMessages.add(new ChatMessage("(Support will reply soon)", false));
            chatAdapter.notifyItemInserted(chatMessages.size() - 1);
            rvChat.scrollToPosition(chatMessages.size() - 1);
        }
    }

    private void streamGeminiResponse(String userMessage, int botMsgIndex) {
        new Thread(() -> {
            try {
                String apiKey = SecretLoader.getGeminiApiKey(this);
                String urlStr = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:streamGenerateContent?alt=sse&key=" + apiKey;
                URL url = new URL(urlStr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                JSONObject payload = new JSONObject();
                // Add system instruction
                JSONObject sysInstruction = new JSONObject();
                JSONArray sysParts = new JSONArray();
                JSONObject sysPart = new JSONObject();
                sysPart.put("text", "You are a helpful assistant for a university lab booking app. Reply concisely and clearly in Vietnamese.");
                sysParts.put(sysPart);
                sysInstruction.put("parts", sysParts);
                payload.put("system_instruction", sysInstruction);
                // Add user message history for context
                JSONArray contents = new JSONArray();
                for (ChatMessage msg : chatMessages) {
                    JSONObject part = new JSONObject();
                    part.put("text", msg.getMessage());
                    JSONObject content = new JSONObject();
                    content.put("role", msg.isUser() ? "user" : "model");
                    content.put("parts", new JSONArray().put(part));
                    contents.put(content);
                }
                // Add current user message
                JSONObject part = new JSONObject();
                part.put("text", userMessage);
                JSONObject content = new JSONObject();
                content.put("role", "user");
                content.put("parts", new JSONArray().put(part));
                contents.put(content);
                payload.put("contents", contents);
                // Add safety settings
                JSONArray safetySettings = new JSONArray();
                JSONObject safety = new JSONObject();
                safety.put("category", "HARM_CATEGORY_DANGEROUS_CONTENT");
                safety.put("threshold", "BLOCK_ONLY_HIGH");
                safetySettings.put(safety);
                payload.put("safetySettings", safetySettings);
                // Add generation config
                JSONObject genConfig = new JSONObject();
                genConfig.put("stopSequences", new JSONArray().put("Title"));
                genConfig.put("temperature", 1.0);
                genConfig.put("maxOutputTokens", 800);
                genConfig.put("topP", 0.8);
                genConfig.put("topK", 10);
                payload.put("generationConfig", genConfig);
                conn.getOutputStream().write(payload.toString().getBytes());
                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuilder response = new StringBuilder();
                Handler mainHandler = new Handler(Looper.getMainLooper());
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("data: ")) {
                        String json = line.substring(6);
                        if (json.trim().isEmpty()) continue;
                        JSONObject obj = new JSONObject(json);
                        JSONArray candidates = obj.optJSONArray("candidates");
                        if (candidates != null && candidates.length() > 0) {
                            JSONObject candidate = candidates.optJSONObject(0);
                            if (candidate != null) {
                                JSONObject contentObj = candidate.optJSONObject("content");
                                if (contentObj != null) {
                                    JSONArray parts = contentObj.optJSONArray("parts");
                                    if (parts != null && parts.length() > 0) {
                                        String text = parts.optJSONObject(0).optString("text", "");
                                        response.append(text);
                                        String resp = response.toString();
                                        mainHandler.post(() -> {
                                            chatMessages.set(botMsgIndex, new ChatMessage(resp, false));
                                            chatAdapter.notifyItemChanged(botMsgIndex);
                                            // Only scroll if the user is already at the bottom
                                            LinearLayoutManager layoutManager = (LinearLayoutManager) rvChat.getLayoutManager();
                                            if (layoutManager != null) {
                                                int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition();
                                                if (lastVisible == botMsgIndex - 1 || lastVisible == botMsgIndex) {
                                                    rvChat.scrollToPosition(botMsgIndex);
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                }
                reader.close();
            } catch (Exception e) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> {
                    chatMessages.set(botMsgIndex, new ChatMessage("(Gemini error)", false));
                    chatAdapter.notifyItemChanged(botMsgIndex);
                });
            }
        }).start();
    }
}
