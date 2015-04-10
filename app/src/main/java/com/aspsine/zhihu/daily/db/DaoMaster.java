package com.aspsine.zhihu.daily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.aspsine.zhihu.daily.Constants;
import com.aspsine.zhihu.daily.db.abs.AbstractDaoMaster;
import com.aspsine.zhihu.daily.db.dao.CacheDao;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class DaoMaster extends AbstractDaoMaster {
    private static final String TAG = "GreenDao";

    private static final int DB_VERSION = Constants.Config.DATABASE_VERSION;

    public DaoMaster(SQLiteDatabase db) {
        super(db, DB_VERSION);
    }

    /**
     * Creates underlying database table using DAOs.
     */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        CacheDao.createTable(db, ifNotExists);
    }

    /**
     * Drops underlying database table using DAOs.
     */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        CacheDao.dropTable(db, ifExists);
    }

    public static abstract class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "Creating tables for DB version " + DB_VERSION);
            createAllTables(db, false);
        }
    }

    /**
     * WARNING: Drops all table on Upgrade! Use only during development.
     */
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "Upgrading DB from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    @Override
    public DaoSession newSession() {
        return new DaoSession();
    }
}
