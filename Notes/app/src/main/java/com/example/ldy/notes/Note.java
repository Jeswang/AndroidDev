package com.example.ldy.notes;

import com.google.firebase.database.Exclude;

/**
 * Created by ldy on 17-5-8.
 */

public class Note {
    private int id; // note id
    private String title; // note title
    private String content; // note content
    private String createTime; //
    private String reminder; // time reminder
    private String email;
    private String key;

    public int getId() { return id;}
    public void setId(int id) { this.id = id;}

    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getContent() { return content;}
    public void setContent(String content) { this.content = content;}

    public String getCreateTime() { return createTime;}
    public void setCreateTime(String createTime) { this.createTime = createTime;}

    public String getReminder() { return reminder;}
    public void setReminder(String reminder) { this.reminder = reminder;}

    public String getKey() { return key;}

    @Exclude
    public void setKey(String key) { this.key = key;}

}
