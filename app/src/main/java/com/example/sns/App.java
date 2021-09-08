package com.example.sns;

import android.app.Application;

import com.example.sns.Model.SharedPreferenceManager;

public class App extends Application {
    public static SharedPreferenceManager sharedPreferenceManager;

    @Override
    public void onCreate() {
        sharedPreferenceManager = SharedPreferenceManager.getInstance(getApplicationContext());
        super.onCreate();
    }
}
