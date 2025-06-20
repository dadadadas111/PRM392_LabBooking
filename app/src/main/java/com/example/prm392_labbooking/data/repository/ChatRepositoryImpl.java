package com.example.prm392_labbooking.data.repository;

import com.example.prm392_labbooking.data.firebase.ChatService;
import com.example.prm392_labbooking.data.firebase.FirebaseChatService;
import com.example.prm392_labbooking.domain.repository.ChatRepository;
import com.example.prm392_labbooking.presentation.chat.ChatMessage;
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

    @Override
    public void saveChatHistory(String userId, List<ChatMessage> messages, SaveListener listener) {
        if (chatService instanceof FirebaseChatService) {
            ((FirebaseChatService) chatService).saveChatHistory(userId, messages, new FirebaseChatService.SaveListener() {
                @Override
                public void onSaved() { listener.onSaved(); }
                @Override
                public void onError(Exception e) { listener.onError(e); }
            });
        }
    }

    @Override
    public void loadChatHistory(String userId, ChatHistoryListener listener) {
        if (chatService instanceof FirebaseChatService) {
            ((FirebaseChatService) chatService).getChatHistory(userId, new FirebaseChatService.ChatHistoryListener() {
                @Override
                public void onHistoryLoaded(List<ChatMessage> messages) { listener.onHistoryLoaded(messages); }
                @Override
                public void onError(Exception e) { listener.onError(e); }
            });
        }
    }
}
