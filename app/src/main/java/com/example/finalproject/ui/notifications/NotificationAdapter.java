package com.example.finalproject.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.comment.CommentDao;
import com.example.finalproject.ui.comment.CommentEntity;
import com.example.finalproject.ui.login.loginDBhelper;

import java.util.HashMap;
import java.util.Map;

public class NotificationAdapter
        extends ListAdapter<NotificationEntity, NotificationAdapter.Holder> {

    /* ---------- click listener ---------- */
    public interface OnItemClickListener { void onItemClick(NotificationEntity n); }
    private OnItemClickListener listener;
    public  void setOnItemClickListener(OnItemClickListener l) { listener = l; }

    /* ---------- tiny cache {userId → username} ---------- */
    private final Map<Integer, String> userCache = new HashMap<>();

    public NotificationAdapter() {
        super(new DiffUtil.ItemCallback<NotificationEntity>() {
            @Override public boolean areItemsTheSame(@NonNull NotificationEntity a,
                                                     @NonNull NotificationEntity b) {
                return a.getId() == b.getId();
            }
            @Override public boolean areContentsTheSame(@NonNull NotificationEntity a,
                                                        @NonNull NotificationEntity b) {
                return a.getIsRead() == b.getIsRead()
                        && a.getContentPreview().equals(b.getContentPreview())
                        && a.getTimestamp() == b.getTimestamp();
            }
        });
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup p, int vType) {
        View v = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_notification, p, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder h, int pos) {
        h.bind(getItem(pos));
    }

    /* ---------- ViewHolder ---------- */
    class Holder extends RecyclerView.ViewHolder {
        private final TextView tvContent;
        private final TextView tvTime;

        Holder(@NonNull View v) {
            super(v);
            tvContent = v.findViewById(R.id.tv_notification_content); // 注意 id
            tvTime    = v.findViewById(R.id.tv_notification_time);
        }

        void bind(final NotificationEntity n) {
            /* ---- 1) find commenter name (with cache) ---- */
            String commenterName;
            CommentDao cDao = new CommentDao(itemView.getContext());
            CommentEntity c = cDao.getCommentById(n.getCommentId());
            if (c == null) {           // safety check
                commenterName = "有人";
            } else {
                int    uid   = c.getUserId();
                String name  = userCache.get(uid);
                if (name == null) {    // first time, query DB
                    name = new loginDBhelper(itemView.getContext())
                            .getUsernameById(uid);
                    if (name == null || name.isEmpty()) name = "有人";
                    userCache.put(uid, name);
                }
                commenterName = name;
            }

            /* ---- 2) build display text ---- */
            String msg = commenterName + " 评论了你：" + n.getContentPreview();
            tvContent.setText(msg);

            /* ---- 3) time + read status ---- */
            tvTime.setText(TimeUtil.getRelativeTime(n.getTimestamp()));
            itemView.setAlpha(n.getIsRead() == 1 ? 0.5f : 1.0f);

            /* ---- 4) click ---- */
            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onItemClick(n);
            });
        }
    }
}