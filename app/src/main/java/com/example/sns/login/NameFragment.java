package com.example.sns.login;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sns.R;
import com.example.sns.databinding.FragmentNameBinding;

import org.jetbrains.annotations.NotNull;

public class NameFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FragmentNameBinding bind;


    public NameFragment() {
        // Required empty public constructor
    }

    public static NameFragment newInstance(String param1, String param2) {
        NameFragment fragment = new NameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_name, container, false);

        bind.getRoot().findViewById(R.id.Btn_Sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_from_email_to_sign_pw);
            }
        });
        return bind.getRoot();
    }
}