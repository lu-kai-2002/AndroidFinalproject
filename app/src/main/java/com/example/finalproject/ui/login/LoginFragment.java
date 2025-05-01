package com.example.finalproject.ui.login;

import androidx.fragment.app.Fragment;

import com.example.finalproject.R;
import com.example.finalproject.ui.NotificationContainerFragment;
import com.example.finalproject.ui.auth.AuthManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LoginFragment extends Fragment {
    private LinearLayout loginContainer, registerContainer;
    private EditText etUserLogin, etPassLogin;
    private EditText etUserReg, etPassReg, etPassConfirm;
    private Button btnLogin, btnRegister;
    private TextView tvGoToRegister, tvGoToLogin;
    private loginDBhelper dbHelper;

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, parent, false);
        // 找到所有控件
        loginContainer    = v.findViewById(R.id.login_container);
        registerContainer = v.findViewById(R.id.register_container);

        etUserLogin  = v.findViewById(R.id.etUsernameLogin);
        etPassLogin  = v.findViewById(R.id.etPasswordLogin);
        etUserReg    = v.findViewById(R.id.etUsernameReg);
        etPassReg    = v.findViewById(R.id.etPasswordReg);
        etPassConfirm= v.findViewById(R.id.etPasswordConfirm);

        btnLogin     = v.findViewById(R.id.btnLogin);
        btnRegister  = v.findViewById(R.id.btnRegister);

        tvGoToRegister = v.findViewById(R.id.tvGoToRegister);
        tvGoToLogin    = v.findViewById(R.id.tvGoToLogin);

        dbHelper = new loginDBhelper(requireContext());

        // 点击“去注册”
        tvGoToRegister.setOnClickListener(x -> {
            loginContainer.setVisibility(View.GONE);
            registerContainer.setVisibility(View.VISIBLE);
        });
        // 点击“去登录”
        tvGoToLogin.setOnClickListener(x -> {
            registerContainer.setVisibility(View.GONE);
            loginContainer.setVisibility(View.VISIBLE);
        });

        // 注册逻辑
        btnRegister.setOnClickListener(x -> {
            String u = etUserReg.getText().toString().trim();
            String p = etPassReg.getText().toString();
            String c = etPassConfirm.getText().toString();
            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Toast.makeText(getContext(), "用户名和密码不能为空", Toast.LENGTH_SHORT).show();
            } else if (!p.equals(c)) {
                Toast.makeText(getContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                boolean ok = dbHelper.addUser(u, p);
                if (ok) {
                    Toast.makeText(getContext(), "注册成功，请登录", Toast.LENGTH_SHORT).show();
                    tvGoToLogin.performClick();
                } else {
                    Toast.makeText(getContext(), "用户名已存在", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 登录逻辑
        btnLogin.setOnClickListener(x -> {
            String u = etUserLogin.getText().toString().trim();
            String p = etPassLogin.getText().toString();
            if (TextUtils.isEmpty(u) || TextUtils.isEmpty(p)) {
                Toast.makeText(getContext(), "请输入用户名和密码", Toast.LENGTH_SHORT).show();
            } else if (dbHelper.checkUser(u, p)) {
                AuthManager.setLoggedIn(true);
                // 1) 查出当前用户的数据库 id
                int userId = dbHelper.getUserId(u);

                // 2) 存到 SharedPreferences
                SharedPreferences sp = requireContext()
                        .getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                sp.edit()
                        .putBoolean("is_logged_in", true)
                        .putInt("current_user_id", userId)
                        .apply();

                // 3) 通知切换 UI
                if (getParentFragment() instanceof NotificationContainerFragment) {
                    ((NotificationContainerFragment) getParentFragment()).loadChildFragment();
                }
            } else {
                Toast.makeText(getContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }
}