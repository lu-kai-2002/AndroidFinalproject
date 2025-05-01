package com.example.finalproject.ui.notifications;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.finalproject.PostDetailActivity;
import com.example.finalproject.R;
import com.example.finalproject.databinding.FragmentNotificationsBinding;
import com.example.finalproject.ui.NotificationContainerFragment;
import com.example.finalproject.ui.auth.AuthManager;
import com.example.finalproject.ui.login.LoginFragment;


import java.util.List;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private NotificationAdapter          adapter;
    private NotificationDao              dao;

    /* ---------- life-cycle ---------- */

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup              container,
            Bundle                 savedInstanceState) {

        /* --- keep original ViewModel demo text (optional) --- */
        NotificationsViewModel vm =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();



        /* --- RecyclerView set-up --- */
        dao     = new NotificationDao(requireContext());
        adapter = new NotificationAdapter();
        adapter.setOnItemClickListener(this::openPost);

        binding.recyclerViewNotifications.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.recyclerViewNotifications.setAdapter(adapter);
        setUpLogoutButton();
        loadData();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();          // refresh when coming back
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /* ---------- helpers ---------- */

    /** read current userId exactly the same way as LoginFragment */
    private int getCurrentUserId() {
        SharedPreferences sp = requireContext()
                .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        return sp.getInt("current_user_id", -1);
    }

    /** (re)load list or show hint when not logged-in / empty */
    private void loadData() {
        int uid = getCurrentUserId();
        if (uid == -1) {
            // not logged-in
            binding.tvEmpty.setText("请先登录");
            binding.tvEmpty.setVisibility(View.VISIBLE);
            binding.recyclerViewNotifications.setVisibility(View.GONE);
            adapter.submitList(null);
            return;
        }

        // query DB
        var list = dao.getNotificationsByReceiver(uid);

        boolean empty = list.isEmpty();
        binding.tvEmpty.setText(empty ? "暂无消息" : "");
        binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.recyclerViewNotifications.setVisibility(empty ? View.GONE : View.VISIBLE);

        adapter.submitList(list);   // DiffUtil handles anim
    }
    /** init logout button */
    private void setUpLogoutButton() {
        binding.btnLogout.setOnClickListener(v -> {

            /* 1) clear login flags */
            SharedPreferences sp = requireContext()
                    .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            sp.edit()
                    .remove("is_logged_in")
                    .remove("current_user_id")
                    .apply();
            AuthManager.setLoggedIn(false);

            /* 2) toast */
            Toast.makeText(getContext(), "已退出登录", Toast.LENGTH_SHORT).show();

            /* 3) let parent (NotificationContainerFragment) reload child */
            Fragment parent = getParentFragment();
            if (parent instanceof NotificationContainerFragment) {
                ((NotificationContainerFragment) parent).loadChildFragment();
            } else {
                // 理论上进不到这里；保险起见直接 pop back-stack
                requireActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    /** open post detail & mark as read */
    private void openPost(NotificationEntity n) {
        dao.markAsRead(n.getId());

        Intent i = new Intent(getContext(), PostDetailActivity.class);
        i.putExtra("post_id", n.getPostId());
        startActivity(i);
    }
}

