package com.example.finalproject.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {
    private static final String PREFS_NAME = "user_session";
    private static final String KEY_UID = "uid";

    private static UserSession instance;
    private final SharedPreferences prefs;

    private UserSession(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized UserSession getInstance(Context ctx) {
        if (instance == null) {
            instance = new UserSession(ctx);
        }
        return instance;
    }

    public void login(int uid) { prefs.edit().putInt(KEY_UID, uid).apply(); }

    public void logout() { prefs.edit().remove(KEY_UID).apply(); }

    public int getCurrentUserId() { return prefs.getInt(KEY_UID, -1); }
}