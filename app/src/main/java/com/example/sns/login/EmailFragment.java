package com.example.sns.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.sns.LoginActivity;
import com.example.sns.R;

public class EmailFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LoginViewModel viewModel;

    private EditText emailText;

    public static EmailFragment newInstance(String param1, String param2) {
        EmailFragment fragment = new EmailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EmailFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_email, container, false);
        emailText = root.findViewById(R.id.editText_email);
        viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);
        root.findViewById(R.id.Btn_Next).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.eMail.setValue(emailText.getText().toString());
                Navigation.findNavController(getView()).navigate(R.id.action_from_email_to_sign_pw);
            }
        });
        return root;
    }
}