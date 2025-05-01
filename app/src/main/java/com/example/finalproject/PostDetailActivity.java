package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.finalproject.ui.notifications.NotificationDao;
import com.example.finalproject.ui.notifications.NotificationEntity;
import com.example.finalproject.ui.post.PostDao;
import com.example.finalproject.ui.post.PostEntity;

import java.util.List;

public class PostDetailActivity extends AppCompatActivity {

    /* ---------- fields ---------- */
    private PostEntity  currentPost;
    private int         postId;
    private PostDao     postDao;

    private RecyclerView recyclerViewComments;
    private EditText     editComment;
    private Button       btnSubmitComment;

    private CommentAdapter commentAdapter;
    private CommentDao     commentDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        /* ---------- get postId from intent ---------- */
        postId = getIntent().getIntExtra("post_id", -1);
        if (postId == -1) {
            Toast.makeText(this, "无法加载帖子：ID 错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        /* ---------- fetch post from DB ---------- */
        postDao     = new PostDao(this);
        currentPost = postDao.getPostById(postId);
        if (currentPost == null) {
            Toast.makeText(this, "找不到帖子内容", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        /* ---------- action-bar back button ---------- */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("详情");
        }

        /* ---------- view refs ---------- */
        ImageView imageView       = findViewById(R.id.imageContent);
        TextView  usernameView    = findViewById(R.id.textUsername);
        TextView  contentView     = findViewById(R.id.textContent);
        Button    buttonGoToBar   = findViewById(R.id.buttonGoToBar);

        recyclerViewComments = findViewById(R.id.recyclerViewComments);
        editComment          = findViewById(R.id.editComment);
        btnSubmitComment     = findViewById(R.id.btnSubmitComment);

        /* ---------- fill author name ---------- */
        loginDBhelper loginHelper = new loginDBhelper(this);
        String username = loginHelper.getUsernameById(currentPost.getUserId());
        usernameView.setText(username != null ? username : "未知用户");

        /* ---------- fill content ---------- */
        contentView.setText(currentPost.getContent());
        imageView.setImageResource(currentPost.getImageResId());

        /* ---------- "Go to Bar" button ---------- */
        String barName = currentPost.getBarName();
        if (barName != null && !barName.isEmpty()) {
            buttonGoToBar.setVisibility(View.VISIBLE);
            buttonGoToBar.setText("去【" + barName + "】看看");
            buttonGoToBar.setOnClickListener(v -> {
                BarDao barDao = new BarDao(PostDetailActivity.this);
                BarItem barItem = barDao.getBarByName(barName);
                if (barItem != null) {
                    Intent i = new Intent(PostDetailActivity.this, BarDetailActivity.class);
                    i.putExtra("BAR_ITEM",      barItem);
                    i.putExtra("ITEM_POSITION", -1);
                    startActivity(i);
                } else {
                    Toast.makeText(PostDetailActivity.this, "找不到对应酒吧", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            buttonGoToBar.setVisibility(View.GONE);
        }

        /* ---------- comment list ---------- */
        commentDao     = new CommentDao(this);
        commentAdapter = new CommentAdapter(this, commentDao.getCommentsByPostId(postId));
        recyclerViewComments.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComments.setAdapter(commentAdapter);

        /* ---------- submit comment ---------- */
        btnSubmitComment.setOnClickListener(v -> submitComment());
    }

    /* ========== helpers ========== */

    /** read current logged-in userId from SharedPreferences */
    private int getCurrentUserId() {
        SharedPreferences sp = getSharedPreferences("app_prefs", MODE_PRIVATE);
        return sp.getInt("current_user_id", -1);
    }

    /** refresh comment recycler */
    private void refreshCommentList() {
        List<CommentEntity> comments = commentDao.getCommentsByPostId(postId);
        commentAdapter.setCommentList(comments);
    }

    /** handle toolbar back */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* ========== NEW: submit + notification ========== */
    private void submitComment() {
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

        /* ----- write comment ----- */
        CommentEntity c = new CommentEntity();
        c.setPostId(postId);
        c.setUserId(userId);
        c.setContent(content);
        c.setTimestamp(System.currentTimeMillis());

        long commentRowId = commentDao.addComment(c);
        if (commentRowId == -1) {
            Toast.makeText(this, "评论失败", Toast.LENGTH_SHORT).show();
            return;
        }

        /* ----- success: ui ----- */
        Toast.makeText(this, "评论成功", Toast.LENGTH_SHORT).show();
        editComment.setText("");
        refreshCommentList();

        /* ----- NEW: write notification (skip if author self-comment) ----- */
        if (userId != currentPost.getUserId()) {
            NotificationEntity n = new NotificationEntity();
            n.setReceiverId(currentPost.getUserId());           // post author
            n.setPostId(postId);
            n.setCommentId((int) commentRowId);
            n.setContentPreview(
                    content.length() > 20 ? content.substring(0, 20) + "…" : content);
            n.setTimestamp(System.currentTimeMillis());
            n.setIsRead(0);
            Log.d("commentsuccess",n.getContentPreview());
            NotificationDao notiDao = new NotificationDao(this);
            notiDao.addNotification(n);
            // 👉 若要系统推送，可在此再调 NotificationCompat.Builder…
        }
    }
}
