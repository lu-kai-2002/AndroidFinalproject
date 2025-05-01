package com.example.finalproject.ui.comment;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论数据访问类，用于对评论进行增删查操作。
 */
public class CommentDao {

    private final CommentDbHelper dbHelper;

    public CommentDao(Context context) {
        dbHelper = new CommentDbHelper(context);
    }

    /** 添加评论 */
    public long addComment(CommentEntity comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CommentDbHelper.COL_POST_ID, comment.getPostId());
        values.put(CommentDbHelper.COL_USER_ID, comment.getUserId());
        values.put(CommentDbHelper.COL_CONTENT, comment.getContent());
        values.put(CommentDbHelper.COL_TIMESTAMP, comment.getTimestamp());
        return db.insert(CommentDbHelper.TABLE_NAME, null, values);
    }

    /** 查询指定帖子下的所有评论（按时间降序） */
    public List<CommentEntity> getCommentsByPostId(int postId) {
        List<CommentEntity> commentList = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                CommentDbHelper.TABLE_NAME,
                null,
                CommentDbHelper.COL_POST_ID + "=?",
                new String[]{String.valueOf(postId)},
                null,
                null,
                CommentDbHelper.COL_TIMESTAMP + " DESC"
        );

        while (cursor.moveToNext()) {
            CommentEntity comment = new CommentEntity();
            comment.setId(cursor.getInt(cursor.getColumnIndexOrThrow(CommentDbHelper.COL_ID)));
            comment.setPostId(cursor.getInt(cursor.getColumnIndexOrThrow(CommentDbHelper.COL_POST_ID)));
            comment.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(CommentDbHelper.COL_USER_ID)));
            comment.setContent(cursor.getString(cursor.getColumnIndexOrThrow(CommentDbHelper.COL_CONTENT)));
            comment.setTimestamp(cursor.getLong(cursor.getColumnIndexOrThrow(CommentDbHelper.COL_TIMESTAMP)));
            commentList.add(comment);
        }

        cursor.close();
        return commentList;
    }
    public CommentEntity getCommentById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor c = db.query(
                CommentDbHelper.TABLE_NAME,
                null,
                CommentDbHelper.COL_ID + "=?",
                new String[]{ String.valueOf(id) },
                null, null, null,
                "1"                       // LIMIT 1
        );
        CommentEntity result = null;
        if (c.moveToFirst()) result = cursor2entity(c);
        c.close();
        return result;                // may return null (not found)
    }
    private CommentEntity cursor2entity(Cursor c) {
        CommentEntity e = new CommentEntity();
        e.setId(        c.getInt   (c.getColumnIndexOrThrow(CommentDbHelper.COL_ID)));
        e.setPostId(    c.getInt   (c.getColumnIndexOrThrow(CommentDbHelper.COL_POST_ID)));
        e.setUserId(    c.getInt   (c.getColumnIndexOrThrow(CommentDbHelper.COL_USER_ID)));
        e.setContent(   c.getString(c.getColumnIndexOrThrow(CommentDbHelper.COL_CONTENT)));
        e.setTimestamp( c.getLong  (c.getColumnIndexOrThrow(CommentDbHelper.COL_TIMESTAMP)));
        return e;
    }
}
