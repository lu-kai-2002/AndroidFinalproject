package com.example.finalproject.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private final List<BarItem> items;
    private final OnItemClickListener listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivBar;
        public TextView tvName;
        public TextView tvAddress;
        public TextView tvRating;

        public ViewHolder(View view) {
            super(view);
            ivBar = view.findViewById(R.id.iv_bar);
            tvName = view.findViewById(R.id.tv_name);
            tvAddress = view.findViewById(R.id.tv_address);
            tvRating = view.findViewById(R.id.tv_rating);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(BarItem item);
    }

    // 移除旧的构造函数，仅保留新的构造函数
    public LeaderboardAdapter(List<BarItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BarItem item = items.get(position);
        holder.ivBar.setImageResource(item.getImageResId());
        holder.tvName.setText(item.getName());
        holder.tvAddress.setText(item.getAddress());
        holder.tvRating.setText(String.format("评分：%.1f", item.getRating()));

        // 添加点击事件
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}