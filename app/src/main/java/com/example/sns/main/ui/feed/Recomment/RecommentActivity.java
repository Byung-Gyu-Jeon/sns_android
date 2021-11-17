package com.example.sns.main.ui.feed.Recomment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sns.R;

public class RecommentActivity extends AppCompatActivity {

    private RecommentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recomment);

        RecyclerView recyclerView = findViewById(R.id.recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecommentAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}