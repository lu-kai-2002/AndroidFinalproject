package com.example.finalproject.ui.notifications;

public class NotificationEntity {
    private int id;
    private int receiverId;
    private int postId;
    private int commentId;
    private String contentPreview;
    private long timestamp;
    private int isRead; // 0 = unread, 1 = read

    public NotificationEntity() {}

    public NotificationEntity(int id,
                              int receiverId,
                              int postId,
                              int commentId,
                              String contentPreview,
                              long timestamp,
                              int isRead) {
        this.id = id;
        this.receiverId = receiverId;
        this.postId = postId;
        this.commentId = commentId;
        this.contentPreview = contentPreview;
        this.timestamp = timestamp;
        this.isRead = isRead;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }

    public int getPostId() { return postId; }
    public void setPostId(int postId) { this.postId = postId; }

    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public String getContentPreview() { return contentPreview; }
    public void setContentPreview(String contentPreview) { this.contentPreview = contentPreview; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getIsRead() { return isRead; }
    public void setIsRead(int isRead) { this.isRead = isRead; }
}