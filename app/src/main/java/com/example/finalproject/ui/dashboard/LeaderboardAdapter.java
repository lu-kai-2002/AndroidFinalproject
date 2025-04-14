package com.example.finalproject.ui.dashboard;

import android.util.Log;
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

    public interface OnItemClickListener {
        void onItemClick(BarItem item, int position);
    }

    public LeaderboardAdapter(List<BarItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView ivBar;
        final TextView tvName;
        final TextView tvAddress;
        final TextView tvRating;

        public ViewHolder(View view) {
            super(view);
            ivBar = view.findViewById(R.id.iv_bar);
            tvName = view.findViewById(R.id.tv_name);
            tvAddress = view.findViewById(R.id.tv_address);
            tvRating = view.findViewById(R.id.tv_rating);
        }

        public void bindClick(final BarItem item, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(item, position);
                }
            });
        }
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
        Log.d("BindDebug", "绑定数据: " + item.getName() +
                " 平均分=" + item.getAverageRating() +
                " 次数=" + item.getRatingCount());
        double point=item.getAverageRating();
        holder.ivBar.setImageResource(item.getImageResId());
        holder.tvName.setText(item.getName());
        holder.tvAddress.setText(item.getAddress());
        holder.tvRating.setText(String.format("评分：%.1f（%d人）",
                point,
                item.getRatingCount()));
        holder.bindClick(item, listener); // 绑定点击事件
    }

    // 添加数据更新方法
    public void updateData(List<BarItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}