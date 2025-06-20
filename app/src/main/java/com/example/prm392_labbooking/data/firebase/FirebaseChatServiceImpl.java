package com.example.prm392_labbooking.data.firebase;

import com.example.prm392_labbooking.presentation.chat.ChatMessage;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseChatServiceImpl implements FirebaseChatService {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String COLLECTION = "chat_histories";

    @Override
    public void saveChatHistory(String userId, List<ChatMessage> messages, SaveListener listener) {
        List<Map<String, Object>> msgList = new ArrayList<>();
        for (ChatMessage msg : messages) {
            Map<String, Object> map = new HashMap<>();
            map.put("message", msg.getMessage());
            map.put("isUser", msg.isUser());
            msgList.add(map);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("messages", msgList);
        db.collection(COLLECTION).document(userId)
            .set(data, SetOptions.merge())
            .addOnSuccessListener(unused -> listener.onSaved())
            .addOnFailureListener(listener::onError);
    }

    @Override
    public void getChatHistory(String userId, ChatHistoryListener listener) {
        db.collection(COLLECTION).document(userId).get()
            .addOnSuccessListener(documentSnapshot -> {
                List<ChatMessage> messages = new ArrayList<>();
                if (documentSnapshot.exists()) {
                    List<Map<String, Object>> msgList = (List<Map<String, Object>>) documentSnapshot.get("messages");
                    if (msgList != null) {
                        for (Map<String, Object> map : msgList) {
                            String message = (String) map.get("message");
                            boolean isUser = Boolean.TRUE.equals(map.get("isUser"));
                            messages.add(new ChatMessage(message, isUser));
                        }
                    }
                }
                listener.onHistoryLoaded(messages);
            })
            .addOnFailureListener(listener::onError);
    }

    // Unused methods from ChatService
    @Override
    public void sendMessage(String userId, String message) {}
    @Override
    public void listenForMessages(String userId, MessageListener listener) {}
}
