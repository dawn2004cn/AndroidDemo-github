package com.noahedu.demo.mybatis.core;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by RefitD on 10/27/2015.
 */
public class MainApp extends Application {

    private static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //enable below for device test or disable for unit test
        copyDataBase("app.db");
    }

    private void copyDataBase(String dbname)
    {
        String dbpath = "/data/data/"+ getAppContext().getPackageName()+"/databases/";
        copyDataBase(dbname, dbpath);
    }

    /**
     * Check if the database exist and can be read.
     *
     * @return true if it exists and can be read, false if it doesn't
     */
    private boolean checkDataBase(String dbfile) {
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(dbfile, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }
        return checkDB != null;
    }

    private void copyDataBase(String dbname, String dbpath)
    {
        Log.i("Database", "New database is being copied to device!");
        byte[] buffer = new byte[1024];
        OutputStream myOutput = null;
        int length;
        // Open your local db as the input stream
        InputStream myInput = null;
        try
        {
            String dbfile = dbpath + dbname;
            if(!checkDataBase(dbfile)) {
                Log.i("Database","exist in " + dbfile);
                getAppContext().openOrCreateDatabase(dbname, MODE_PRIVATE, null);
            }
            myInput = getAppContext().getAssets().open(dbname);
            // transfer bytes from the inputfile to the
            // outputfile

            Log.i("Database",dbfile);
            myOutput =new FileOutputStream(dbfile);
            while((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            myOutput.close();
            myOutput.flush();
            myInput.close();
            Log.i("Database","New database has been copied to device!");

        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public static Context getAppContext() {
        return context;
    }
}