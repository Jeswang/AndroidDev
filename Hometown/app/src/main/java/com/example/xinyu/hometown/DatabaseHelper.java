package com.example.xinyu.hometown;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinyu on 4/9/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "hometown.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase nameDb) {
        nameDb.execSQL("CREATE TABLE IF NOT EXISTS" + " hometown" + " ("
            + "id" + " TEXT PRIMARY KEY NOT NULL,"
            + "nickname" + " TEXT UNIQUE NOT NULL,"
            + "country" + " TEXT NOT NULL,"
            + "state" + " TEXT NOT NULL,"
            + "city" + " TEXT NOT NULL,"
            + "year" + " TEXT NOT NULL,"
            + "longitude" + " TEXT,"
            + "latitude" + " TEXT"
            + ");");
        //nameDb.execSQL("INSERT INTO hometown ( nickname,country,state,city,year) VALUES ('zxy','china','heilongjiang','qiqihar','2016' );");
    }
    public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) { }
    public void saveUserInfos(SQLiteDatabase nameDb, UserInfo[] users) {
        for(int i = 0; i < users.length; i++) {
            ContentValues newUser = new ContentValues(8);
            newUser.put("id", users[i].id);
            newUser.put("nickname", users[i].nickname);
            newUser.put("country", users[i].country);
            newUser.put("state", users[i].state);
            newUser.put("city", users[i].city);
            newUser.put("year", users[i].year);
            newUser.put("longitude", users[i].longitude);
            newUser.put("latitude", users[i].latitude);
            nameDb.replace("hometown", null, newUser);
        }
    }
    public List<UserInfo> getUserInfos(SQLiteDatabase nameDb) {
        Cursor result = nameDb.rawQuery("select * from hometown", null);
        int rowCount = result.getCount();
        UserInfo[] getUser = new UserInfo[rowCount];
        List<UserInfo> responseUsers = new ArrayList<UserInfo>();
        for(int i = 0; i < rowCount; i++){
            UserInfo newUser = new UserInfo();
            result.moveToFirst();
            newUser.id = result.getString(0);
            newUser.nickname = result.getString(1);
            newUser.country = result.getString(2);
            newUser.state = result.getString(3);
            newUser.city = result.getString(4);
            newUser.year = result.getString(5);
            newUser.longitude = result.getString(6);
            newUser.latitude = result.getString(7);
            getUser[i] = newUser;
        }
        for (int i = 0; i < getUser.length; i++) {
            responseUsers.add(getUser[i]);
        }
        return responseUsers;
    }
}
