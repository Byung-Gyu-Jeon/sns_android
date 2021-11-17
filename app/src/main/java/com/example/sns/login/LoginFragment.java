package com.example.sns.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.sns.MainLoginActivity;
import com.example.sns.R;

public class LoginFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private LoginViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_login, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        EditText editTextName = root.findViewById(R.id.editText_id);
        EditText editTextPassword = root.findViewById(R.id.editText_password);

        root.findViewById(R.id.Btn_Join).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_from_login_to_sign_email);
            }
        });
        root.findViewById(R.id.Btn_Login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = editTextName.getText().toString();
                String password = editTextPassword.getText().toString();

                if (id.length() != 0 && password.length() != 0) {  //아이디와 비밀번호의 길이가 0이 아니면
                    viewModel.eMail.setValue(id);
                    viewModel.passWord.setValue(password);
                    viewModel.Login(getView());
                }else {
                    Toast.makeText(getContext(), "아이디 또는 비밀번호를 입력 해 주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return root;
    }
}