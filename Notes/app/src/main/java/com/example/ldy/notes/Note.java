package com.example.ldy.notes;

import com.google.firebase.database.Exclude;

public class Note {
    private int id; // note id
    private String title; // note title
    private String content; // note content
    private String date; //
    private String time; // time reminder
    private String key;

    public int getId() { return id;}
    public void setId(int id) { this.id = id;}

    public String getTitle() { return title;}
    public void setTitle(String title) { this.title = title;}

    public String getContent() { return content;}
    public void setContent(String content) { this.content = content;}

    public String getDate() { return date;}
    public void setDate(String date) { this.date = date;}

    public String getTime() { return time;}
    public void setTime(String time) { this.time = time;}

    public String getKey() { return key;}

    @Exclude
    public void setKey(String key) { this.key = key;}

}
