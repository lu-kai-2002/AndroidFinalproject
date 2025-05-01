package com.example.finalproject.ui.comment;

/**
 * 评论实体类，表示某个帖子下的单条评论。
 */
public class CommentEntity {

    private int id;           // 评论ID，自增主键
    private int postId;       // 所属帖子ID
    private int userId;       // 评论用户ID
    private String content;   // 评论内容
    private long timestamp;   // 评论时间戳（毫秒）

    // 构造函数（无参）
    public CommentEntity() {}

    // 构造函数（全参）
    public CommentEntity(int id, int postId, int userId, String content, long timestamp) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.timestamp = timestamp;
    }

    // Getter 和 Setter 方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
