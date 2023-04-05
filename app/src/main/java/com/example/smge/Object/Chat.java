package com.example.smge.Object;


public class Chat {
    private String message;
    private String senderId;
    private String senderEmail;
    private String receiverId;
    private long timestamp;

    public Chat(String message, String senderId, String senderEmail, String receiverId, long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.senderEmail = senderEmail;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public long getTimestamp() {
        return timestamp;
    }
}




