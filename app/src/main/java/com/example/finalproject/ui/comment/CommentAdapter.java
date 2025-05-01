package com.example.finalproject.ui.comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.login.loginDBhelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * RecyclerView 适配器：用于展示每条评论。
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<CommentEntity> commentList;
    private Context context;

    public CommentAdapter(Context context, List<CommentEntity> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    public void setCommentList(List<CommentEntity> commentList) {
        this.commentList = commentList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        CommentEntity comment = commentList.get(position);

        // 获取用户名（通过 userId 查询 loginDBhelper）
        loginDBhelper loginHelper = new loginDBhelper(context);
        String username = loginHelper.getUsernameById(comment.getUserId());

        holder.textUsername.setText(username != null ? username : "匿名用户");
        holder.textContent.setText(comment.getContent());
        holder.textTimestamp.setText(formatTime(comment.getTimestamp()));
    }

    @Override
    public int getItemCount() {
        return commentList != null ? commentList.size() : 0;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        TextView textUsername, textContent, textTimestamp;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            textUsername = itemView.findViewById(R.id.textCommentUsername);
            textContent = itemView.findViewById(R.id.textCommentContent);
            textTimestamp = itemView.findViewById(R.id.textCommentTimestamp);
        }
    }

    /** 时间戳格式化为可读字符串 */
    private String formatTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 · HH:mm", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }
}
