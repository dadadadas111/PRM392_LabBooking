package com.example.prm392_labbooking.data.firebase;

import java.util.List;

public interface ChatService {
    void sendMessage(String userId, String message);
    void listenForMessages(String userId, MessageListener listener);

    interface MessageListener {
        void onNewMessages(List<String> messages);
    }
}
