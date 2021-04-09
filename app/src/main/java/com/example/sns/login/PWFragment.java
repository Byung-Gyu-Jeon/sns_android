package com.example.sns.login;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.example.sns.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PWFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class PWFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public static PWFragment newInstance(String param1, String param2) {
        PWFragment fragment = new PWFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PWFragment() {
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
        View root = inflater.inflate(R.layout.fragment_p_w, container, false);

        CheckBox checkBox = root.findViewById(R.id.checkBox);
        EditText editTextTextPersonName4 = root.findViewById(R.id.editTextTextPersonName4);

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    editTextTextPersonName4.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    editTextTextPersonName4.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }

            }
        });


        root.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(getView()).navigate(R.id.action_from_pw_to_sign_name);
            }
        });
        return root;
    }
}