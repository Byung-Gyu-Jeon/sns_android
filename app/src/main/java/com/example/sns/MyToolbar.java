package com.example.sns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MyToolbar {

    public static void show(AppCompatActivity activity, String title, boolean upButton) {
        Toolbar toolbar = activity.findViewById(R.id.main_toolbar);
        activity.setSupportActionBar(toolbar);
        if(activity.getSupportActionBar() != null){
            activity.getSupportActionBar().setTitle(title);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(upButton);
            if(upButton)
                activity.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_arrows_back_direction_left_navigation_right_icon_123237);
        }
    }
}
