package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ui.comment.CommentAdapter;
import com.example.finalproject.ui.comment.CommentDao;
import com.example.finalproject.ui.comment.CommentEntity;
import com.example.finalproject.ui.dashboard.BarDao;
import com.example.finalproject.ui.dashboard.BarItem;
import com.example.finalproject.ui.login.loginDBhelper;
import com.example.finalproject.ui.post.PostDao;
import com.example.finalproject.ui.post.PostEntity;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    private PostEntity currentPost;
    private int postId;
    private PostDao postDao;

    private RecyclerView recyclerViewComments;
    private EditText editComment;
    private Button btnSubmitComment;

    private CommentAdapter commentAdapter;
    private CommentDao commentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 获取 postId
        postId = getIntent().getIntExtra("post_id", -1);
        if (postId == -1) {
            Toast.makeText(this, "无法加载帖子：ID 错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 获取帖子对象
        postDao = new PostDao(this);
        currentPost = postDao.getPostById(postId);
        if (currentPost == null) {
            Toast.makeText(this, "找不到帖子内容", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 设置返回按钮
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("详情");
        }

        // 初始化控件
        ImageView imageView = findViewById(R.id.imageContent);
        TextView usernameView = findViewById(R.id.textUsername);
        TextView contentView = findViewById(R.id.textContent);
        Button buttonGoToBar = findViewById(R.id.buttonGoToBar);

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        editComment = findViewById(R.id.editComment);
        btnSubmitComment = findViewById(R.id.btnSubmitComment);

        // 显示作者昵称
        loginDBhelper loginHelper = new loginDBhelper(this);
        String username = loginHelper.getUsernameById(currentPost.getUserId());
        usernameView.setText(username != null ? username : "未知用户");

        // 显示内容与图片
        contentView.setText(currentPost.getContent());
        imageView.setImageResource(currentPost.getImageResId());

        // 跳转酒吧页面
        String barName = currentPost.getBarName();
        if (barName != null && !barName.isEmpty()) {
            buttonGoToBar.setVisibility(View.VISIBLE);
            buttonGoToBar.setText("去【" + barName + "】看看");
            buttonGoToBar.setOnClickListener(v -> {
                BarDao barDao = new BarDao(PostDetailActivity.this);
                BarItem barItem = barDao.getBarByName(barName);
                if (barItem != null) {
                    Intent intent = new Intent(PostDetailActivity.this, BarDetailActivity.class);
                    intent.putExtra("BAR_ITEM", barItem);
                    intent.putExtra("ITEM_POSITION", -1);
                    startActivity(intent);
                } else {
                    Toast.makeText(PostDetailActivity.this, "找不到对应酒吧", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            buttonGoToBar.setVisibility(View.GONE);
        }

        // 初始化评论区
        commentDao = new CommentDao(this);
        commentAdapter = new CommentAdapter(this, commentDao.getCommentsByPostId(postId));
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);

        btnSubmitComment.setOnClickListener(v -> {
            String content = editComment.getText().toString().trim();
            if (content.isEmpty()) {
                Toast.makeText(this, "评论不能为空", Toast.LENGTH_SHORT).show();
                return;
            }

            int userId = getCurrentUserId();
            if (userId == -1) {
                Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
                return;
            }

            CommentEntity comment = new CommentEntity();
            comment.setPostId(postId);
            comment.setUserId(userId);
            comment.setContent(content);
            comment.setTimestamp(System.currentTimeMillis());

            long result = commentDao.addComment(comment);
            if (result != -1) {
                Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
                editComment.setText("");
                refreshCommentList();
            } else {
                Toast.makeText(this, "评论失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /** ✅ 正确读取已登录用户 ID（与帖子发帖方式保持一致） */
    private int getCurrentUserId() {
        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return sp.getInt("current_user_id", -1);
    }

    /** 刷新评论列表 */
    private void refreshCommentList() {
        List<CommentEntity> comments = commentDao.getCommentsByPostId(postId);
        commentAdapter.setCommentList(comments);
    }

    /** 返回按钮 */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
