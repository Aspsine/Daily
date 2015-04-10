package com.aspsine.zhihu.daily.db.abs;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aspsine on 2015/4/10.
 */
public abstract class AbstractDaoMaster {
    protected final SQLiteDatabase db;

    protected final int schemaVersion;

    public AbstractDaoMaster(SQLiteDatabase db, int schemaVersion) {
        this.db = db;
        this.schemaVersion = schemaVersion;

    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    /**
     * Gets the SQLiteDatabase for custom database access. Not needed for greenDAO entities.
     */
    public SQLiteDatabase getDatabase() {
        return db;
    }

    public abstract AbstractDaoSession newSession();
}
