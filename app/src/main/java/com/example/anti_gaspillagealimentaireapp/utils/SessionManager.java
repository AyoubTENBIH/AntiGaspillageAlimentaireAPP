package com.example.anti_gaspillagealimentaireapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;

/**
 * Gestion de la session utilisateur via SharedPreferences.
 */
public class SessionManager {

    private static final String PREFS_NAME = "saveat_session";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_USER_EMAIL = "user_email";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_COMMERCE_ID = "commerce_id";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public long getUserId() {
        return prefs.getLong(KEY_USER_ID, -1L);
    }

    public void setUserId(long value) {
        prefs.edit().putLong(KEY_USER_ID, value).apply();
    }

    public UserRole getUserRole() {
        try {
            String s = prefs.getString(KEY_USER_ROLE, null);
            return s == null ? null : UserRole.valueOf(s);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public void setUserRole(UserRole value) {
        prefs.edit().putString(KEY_USER_ROLE, value == null ? null : value.name()).apply();
    }

    public String getUserName() {
        return prefs.getString(KEY_USER_NAME, null);
    }

    public void setUserName(String value) {
        prefs.edit().putString(KEY_USER_NAME, value).apply();
    }

    public String getUserEmail() {
        return prefs.getString(KEY_USER_EMAIL, null);
    }

    public void setUserEmail(String value) {
        prefs.edit().putString(KEY_USER_EMAIL, value).apply();
    }

    public boolean isLoggedIn() {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setLoggedIn(boolean value) {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, value).apply();
    }

    public long getCommerceId() {
        return prefs.getLong(KEY_COMMERCE_ID, -1L);
    }

    public void setCommerceId(long value) {
        prefs.edit().putLong(KEY_COMMERCE_ID, value).apply();
    }

    public void saveSession(long userId, UserRole userRole, String userName, String userEmail) {
        setUserId(userId);
        setUserRole(userRole);
        setUserName(userName);
        setUserEmail(userEmail != null ? userEmail : "");
        setLoggedIn(true);
    }

    public void saveSession(long userId, UserRole userRole, String userName) {
        saveSession(userId, userRole, userName, "");
    }

    public void clearSession() {
        prefs.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_USER_ROLE)
            .remove(KEY_USER_NAME)
            .remove(KEY_USER_EMAIL)
            .remove(KEY_IS_LOGGED_IN)
            .remove(KEY_COMMERCE_ID)
            .apply();
    }

    public CurrentUser getCurrentUser() {
        try {
            if (!isLoggedIn() || getUserId() < 0) return null;
            UserRole role = getUserRole();
            if (role == null) return null;
            String name = getUserName();
            String email = getUserEmail();
            return new CurrentUser(getUserId(), role, name != null ? name : "", email != null ? email : "");
        } catch (Exception e) {
            return null;
        }
    }

    public static class CurrentUser {
        public final long id;
        public final UserRole role;
        public final String name;
        public final String email;

        public CurrentUser(long id, UserRole role, String name, String email) {
            this.id = id;
            this.role = role;
            this.name = name;
            this.email = email;
        }
    }
}
