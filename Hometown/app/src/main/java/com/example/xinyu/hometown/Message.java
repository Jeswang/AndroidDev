package com.example.xinyu.hometown;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by xinyu on 4/11/17.
 */

public class Message {
    public String user;
    public Long time;
    public String content;

    public Message() {};

    public Message(String user, Long time, String content) {
        this.user = user;
        this.time = time;
        this.content = content;
    }

    public String getUser() { return user;}
    public Long getTime() { return time;}
    public String getContent() { return content;}

    public String getTimeString() {
        Date expiry = new Date(time*1000);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return df.format(expiry);
    }
}
