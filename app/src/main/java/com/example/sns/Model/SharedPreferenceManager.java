package com.example.sns.Model;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceManager {
    private static SharedPreferenceManager instance;
    private SharedPreferences prefs;
    private final String PREFERENCES_NAME = "preference";
    private final String TOKEN = "token";
    private final String REFRESH_TOKEN = "refreshToken";
    private final String ACCESS_TOKEN_EXPIRES_IN = "accessTokenExpiresIn";
    private final String REFRESH_TOKEN_EXPIRES_IN = "refreshTokenExpiresIn";
    private static SharedPreferences.Editor prefsEditor;

    private SharedPreferenceManager() {}

    private SharedPreferenceManager(Context context) {
        prefs = context.getSharedPreferences(PREFERENCES_NAME, context.MODE_PRIVATE);
        prefsEditor = prefs.edit();
    }

    public static SharedPreferenceManager getInstance(Context context) {
        if (instance == null)
            instance = new SharedPreferenceManager(context);
        return instance;
    }

    public void setToken(String value) {
        prefsEditor.putString(TOKEN, value);
        prefsEditor.commit();
    }

    public String getToken() {
        return prefs.getString(TOKEN, "");
    }

    public void setRefreshToken(String value){
        prefsEditor.putString(REFRESH_TOKEN, value);
        prefsEditor.commit();
    }

    public String getRefreshToken() {
        return prefs.getString(REFRESH_TOKEN, "");
    }

    public void setAccessTokenExpiresIn(Long value) {
        prefsEditor.putLong(ACCESS_TOKEN_EXPIRES_IN, value);
        prefsEditor.commit();
    }

    public Long getAccessTokenExpiresIn() {return prefs.getLong(ACCESS_TOKEN_EXPIRES_IN, 0); }

    public void setRefreshTokenExpiresIn(Long value) {
        prefsEditor.putLong(REFRESH_TOKEN_EXPIRES_IN, value);
        prefsEditor.commit();
    }

    public Long getRefreshTokenExpiresIn() {return prefs.getLong(REFRESH_TOKEN_EXPIRES_IN, 0); }
}
