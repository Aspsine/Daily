package com.aspsine.zhihu.daily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.util.L;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "Database";

    private static final int DB_VERSION = Constants.Config.DATABASE_VERSION;
    private static final String DB_NAME = Constants.Config.DATABASE_NAME;

    private static DBOpenHelper sDBOpenHelper;
    /**
     * Creates underlying database table using DAOs.
     */
    private void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        CacheDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    private void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        CacheDao.dropTable(db, ifExists);
    }

    public static DBOpenHelper getInstance(Context context) {
        if (sDBOpenHelper == null) {
            synchronized (DBOpenHelper.class) {
                if (sDBOpenHelper == null) {
                    sDBOpenHelper = new DBOpenHelper(context);
                }
            }
        }
        return sDBOpenHelper;
    }

    private DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        L.i(TAG, "Creating tables for DB version " + DB_VERSION);
        createAllTables(db, false);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        L.i(TAG, "Upgrading DB from version " + oldVersion + " to " + newVersion + " by dropping all tables");
        dropAllTables(db, true);
        onCreate(db);
    }
}

