package com.example.prm392_labbooking.data.firebase;

import java.util.ArrayList;
import java.util.List;

public class MockChatService implements ChatService {
    private final List<String> messages = new ArrayList<>();
    private ChatService.MessageListener listener;

    @Override
    public void sendMessage(String userId, String message) {
        messages.add(userId + ": " + message);
        if (listener != null) listener.onNewMessages(messages);
    }

    @Override
    public void listenForMessages(String userId, MessageListener listener) {
        this.listener = listener;
        listener.onNewMessages(messages);
    }
}
