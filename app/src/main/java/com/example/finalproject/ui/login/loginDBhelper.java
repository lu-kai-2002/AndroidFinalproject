package com.example.finalproject.ui.login;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.database.Cursor;

public class loginDBhelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "app.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COL_ID       = "id";
    public static final String COL_USER     = "username";
    public static final String COL_PASS     = "password";

    private static final String SQL_CREATE =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COL_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USER + " TEXT UNIQUE, " +
                    COL_PASS + " TEXT" +
                    ")";

    public loginDBhelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    /** 注册新用户 */
    public boolean addUser(String user, String pass) {
        if (userExists(user)) return false;
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_USER, user);
        cv.put(COL_PASS, pass);
        long id = db.insert(TABLE_USERS, null, cv);
        return id != -1;
    }

    /** 用户名是否已存在 */
    public boolean userExists(String user) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_USER + "=?", new String[]{user},
                null, null, null);
        boolean exists = c.moveToFirst();
        c.close();
        return exists;
    }

    /** 验证用户名+密码 */
    public boolean checkUser(String user, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_USERS, new String[]{COL_ID},
                COL_USER + "=? AND " + COL_PASS + "=?",
                new String[]{user, pass},
                null, null, null);
        boolean ok = c.moveToFirst();
        c.close();
        return ok;
    }
}
