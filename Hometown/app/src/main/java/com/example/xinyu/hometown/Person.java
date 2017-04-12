package com.example.xinyu.hometown;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by xinyu on 4/11/17.
 */

public class Person {
    private String email;
    private String nickname;
    private String longitude;
    private String latitude;

    public Person() {
    }

    public Person(String email, String nickname, String longitude, String latitude) {
        this.email = email;
        this.nickname = nickname;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }
    public String getNickname() {
        return nickname;
    }
    public String getLongitude() {return longitude;}
    public String getLatitude() { return latitude;}
}
