package com.example.sns.main.ui.friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sns.R;

public class FriendList extends AppCompatActivity {

    private FriendAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        RecyclerView recyclerView = findViewById(R.id.recyclerview2);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FriendAdapter();
        recyclerView.setAdapter(adapter);

        adapter.addItem(new Data3("전병규"));

        adapter.notifyDataSetChanged();
    }
}