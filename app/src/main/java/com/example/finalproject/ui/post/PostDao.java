package com.example.finalproject.ui.post;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据访问类，用于操作 Post 表，包括插入、查询等。
 */
public class PostDao {
    private PostDbHelper dbHelper;

    public PostDao(Context context) {
        dbHelper = new PostDbHelper(context);
    }

    /** 添加新帖子 */
    public long addPost(PostEntity post) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", post.getUserId());
        values.put("content", post.getContent());
        values.put("image_res_id", post.getImageResId());
        values.put("bar_name", post.getBarName());
        return db.insert("posts", null, values);
    }

    /** 获取所有帖子（按 id 降序排列） */
    public List<PostEntity> getAllPosts() {
        List<PostEntity> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("posts", null, null, null, null, null, "id DESC");

        while (cursor.moveToNext()) {
            PostEntity post = new PostEntity();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            post.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow("image_res_id")));
            post.setBarName(cursor.getString(cursor.getColumnIndexOrThrow("bar_name")));
            list.add(post);
        }
        cursor.close();
        return list;
    }

    /** 按用户 ID 获取该用户发的帖子 */
    public List<PostEntity> getPostsByUser(int userId) {
        List<PostEntity> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("posts", null, "user_id=?",
                new String[]{String.valueOf(userId)}, null, null, "id DESC");

        while (cursor.moveToNext()) {
            PostEntity post = new PostEntity();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            post.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow("image_res_id")));
            post.setBarName(cursor.getString(cursor.getColumnIndexOrThrow("bar_name")));
            list.add(post);
        }
        cursor.close();
        return list;
    }

    /** 按酒吧名搜索相关帖子（模糊匹配） */
    public List<PostEntity> searchByBar(String barName) {
        List<PostEntity> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("posts", null, "bar_name LIKE ?",
                new String[]{"%" + barName + "%"}, null, null, "id DESC");

        while (cursor.moveToNext()) {
            PostEntity post = new PostEntity();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            post.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow("image_res_id")));
            post.setBarName(cursor.getString(cursor.getColumnIndexOrThrow("bar_name")));
            list.add(post);
        }
        cursor.close();
        return list;
    }

    /** 删除指定帖子 */
    public void deletePostById(int postId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("posts", "id=?", new String[]{String.valueOf(postId)});
    }

    /** 清空所有帖子（用于调试） */
    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("posts", null, null);
    }

    /** 根据 postId 获取单个帖子 */
    public PostEntity getPostById(int postId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("posts", null, "id=?",
                new String[]{String.valueOf(postId)}, null, null, null);

        PostEntity post = null;
        if (cursor.moveToFirst()) {
            post = new PostEntity();
            post.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
            post.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow("user_id")));
            post.setContent(cursor.getString(cursor.getColumnIndexOrThrow("content")));
            post.setImageResId(cursor.getInt(cursor.getColumnIndexOrThrow("image_res_id")));
            post.setBarName(cursor.getString(cursor.getColumnIndexOrThrow("bar_name")));
        }
        cursor.close();
        return post;
    }

}
