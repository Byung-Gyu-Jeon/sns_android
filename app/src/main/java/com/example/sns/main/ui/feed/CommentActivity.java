package com.example.sns.main.ui.feed;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.sns.R;

public class CommentActivity extends AppCompatActivity {

    private CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new CommentAdapter();
        recyclerView.setAdapter(adapter);

        adapter.addItem(new Data2(R.drawable.cat,"전병규","끝까지 찿아다녀도 목숨이 있는 때까지 방황하여도 보이는 것은 거친 모래뿐일 것이다"));
        adapter.addItem(new Data2(R.drawable.cat2,"이동언","강남대학교 컴퓨터 공학부 4학년 졸업작품 제작중입니다"));

        adapter.notifyDataSetChanged();
    }
}