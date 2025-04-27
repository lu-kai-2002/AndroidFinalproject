package com.example.finalproject.ui.home;

import com.example.finalproject.ui.post.PostEntity;

/**
 * 临时使用的 Post 类（推荐后续全部替换为 PostEntity）
 */
public class Post {
    private String user;        // 用户名
    private String content;     // 发帖内容
    private int imageResId;     // 图片资源 ID（本地图片用）
    private int postId;         // 新增字段：帖子 ID
    private boolean liked = false;

    public Post(String user, String content, int imageResId, int id) {
        this.user = user;
        this.content = content;
        this.imageResId = imageResId;
        this.postId = id;
    }

    // Getter 方法
    public String getUsername() {
        return user;
    }

    public String getContent() {
        return content;
    }

    public int getPostId() {
        return postId;
    }

    public int getImageResId() {
        return imageResId;
    }

    // 点赞状态
    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    // 可选：将 Post 转换为 PostEntity（仅用于测试）
    public PostEntity toEntity(int userId) {
        PostEntity entity = new PostEntity();
        entity.setUserId(userId);
        entity.setContent(content);
        entity.setImageResId(imageResId);
        entity.setBarName("");  // 可留空或后续补充
        return entity;
    }
}
