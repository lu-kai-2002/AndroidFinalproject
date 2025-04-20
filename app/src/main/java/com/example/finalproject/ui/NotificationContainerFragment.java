package com.example.finalproject.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.finalproject.R;
import com.example.finalproject.ui.auth.AuthManager;
import com.example.finalproject.ui.login.LoginFragment;
import com.example.finalproject.ui.notifications.NotificationsFragment;

public class NotificationContainerFragment extends Fragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle s) {
        return inflater.inflate(R.layout.fragment_notification_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // 首次加载
        loadChildFragment();
    }

    /** 根据状态载入 LoginFragment 或 MessageFragment */
    public void loadChildFragment() {
        Fragment child = AuthManager.isLoggedIn()
                ? new NotificationsFragment()
                : new LoginFragment();
        getChildFragmentManager().beginTransaction()
                .replace(R.id.child_container, child)
                .commit();
    }
}