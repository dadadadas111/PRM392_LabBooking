package com.example.prm392_labbooking.domain.repository;

import java.util.List;

public interface ChatRepository {
    void sendMessage(String userId, String message);
    void listenForMessages(String userId, MessageListener listener);

    interface MessageListener {
        void onNewMessages(List<String> messages);
    }
}
