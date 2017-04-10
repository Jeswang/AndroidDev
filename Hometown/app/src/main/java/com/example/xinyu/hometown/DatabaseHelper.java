package com.example.xinyu.hometown;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by xinyu on 4/9/17.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "name.db";
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase nameDb) {
        nameDb.execSQL("CREATE TABLE IF NOT EXISTS" + " hometown" + " ("
            + "_ID" + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
            + "nickname" + " TEXT UNIQUE NOT NULL,"
            + "country" + " TEXT NOT NULL,"
            + "state" + " TEXT NOT NULL,"
            + "city" + " TEXT NOT NULL,"
            + "year" + " NUMBER NOT NULL,"
            + "longitude" + " REAL,"
            + "latitude" + " REAL"
            + ");");
        nameDb.execSQL("INSERT INTO hometown ( nickname,country,state,city,year) VALUES ('zxy','china','heilongjiang','qiqihar',2016 );");
    }
    public void onUpgrade(SQLiteDatabase arg0, int oldVersion, int newVersion) {
    }
}
