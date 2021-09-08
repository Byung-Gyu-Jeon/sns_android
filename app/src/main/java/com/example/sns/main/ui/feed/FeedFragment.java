package com.example.sns.main.ui.feed;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sns.R;

public class FeedFragment extends Fragment {

    private FeedViewModel feedViewModel;
    RecyclerView recyclerView;
    private String token;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);
        View root = inflater.inflate(R.layout.fragment_feed, container, false);
        // 여기에다가 실제코드 작성
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview1);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerviewAdapter adapter = new recyclerviewAdapter(getContext());

        adapter.addItem(new item("홍길동","안녕",R.mipmap.ic_cat_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("철수","하이",R.mipmap.ic_lyan1_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("영희","우왕",R.mipmap.ic_lyan2_round, R.drawable.doughnut, 0));
        adapter.addItem(new item("바나나","굳",R.mipmap.ic_lyan3_round, new int[]{R.drawable.lyan3, R.drawable.doughnut,R.drawable.lyan6}, 1));
        adapter.addItem(new item("원숭이","ㅋ",R.mipmap.ic_lyan4_round, R.drawable.doughnut, 0));

        recyclerView.setAdapter(adapter);

        return root;
    }
}