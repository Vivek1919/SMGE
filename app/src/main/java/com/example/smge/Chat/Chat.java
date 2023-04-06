package com.example.smge.Chat;

public class Chat {
    private String message;
    private String senderUid;
    private String senderEmail;
    private String receiverEmail;
    private long timestamp;

    private boolean isSeen;

    public Chat() {
        // Empty constructor needed for Firebase
    }

    public Chat(String message, String senderUid, String senderEmail, String receiverEmail, long timestamp) {

        this.message = message;
        this.senderUid = senderUid;
        this.senderEmail = senderEmail;
        this.receiverEmail = receiverEmail;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean isSeen) {
        this.isSeen = isSeen;
    }

    @Override
    public String toString() {
        return "Chat{" +
                "message='" + message + '\'' +
                ", senderUid='" + senderUid + '\'' +
                ", senderEmail='" + senderEmail + '\'' +
                ", receiverEmail='" + receiverEmail + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    public Object getKey() {
        return this.senderEmail + "_" + this.receiverEmail + "_" + this.timestamp;
    }
}

