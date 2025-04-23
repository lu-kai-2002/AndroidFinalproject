package com.example.finalproject.ui.post;

/**
 * 帖子实体类，表示一条用户发布的帖子。
 * 每条帖子与一个用户关联，并可关联一个酒吧名。
 */
public class PostEntity {

    private int id;              // 帖子ID，自增主键
    private int userId;          // 发帖用户ID（来自 loginDBhelper 用户表）
    private String content;      // 帖子内容
    private int imageResId;      // 图片资源ID（本地图片资源）
    private String barName;      // 关联的酒吧名（可用于跳转）

    // 构造函数
    public PostEntity(int id, int userId, String content, int imageResId, String barName) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.imageResId = imageResId;
        this.barName = barName;
    }

    // 无参构造函数（可用于查询或默认创建）
    public PostEntity() {}

    // Getter 和 Setter 方法

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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


    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }


    public String getBarName() {
        return barName;
    }

    public void setBarName(String barName) {
        this.barName = barName;
    }
}
