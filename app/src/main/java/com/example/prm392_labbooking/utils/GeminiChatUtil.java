package com.example.prm392_labbooking.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.prm392_labbooking.presentation.chat.ChatAdapter;
import com.example.prm392_labbooking.presentation.chat.ChatMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class GeminiChatUtil {
    public interface GeminiStreamCallback {
        void onStreamUpdate(String text);
        void onError(String errorMsg);
        void onStreamComplete();
    }

    public static void streamGeminiResponse(Context context, List<ChatMessage> chatMessages, String userMessage, GeminiStreamCallback callback) {
        new Thread(() -> {
            try {
                String apiKey = SecretLoader.getGeminiApiKey(context);
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
                                        mainHandler.post(() -> callback.onStreamUpdate(resp));
                                    }
                                }
                            }
                        }
                    }
                }
                reader.close();
                // Notify completion
                mainHandler.post(callback::onStreamComplete);
            } catch (Exception e) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(() -> callback.onError("(Gemini error)"));
                mainHandler.post(() -> {
                    if (callback instanceof GeminiStreamCallback) {
                        ((GeminiStreamCallback) callback).onStreamComplete();
                    }
                });
                return;
            }
            // Always call onStreamComplete at the end of streaming
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(() -> {
                if (callback instanceof GeminiStreamCallback) {
                    ((GeminiStreamCallback) callback).onStreamComplete();
                }
            });
        }).start();
    }
}
