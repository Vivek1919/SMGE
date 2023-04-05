package com.example.smge.Chat;

public class ChatList {
    private String chatId;
    private String senderEmail;
    private String receiverEmail;
    private String lastMessage;
    private long lastMessageTime;
    private int unseenCount;

    public ChatList() {
        // Required empty constructor for Firebase
    }

    public ChatList(String chatId, String senderEmail, String receiverEmail, String lastMessage, long lastMessageTime, int unseenCount) {
        this.chatId = chatId;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.unseenCount = unseenCount;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(long lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public int getUnseenCount() {
        return unseenCount;
    }

    public void setUnseenCount(int unseenMessageCount) {
        this.unseenCount = unseenMessageCount;
    }

    public String getKey() {
        return chatId; // Assuming the chatId field is the unique key for this ChatList object
    }

}

