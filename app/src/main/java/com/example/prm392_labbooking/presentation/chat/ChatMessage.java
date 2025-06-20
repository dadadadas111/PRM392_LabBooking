package com.example.prm392_labbooking.presentation.chat;

public class ChatMessage {
    public String message;
    public boolean isUser;

    public ChatMessage() {
        // Required for Firestore
    }

    public ChatMessage(String message, boolean isUser) {
        this.message = message;
        this.isUser = isUser;
    }

    public String getMessage() {
        return message;
    }

    public boolean isUser() {
        return isUser;
    }
}
