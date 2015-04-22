package com.aspsine.zhihu.daily.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.aspsine.zhihu.daily.model.Cache;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class CacheDao {
    public static final String TABLE_NAME = Cache.class.getSimpleName();
    private DBOpenHelper mHelper;

    public CacheDao(Context context) {
        mHelper = new DBOpenHelper(context);
    }

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint
                + TABLE_NAME
                + " (" +
                "_id INTEGER PRIMARY KEY ," + // 0: id
                "request TEXT," + // 1: request
                "response TEXT," + // 2: response
                "time INTEGER);"); // 3: time
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + TABLE_NAME;
        db.execSQL(sql);
    }

    public void insertCache(Cache cache) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("insert into "
                        + TABLE_NAME
                        + " (request, response, time) values(?,?,?)",
                new Object[]{cache.getRequest(), cache.getResponse(), cache.getTime()});
        db.close();
    }

    public void deleteCache(String request) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("delete from "
                        + TABLE_NAME
                        + " where request=?",
                new Object[]{request});
        db.close();
    }

    public void updateCache(Cache cache) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                        + TABLE_NAME
                        + " where request = ?",
                new String[]{cache.getRequest()});

        boolean isExist = cursor.moveToNext();
        if (isExist) {
            db.execSQL("delete from "
                            + TABLE_NAME
                            + " where request=?",
                    new Object[]{cache.getRequest()});
        }
        db.execSQL("insert into "
                        + TABLE_NAME
                        + " (request, response, time) values(?,?,?)",
                new Object[]{cache.getRequest(), cache.getResponse(), cache.getTime()});
        cursor.close();
        db.close();
    }

    public Cache getCache(String request) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                        + TABLE_NAME
                        + " where request = ?",
                new String[]{request});

        Cache cache = new Cache();
        while (cursor.moveToNext()) {
            cache.setRequest(cursor.getString(cursor.getColumnIndex("request")));
            cache.setResponse(cursor.getString(cursor.getColumnIndex("response")));
            cache.setTime(cursor.getLong(cursor.getColumnIndex("time")));
            break;
        }
        cursor.close();
        db.close();
        return cache;
    }

    public boolean exists(String request) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from "
                        + TABLE_NAME
                        + " where request = ?",
                new String[]{request});
        boolean isExist = cursor.moveToNext();
        cursor.close();
        db.close();
        return isExist;
    }
}
