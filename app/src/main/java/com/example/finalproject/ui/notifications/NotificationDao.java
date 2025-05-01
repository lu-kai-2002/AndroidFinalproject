package com.example.finalproject.ui.notifications;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
public class NotificationDao {
    private final SQLiteDatabase db;

    public NotificationDao(Context context) {
        NotificationDbHelper helper = new NotificationDbHelper(context);
        db = helper.getWritableDatabase();
    }

    public long addNotification(NotificationEntity n) {
        ContentValues v = new ContentValues();
        v.put("receiverId", n.getReceiverId());
        v.put("postId", n.getPostId());
        v.put("commentId", n.getCommentId());
        v.put("contentPreview", n.getContentPreview());
        v.put("timestamp", n.getTimestamp());
        v.put("isRead", n.getIsRead());
        return db.insert(NotificationDbHelper.TABLE_NAME, null, v);
    }

    public List<NotificationEntity> getNotificationsByReceiver(int uid) {
        List<NotificationEntity> list = new ArrayList<>();
        Cursor c = db.query(NotificationDbHelper.TABLE_NAME, null,
                "receiverId = ?",
                new String[]{String.valueOf(uid)},
                null, null,
                "timestamp DESC");
        if (c != null) {
            while (c.moveToNext()) {
                NotificationEntity n = new NotificationEntity();
                n.setId(c.getInt(c.getColumnIndexOrThrow("id")));
                n.setReceiverId(c.getInt(c.getColumnIndexOrThrow("receiverId")));
                n.setPostId(c.getInt(c.getColumnIndexOrThrow("postId")));
                n.setCommentId(c.getInt(c.getColumnIndexOrThrow("commentId")));
                n.setContentPreview(c.getString(c.getColumnIndexOrThrow("contentPreview")));
                n.setTimestamp(c.getLong(c.getColumnIndexOrThrow("timestamp")));
                n.setIsRead(c.getInt(c.getColumnIndexOrThrow("isRead")));
                list.add(n);
            }
            c.close();
        }
        return list;
    }

    public void markAsRead(int id) {
        ContentValues v = new ContentValues();
        v.put("isRead", 1);
        db.update(NotificationDbHelper.TABLE_NAME, v,
                "id = ?", new String[]{String.valueOf(id)});
    }

    public int getUnreadCount(int uid) {
        Cursor c = db.rawQuery("SELECT COUNT(*) FROM " + NotificationDbHelper.TABLE_NAME +
                        " WHERE receiverId = ? AND isRead = 0",
                new String[]{String.valueOf(uid)});
        int count = 0;
        if (c != null) {
            if (c.moveToFirst()) {
                count = c.getInt(0);
            }
            c.close();
        }
        return count;
    }
}