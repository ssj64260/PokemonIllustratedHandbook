package com.android.pokemonillustratedhandbook.utils;

import android.database.sqlite.SQLiteDatabase;

import com.android.pokemonillustratedhandbook.app.APP;
import com.android.pokemonillustratedhandbook.model.DaoMaster;
import com.android.pokemonillustratedhandbook.model.DaoSession;


public class GreenDaoHelper {

    private static final String DATABASE_NAME = "Pokemon-db";

    private DaoSession daoSession;
    private SQLiteDatabase basedb;
    private DaoMaster.DevOpenHelper helper;
    private DaoMaster daoMaster;

    public GreenDaoHelper() {
        helper = new DaoMaster.DevOpenHelper(APP.getInstance(), DATABASE_NAME, null);
        basedb = helper.getWritableDatabase();
        daoMaster = new DaoMaster(basedb);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SQLiteDatabase getBasedb() {
        return basedb;
    }

    public DaoMaster.DevOpenHelper getHelper() {
        return helper;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }
}
