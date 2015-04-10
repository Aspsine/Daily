package com.aspsine.zhihu.daily.db.dao;

import android.database.sqlite.SQLiteDatabase;

import com.aspsine.zhihu.daily.db.abs.AbstractDao;
import com.aspsine.zhihu.daily.model.Cache;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class CacheDao extends AbstractDao<Cache> {
    public static final String TABLE_NAME = Cache.class.getSimpleName();

    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        //FIXME
        db.execSQL("CREATE TABLE " + constraint
                + TABLE_NAME
                + " (" +
                "'_id' INTEGER PRIMARY KEY ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'SLOTS' INTEGER," + // 2: slots
                "'DESCRIPTION' TEXT);"); // 3: description
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        //FIXME
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'BOX'";
        db.execSQL(sql);
    }


}
