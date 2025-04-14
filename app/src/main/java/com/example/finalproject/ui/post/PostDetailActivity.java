package com.example.finalproject.ui.post;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import android.view.View;
import android.widget.Button;


import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.BarDetailActivity;
import com.example.finalproject.R;

public class PostDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 设置顶部返回箭头
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("详情");
        }

        // 初始化页面控件
        ImageView imageView = findViewById(R.id.imageContent);
        TextView usernameView = findViewById(R.id.textUsername);
        TextView contentView = findViewById(R.id.textContent);

        // 显示内容
        usernameView.setText("Rainie");
        contentView.setText("🍹我心中的香港酒吧前三！！喝了78910杯…\n\n" +
                "一家很有爵士 feel 的小酒馆\n" +
                "适合约小姐妹来微醺\n" +
                "老板也会根据你的喜好来推荐");
        imageView.setImageResource(R.drawable.sample3);

        // 🔽 就在这里添加跳转按钮的逻辑 🔽
        Button buttonGoToBar = findViewById(R.id.buttonGoToBar);
        buttonGoToBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostDetailActivity.this, BarDetailActivity.class);
                intent.putExtra("bar_name", "爵士俱乐部");
                startActivity(intent);
            }
        });
    }


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
