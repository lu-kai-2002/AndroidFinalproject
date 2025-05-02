package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.ui.dashboard.BarDao;
import com.example.finalproject.ui.dashboard.BarItem;

import java.util.Locale;

public class BarDetailActivity extends AppCompatActivity {
    private BarItem currentBar;
    private RatingBar ratingBar;      // 显示平均评分
    private RatingBar userRatingBar; // 用户评分输入
    private TextView tvRatingValue;
    private BarDao barDao;
    private int itemPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_detail);
        setTitle("详情");

        // 必须先初始化DAO和获取数据
        barDao = new BarDao(this);
        currentBar = getIntent().getParcelableExtra("BAR_ITEM");
        itemPosition = getIntent().getIntExtra("ITEM_POSITION", -1);

        // 初始化视图
        initViews();
        setupRatingSystem();
    }

    private void initViews() {
        ImageView ivDetail = findViewById(R.id.iv_detail);
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvAddress = findViewById(R.id.tv_detail_address);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tv_rating_value);
        userRatingBar = findViewById(R.id.user_rating_bar);
        Button btnSubmit = findViewById(R.id.btn_submit_rating);

        if (currentBar != null) {
            // 设置基础信息
            ivDetail.setImageResource(currentBar.getImageResId());
            tvName.setText(currentBar.getName());
            tvAddress.setText(currentBar.getAddress());

            // 显示当前平均评分
            updateRatingDisplay();
        }
    }

    private void setupRatingSystem() {
        // 设置评分变更监听
        userRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                showRatingConfirmDialog(rating);
            }
        });

        // 提交按钮点击事件
        findViewById(R.id.btn_submit_rating).setOnClickListener(v -> {
            float newRating = userRatingBar.getRating();
            if (newRating > 0) {
                submitRating(newRating);
            } else {
                Toast.makeText(this, "请先进行评分", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showRatingConfirmDialog(float newRating) {
        new AlertDialog.Builder(this)
                .setTitle("确认评分")
                .setMessage(String.format("您要给 %s 打 %.1f 星吗？", currentBar.getName(), newRating))
                .setPositiveButton("确定", (dialog, which) -> {
                    // 用户确认后不做即时更新，等点击提交按钮再处理
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    userRatingBar.setRating(0); // 取消时重置为未评分状态
                })
                .show();
    }

    private void submitRating(float newRating) {
        // 1. 更新数据库
        barDao.addNewRating(currentBar.getName(), newRating);

        // 2. 更新当前对象
        currentBar.addRating(newRating);

        // 3. 刷新显示
        updateRatingDisplay();

        // 4. 返回结果
        returnToFragment();
    }

    private void updateRatingDisplay() {
        ratingBar.setRating((float) currentBar.getAverageRating());
        tvRatingValue.setText(String.format(Locale.getDefault(),
                "%.1f（%d人评分）",
                currentBar.getAverageRating(),
                currentBar.getRatingCount()));
    }

    private void returnToFragment() {
        if (itemPosition >= 0) {
            // 从排行榜页面进入，回传更新数据
            Intent resultIntent = new Intent();
            resultIntent.putExtra("UPDATED_BAR", currentBar);
            resultIntent.putExtra("ITEM_POSITION", itemPosition);
            setResult(RESULT_OK, resultIntent);
        }
        // 否则只是从帖子页进入，直接关闭即可
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();  // 正常返回上一页面（PostDetailActivity）
        return true;
    }


}
