package com.example.finalproject.ui.post;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.finalproject.R;

import java.util.Arrays;
import java.util.List;

public class PostDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "post.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_POSTS = "posts";
    public static final String COL_ID = "id";
    public static final String COL_USER_ID = "user_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMAGE_RES_ID = "image_res_id";
    public static final String COL_BAR_NAME = "bar_name";

    // 修改：移除外键约束
    private static final String SQL_CREATE =
            "CREATE TABLE IF NOT EXISTS " + TABLE_POSTS + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_USER_ID + " INTEGER NOT NULL, " +  // 保留字段但不关联外键
                    COL_CONTENT + " TEXT NOT NULL, " +
                    COL_IMAGE_RES_ID + " INTEGER DEFAULT 0, " +
                    COL_BAR_NAME + " TEXT" +
                    ")";

    public PostDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 确保先创建 posts 表（不再依赖 users 表）
        db.execSQL(SQL_CREATE);
        insertSamplePosts(db); // 插入测试数据
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_POSTS);
        onCreate(db);
    }

    /** 插入初始数据（不再需要 users 表存在） */
    private void insertSamplePosts(SQLiteDatabase db) {
        List<PostEntity> samplePosts = Arrays.asList(
                new PostEntity(1, 1, "Wow!", R.drawable.sample1, null),
                new PostEntity(2, 2, "So chill~", R.drawable.sample2, null),
                new PostEntity(3, 3, "It's a special place.", R.drawable.sample3, null)
        );

        ContentValues values = new ContentValues();
        for (PostEntity post : samplePosts) {
            values.clear();
            values.put(COL_USER_ID, post.getUserId());
            values.put(COL_CONTENT, post.getContent());
            values.put(COL_IMAGE_RES_ID, post.getImageResId());
            values.put(COL_BAR_NAME, post.getBarName());
            db.insert(TABLE_POSTS, null, values);
        }
    }
}
