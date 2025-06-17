package com.example.prm392_labbooking.data.repository;

import com.example.prm392_labbooking.data.firebase.ChatService;
import com.example.prm392_labbooking.domain.repository.ChatRepository;
import java.util.List;

public class ChatRepositoryImpl implements ChatRepository {
    private final ChatService chatService;

    public ChatRepositoryImpl(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void sendMessage(String userId, String message) {
        chatService.sendMessage(userId, message);
    }

    @Override
    public void listenForMessages(String userId, MessageListener listener) {
        chatService.listenForMessages(userId, listener::onNewMessages);
    }
}
