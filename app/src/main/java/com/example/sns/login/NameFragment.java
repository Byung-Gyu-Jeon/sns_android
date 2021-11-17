package com.example.sns.login;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.sns.R;
import com.example.sns.databinding.FragmentNameBinding;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class NameFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    FragmentNameBinding bind;

    private LoginViewModel viewModel;

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
//        bind = DataBindingUtil.inflate(inflater,R.layout.fragment_name, container, false);
        View root = inflater.inflate(R.layout.fragment_name, container, false);
        viewModel = new ViewModelProvider(getActivity()).get(LoginViewModel.class);

        EditText editTextName = root.findViewById(R.id.editText_name);
        ProgressBar progressBar = root.findViewById(R.id.Sign_progressBar);

//        viewModel.enableButton(true, LoginViewModel.NAME);

//        bind.getRoot().findViewById(R.id.Btn_Sign).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                viewModel.name.setValue(editTextName.getText().toString());
//                Log.d(TAG, "onClick: " + viewModel.eMail.getValue());
//                Log.d(TAG, "onClick: " + viewModel.passWord.getValue());
//                Log.d(TAG, "onClick: " + viewModel.name.getValue());
//                Navigation.findNavController(getView()).navigate(R.id.action_from_email_to_sign_pw);
//            }
//        });
//        return bind.getRoot();
        root.findViewById(R.id.Btn_Sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.name.setValue(editTextName.getText().toString());
                progressBar.setVisibility(View.VISIBLE);

                viewModel.SignUP(getView());

                Log.d(TAG, "onClick: " + viewModel.eMail.getValue());
                Log.d(TAG, "onClick: " + viewModel.passWord.getValue());
                Log.d(TAG, "onClick: " + viewModel.name.getValue());
//                Navigation.findNavController(getView()).navigate(R.id.action_from_name_to_login);
            }
        });
        return root;
    }
}