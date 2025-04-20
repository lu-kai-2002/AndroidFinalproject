package com.example.finalproject.ui.auth;






public class AuthManager {
    // 只保存在进程内存，进程被杀掉后就重置为 false
    private static boolean loggedIn = false;

    /** 查询登录状态 */
    public static boolean isLoggedIn() {
        return loggedIn;
    }

    /** 设置登录状态 */
    public static void setLoggedIn(boolean value) {
        loggedIn = value;
    }

    /** 登出 */
    public static void logout() {
        loggedIn = false;
    }
}

