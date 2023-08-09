package com.example.project73.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.project73.R;
import com.example.project73.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccessDeniedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccessDeniedFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1, mParam2;
    private Button mLoginButton;

    public AccessDeniedFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static AccessDeniedFragment newInstance() {
        return new AccessDeniedFragment();
    }

    public static AccessDeniedFragment newInstance(String param1, String param2) {
        AccessDeniedFragment fragment = new AccessDeniedFragment();
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
    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_access_denied, container, false);

        mLoginButton = view.findViewById(R.id.access_denied_login_button);
        mLoginButton.setOnClickListener(v -> startLogIn());

        return view;
    }

    private void startLogIn() {
        Intent i = new Intent(getActivity(), LoginActivity.class);
        startActivity(i);
    }
}