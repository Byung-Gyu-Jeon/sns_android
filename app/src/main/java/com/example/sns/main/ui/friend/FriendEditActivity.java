package com.example.sns.main.ui.friend;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sns.R;

public class FriendEditActivity extends AppCompatActivity {

    private FriendEditAdapter adapter;
    private FriendEditAdapterr adapterr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_edit);

        RecyclerView recyclerView = findViewById(R.id.recyclerVieww);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new FriendEditAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();


        RecyclerView recyclerView2 = findViewById(R.id.recyclerView22);

        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(this);
        recyclerView2.setLayoutManager(linearLayoutManager2);

        adapterr = new FriendEditAdapterr();
        recyclerView2.setAdapter(adapterr);

        adapterr.notifyDataSetChanged();




    }
}