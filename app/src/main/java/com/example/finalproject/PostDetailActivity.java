package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.ui.dashboard.BarDao;
import com.example.finalproject.ui.dashboard.BarItem;
import com.example.finalproject.ui.login.loginDBhelper;
import com.example.finalproject.ui.post.PostDao;
import com.example.finalproject.ui.post.PostDbHelper;
import com.example.finalproject.ui.post.PostEntity;



public class PostDetailActivity extends AppCompatActivity {
    private PostEntity currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 设置顶部返回箭头
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("详情");
        }

        // 获取传入的 postId
        int postId = getIntent().getIntExtra("post_id", -1);
        if (postId == -1) {
            Toast.makeText(this, "无法加载帖子：ID 错误", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 从数据库中获取帖子
        PostDao postDao = new PostDao(this);
        currentPost = postDao.getPostById(postId);

        if (currentPost == null) {
            Toast.makeText(this, "找不到帖子内容", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // 初始化页面控件
        ImageView imageView = findViewById(R.id.imageContent);
        TextView usernameView = findViewById(R.id.textUsername);
        TextView contentView = findViewById(R.id.textContent);

//        // 显示内容
//        // 从数据库中获得用户名和头像
//        usernameView.setText("Rainie");
//        //从数据库中获得内容（由用户输入）
//        contentView.setText("🍹我心中的香港酒吧前三！！喝了78910杯…\n\n" +
//                "一家很有爵士 feel 的小酒馆\n" +
//                "适合约小姐妹来微醺\n" +
//                "老板也会根据你的喜好来推荐");
//        imageView.setImageResource(R.drawable.sample3);

        // 加载用户昵称
        loginDBhelper loginHelper = new loginDBhelper(this);
        String username = loginHelper.getUsernameById(currentPost.getUserId());
        usernameView.setText(username != null ? username : "未知用户");

        // 加载内容和图片
        contentView.setText(currentPost.getContent());
        imageView.setImageResource(currentPost.getImageResId());

        // 添加跳转按钮的逻辑
        Button buttonGoToBar = findViewById(R.id.buttonGoToBar);

        // 处理跳转到酒吧按钮
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
            buttonGoToBar.setVisibility(View.GONE); // 没有关联酒吧则隐藏按钮
        }
    }

//        buttonGoToBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 从数据库中查找“爵士俱乐部”
//                BarDao barDao = new BarDao(PostDetailActivity.this);
//                // 从数据库中（用户post输入）
//                BarItem barItem = barDao.getBarByName("爵士俱乐部");
//
//                if (barItem != null) {
//                    Intent intent = new Intent(PostDetailActivity.this, BarDetailActivity.class);
//                    intent.putExtra("BAR_ITEM", barItem);  // 传完整对象
//                    intent.putExtra("ITEM_POSITION", -1);  // 表示不是从排行榜进入的
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(PostDetailActivity.this, "找不到对应酒吧", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }

    // 返回箭头点击逻辑
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();  // 关闭当前页面，返回上一页
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}

