package com.example.finalproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalproject.ui.dashboard.BarDao;
import com.example.finalproject.ui.dashboard.BarItem;

import java.util.Locale;

public class BarDetailActivity extends AppCompatActivity {
    private BarItem currentBar;
    private RatingBar ratingBar;
    private RatingBar userRatingBar;
    private TextView tvRatingValue;

    private BarDao barDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        barDao = new BarDao(this); // 初始化DAO
        setContentView(R.layout.activity_bar_detail);

        // 初始化视图
        ImageView ivDetail = findViewById(R.id.iv_detail);
        TextView tvName = findViewById(R.id.tv_detail_name);
        TextView tvAddress = findViewById(R.id.tv_detail_address);
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingValue = findViewById(R.id.tv_rating_value);
        if (currentBar != null) {
            ratingBar.setRating((float) currentBar.getAverageRating());
            tvRatingValue.setText(String.format(Locale.getDefault(),
                    "%.1f（%d人评分）",
                    currentBar.getAverageRating(),
                    currentBar.getRatingCount()));
        }
        userRatingBar = findViewById(R.id.user_rating_bar);
        Button btnSubmit = findViewById(R.id.btn_submit_rating);

        // 获取传递的数据
        currentBar = getIntent().getParcelableExtra("BAR_ITEM");
        if (currentBar != null) {
            // 设置基础信息
            ivDetail.setImageResource(currentBar.getImageResId());
            tvName.setText(currentBar.getName());
            tvAddress.setText(currentBar.getAddress());

            // 设置平均评分
            ratingBar.setRating((float) currentBar.getRating());
            tvRatingValue.setText(String.format("%.1f", currentBar.getRating()));

            // 加载用户历史评分
            SharedPreferences prefs = getSharedPreferences("BarRatings", MODE_PRIVATE);
            float userRating = prefs.getFloat(currentBar.getName(), 0);
            userRatingBar.setRating(userRating);
        }

        // 用户评分变更监听
        userRatingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                showRatingConfirmDialog((float) rating);
            }
        });

        // 提交按钮点击
        btnSubmit.setOnClickListener(v -> {
            saveRatingToStorage();
            updateOriginalData();
            finish(); // 返回上一页
        });
    }

    private void showRatingConfirmDialog(float newRating) {
        new AlertDialog.Builder(this)
                .setTitle("确认评分")
                .setMessage("您要给这个酒吧打 " + newRating + " 星吗？")
                .setPositiveButton("确定", (dialog, which) -> {
                    currentBar.updateRating(newRating);
                    // 实时更新显示
                    ratingBar.setRating(newRating);
                    tvRatingValue.setText(String.format("%.1f", newRating));
                })
                .setNegativeButton("取消", (dialog, which) -> {
                    // 取消时恢复原评分
                    userRatingBar.setRating(currentBar.getRating());
                })
                .show();
    }

    private void updateOriginalData() {
        // 将修改传递回Fragment
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_BAR", currentBar);
        setResult(RESULT_OK, resultIntent);
    }
    private void updateOriginalData(int position) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("UPDATED_BAR", currentBar);
        resultIntent.putExtra("ITEM_POSITION", position);
        setResult(RESULT_OK, resultIntent);
    }
    private void saveRatingToStorage() {
        // 替换为数据库操作
        barDao.updateRating(currentBar.getName(), currentBar.getRating());
    }
}
