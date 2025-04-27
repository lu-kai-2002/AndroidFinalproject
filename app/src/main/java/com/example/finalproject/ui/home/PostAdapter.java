package com.example.finalproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.PostDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.ui.login.loginDBhelper;
import com.example.finalproject.ui.post.PostEntity;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private List<Post> postList;
    private Context context;

    public PostAdapter(List<Post> postList, Context context) {
        this.postList = postList;
        this.context = context;
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView textViewUser, textViewContent;
        ImageView imageViewPost, imageLike, imageAvatar;

        public PostViewHolder(View itemView) {
            super(itemView);
            textViewUser = itemView.findViewById(R.id.textViewUser);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            imageViewPost = itemView.findViewById(R.id.imageViewPost);
            imageLike = itemView.findViewById(R.id.imageLike);
            imageAvatar = itemView.findViewById(R.id.imageAvatar);
        }
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_card, parent, false);
        return new PostViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
//        Post post = postList.get(position);
//
//        holder.textViewUser.setText(post.getUsername());
//        holder.textViewContent.setText(post.getContent());
//        holder.imageViewPost.setImageResource(post.getImageResId());
//
//        // 👤 设置头像（可换成 post.getAvatarResId()）
//        holder.imageAvatar.setImageResource(R.drawable.ic_avatar_default);
//
//        // 设置当前点赞状态
//        holder.imageLike.setImageResource(
//                post.isLiked() ? R.drawable.ic_heart_red : R.drawable.ic_heart_gray
//        );
//
//        // 点击切换点赞状态
//        holder.imageLike.setOnClickListener(v -> {
//            boolean liked = post.isLiked();
//            post.setLiked(!liked);
//            holder.imageLike.setImageResource(
//                    post.isLiked() ? R.drawable.ic_heart_red : R.drawable.ic_heart_gray
//            );
//        });
//
//        // 点击Rainie跳转
//        holder.itemView.setOnClickListener(v -> {
//            if (post.getUsername().equals("Rainie")) {
//                Intent intent = new Intent(context, PostDetailActivity.class);
//                context.startActivity(intent);
//            }
//        });
//
//    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = postList.get(position);
        Log.d("position",String.valueOf(position));

        holder.textViewUser.setText(post.getUsername());
        holder.textViewContent.setText(post.getContent());
        holder.imageViewPost.setImageResource(post.getImageResId());
        holder.imageAvatar.setImageResource(R.drawable.ic_avatar_default);

        holder.imageLike.setImageResource(
                post.isLiked() ? R.drawable.ic_heart_red : R.drawable.ic_heart_gray
        );

        holder.imageLike.setOnClickListener(v -> {
            boolean liked = post.isLiked();
            post.setLiked(!liked);
            holder.imageLike.setImageResource(
                    post.isLiked() ? R.drawable.ic_heart_red : R.drawable.ic_heart_gray
            );
        });

        // 点击跳转详情页
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PostDetailActivity.class);
            intent.putExtra("post_id", post.getPostId());
            Log.d("postid",String.valueOf(post.getPostId()));// 传入数据库 ID
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
