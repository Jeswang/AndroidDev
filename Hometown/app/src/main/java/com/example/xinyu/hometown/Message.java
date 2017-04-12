package com.example.xinyu.hometown;

/**
 * Created by xinyu on 4/11/17.
 */

public class Message {
    private String userEmail;
    private String time;
    private String messageContent;

    public Message() {};

    public Message(String userEmail, String time, String content) {
        this.userEmail = userEmail;
        this.time = time;
        this.messageContent = content;
    }

    public String getUserEmail() { return userEmail;}
    public String getTime() { return time;}
    public String getContent() { return messageContent;}
}
