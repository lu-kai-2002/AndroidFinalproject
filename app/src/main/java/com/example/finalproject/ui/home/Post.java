package com.example.finalproject.ui.home;

public class Post {
    private String user;        // 用户名
    private String content;     // 发帖内容
    private int imageResId;     // 图片资源 ID（本地图片用）
    private boolean liked = false;

    public Post(String user, String content, int imageResId) {
        this.user = user;
        this.content = content;
        this.imageResId = imageResId;
    }

    // Getter 方法
    public String getUsername() {
        return user;
    }

    public String getContent() {
        return content;
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
}
