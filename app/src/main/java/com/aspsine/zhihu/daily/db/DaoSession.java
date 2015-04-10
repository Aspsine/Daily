package com.aspsine.zhihu.daily.db;

import com.aspsine.zhihu.daily.db.abs.AbstractDaoSession;
import com.aspsine.zhihu.daily.db.dao.CacheDao;

/**
 * Created by Aspsine on 2015/4/10.
 */
public class DaoSession extends AbstractDaoSession {

    private final CacheDao cacheDao;

    public DaoSession() {
        cacheDao = new CacheDao();
    }

    public CacheDao getCacheDao() {
        return cacheDao;
    }

}
