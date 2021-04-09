package com.example.sns.main.ui.friend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.sns.R;

public class FriendFragment extends Fragment {

    private FriendViewModel friendViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        friendViewModel =
                new ViewModelProvider(this).get(FriendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_friend, container, false);

        return root;
    }
}