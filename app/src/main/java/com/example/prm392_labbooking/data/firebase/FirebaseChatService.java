package com.example.prm392_labbooking.data.firebase;

import com.example.prm392_labbooking.presentation.chat.ChatMessage;
import java.util.List;

public interface FirebaseChatService extends ChatService {
    void saveChatHistory(String userId, List<ChatMessage> messages, SaveListener listener);
    void getChatHistory(String userId, ChatHistoryListener listener);

    interface SaveListener {
        void onSaved();
        void onError(Exception e);
    }
    interface ChatHistoryListener {
        void onHistoryLoaded(List<ChatMessage> messages);
        void onError(Exception e);
    }
}
