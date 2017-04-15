package com.example.xinyu.hometown;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xinyu on 4/11/17.
 */

public class Message {
    private String user;
    private int time;
    private String content;

    public Message() {};

    public Message(String user, int time, String content) {
        this.user = user;
        this.time = time;
        this.content = content;
    }

    public String getUser() { return user;}
    public int getTime() { return time;}
    public String getContent() { return content;}

    public String getTimeString() {
        Date expiry = new Date(time);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(expiry);
    }
}
