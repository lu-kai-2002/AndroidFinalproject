package com.example.finalproject.ui.notifications;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NotificationDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "app2.db";
    private static final int DATABASE_VERSION = 1; // bump when adding new tables

    public static final String TABLE_NAME = "notifications";

    public NotificationDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "receiverId INTEGER NOT NULL," +
                "postId INTEGER NOT NULL," +
                "commentId INTEGER NOT NULL," +
                "contentPreview TEXT," +
                "timestamp INTEGER NOT NULL," +
                "isRead INTEGER DEFAULT 0);";
        db.execSQL(sql);

        // 其他旧表的建表 SQL（若有）同样放在这里
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }
}
