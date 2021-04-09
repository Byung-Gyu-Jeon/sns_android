package com.example.sns.main.ui.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.sns.R;

public class MyPageFragment extends Fragment {

    private MyPageViewModel myPageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        myPageViewModel = new ViewModelProvider(this).get(MyPageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_mypage, container, false);


        return root;
    }
}