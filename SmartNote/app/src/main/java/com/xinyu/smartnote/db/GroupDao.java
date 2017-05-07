package com.xinyu.smartnote.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.xinyu.smartnote.models.Group;
import com.xinyu.smartnote.util.DateUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

public class GroupDao {
    private MyOpenHelper helper;

    public GroupDao(Context context) {
        helper = new MyOpenHelper(context);
    }

    /**
     * 查询所有分类列表
     *
     * @return
     */
    public List<Group> queryGroupAll() {
        SQLiteDatabase db = helper.getWritableDatabase();
        List<Group> groupList = new ArrayList<Group>();

        Group group ;
        Cursor cursor = null;
        try {
            cursor = db.query("db_group", null, null, null, null, null, "g_create_time asc");
            while (cursor.moveToNext()) {
                int groupId = cursor.getInt(cursor.getColumnIndex("g_id"));
                String groupName = cursor.getString(cursor.getColumnIndex("g_name"));
                int order = cursor.getInt(cursor.getColumnIndex("g_order"));
                String color = cursor.getString(cursor.getColumnIndex("g_color"));
                int encrypt = cursor.getInt(cursor.getColumnIndex("g_encrypt"));
                String createTime = cursor.getString(cursor.getColumnIndex("g_create_time"));
                String updateTime = cursor.getString(cursor.getColumnIndex("g_update_time"));
                //生成一个分类
                group = new Group();
                group.setId(groupId);
                group.setName(groupName);
                group.setOrder(order);
                group.setColor(color);
                group.setIsEncrypt(encrypt);
                group.setCreateTime(createTime);
                group.setUpdateTime(updateTime);
                groupList.add(group);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return groupList;
    }

    /**
     * 根据分类名查询分类
     *
     * @param groupName
     * @return
     */
    public Group queryGroupByName(String groupName) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Group group = null;
        Cursor cursor = null;
        try {
            Log.i(TAG, "###queryGroupByName: "+groupName);
            cursor = db.query("db_group", null, "g_name=?", new String[]{groupName}, null, null, null);
            while (cursor.moveToNext()) {
                int groupId = cursor.getInt(cursor.getColumnIndex("g_id"));
                int order = cursor.getInt(cursor.getColumnIndex("g_order"));
                String color = cursor.getString(cursor.getColumnIndex("g_color"));
                int encrypt = cursor.getInt(cursor.getColumnIndex("g_encrypt"));
                String createTime = cursor.getString(cursor.getColumnIndex("g_create_time"));
                String updateTime = cursor.getString(cursor.getColumnIndex("g_update_time"));
                //生成一个分类
                group = new Group();
                group.setId(groupId);
                group.setName(groupName);
                group.setOrder(order);
                group.setColor(color);
                group.setIsEncrypt(encrypt);
                group.setCreateTime(createTime);
                group.setUpdateTime(updateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return group;
    }

    /**
     * 根据分类ID查询分类
     *
     * @return
     */
    public Group queryGroupById(int groupId) {
        SQLiteDatabase db = helper.getWritableDatabase();

        Group group = null;
        Cursor cursor = null;
        try {
            cursor = db.query("db_group", null, "g_id=?", new String[]{groupId + ""}, null, null, null);
            while (cursor.moveToNext()) {
                int order = cursor.getInt(cursor.getColumnIndex("g_order"));
                String color = cursor.getString(cursor.getColumnIndex("g_color"));
                int encrypt = cursor.getInt(cursor.getColumnIndex("g_encrypt"));
                String groupName = cursor.getString(cursor.getColumnIndex("g_name"));
                String createTime = cursor.getString(cursor.getColumnIndex("g_create_time"));
                String updateTime = cursor.getString(cursor.getColumnIndex("g_update_time"));
                //生成一个订单
                group = new Group();
                group.setId(groupId);
                group.setName(groupName);
                group.setOrder(order);
                group.setColor(color);
                group.setIsEncrypt(encrypt);
                group.setCreateTime(createTime);
                group.setUpdateTime(updateTime);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return group;
    }
}
