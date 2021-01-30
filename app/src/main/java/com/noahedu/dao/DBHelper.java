package com.noahedu.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * © 2019 www.youxuepai.com
 *
 * @version 1.0
 * @file name：DBHelper$
 * @file describe：简单描述该文件作用
 * @anthor :daisg
 * @create time 2020/4/26$ 20:21$
 */
public class DBHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "myTask.db";
    public static int DB_VERSION = 1;
    public static int DB_RESOURCE;
    private Context context;
    private DaoHelper daoHelper;

    public DBHelper(Context context) {
        this(context, DB_NAME, null, DB_VERSION);
    }

    private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createTable(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            dropTable(db);
            createTable(db);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    public DaoHelper getDaoHelper() {
        return daoHelper;
    }

    public void setDaoHelper(DaoHelper daoHelper) {
        this.daoHelper = daoHelper;
    }

    private void createTable(SQLiteDatabase db) throws XmlPullParserException, IOException {
        DaoHelper.dbXmlId=DB_RESOURCE;
        daoHelper=new DaoHelper();
        daoHelper.setContext(context);
        daoHelper.initTable(db);
    }

    private void dropTable(SQLiteDatabase db) throws XmlPullParserException, IOException{
        DaoHelper.dbXmlId=DB_RESOURCE;
        daoHelper=new DaoHelper();
        daoHelper.setContext(context);
        daoHelper.dropTable(db);
    }
}
