package com.example.finalproject.ui.post;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app.db"; // 与 loginDBhelper 保持一致
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POSTS = "posts";

    // 字段常量（推荐）
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMAGE_RES_ID = "image_res_id";
    public static final String COL_BAR_NAME = "bar_name";

    // 建表 SQL
    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USER_ID + " INTEGER NOT NULL, " + // 外键，可关联 users(id)
                    COL_CONTENT + " TEXT NOT NULL, " +
                    COL_IMAGE_RES_ID + " INTEGER, " +
                    COL_BAR_NAME + " TEXT" +
                    ");";

    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 启用外键约束（可选）
    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }
}
