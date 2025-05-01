package com.example.finalproject.ui.comment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 评论数据库辅助类，用于创建和管理评论表。
 */
public class CommentDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "comment.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "comments";
    public static final String COL_ID = "id";
    public static final String COL_POST_ID = "post_id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_TIMESTAMP = "timestamp";

    // 建表语句
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_POST_ID + " INTEGER NOT NULL, " +
                    COL_USER_ID + " INTEGER NOT NULL, " +
                    COL_CONTENT + " TEXT NOT NULL, " +
                    COL_TIMESTAMP + " INTEGER NOT NULL" +
                    ")";

    public CommentDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 初次创建数据库时调用
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    // 数据库版本升级时调用
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
