package com.yetwish.contactsdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * SQLiteOpenHelperImp
 * Created by yetwish on 2016/9/8.
 */
public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DB_NAME = "contactsDemo.db";
    private static final int DATABASE_VERSION = 1;

    //todo varchar长度
    private static final String CREATE_CONTACTS = "CREATE TABLE IF NOT EXISTS contacts("
            + " id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " name VARCHAR NOT NULL, "
            + " phoneNumber VARCHAR, "//用json格式存储
            + " sortKey VARCHAR )";


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CONTACTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST contacts");
        onCreate(db);
    }

}
