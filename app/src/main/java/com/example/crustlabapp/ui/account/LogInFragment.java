package com.example.crustlabapp.ui.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.crustlabapp.CheckElementHelper;
import com.example.crustlabapp.ui.MainActivity;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.ui.home.HomeFragment;

public class LogInFragment extends Fragment {

    private Button loginButton;
    private EditText userIDEditText, passwordEditText;
    private Context context;


    public static LogInFragment newInstance() {
        return new LogInFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initialize(view);
        loginButton.setOnClickListener(v -> loginUser());
        super.onViewCreated(view, savedInstanceState);
    }

    private void initialize(View view) {
        loginButton = view.findViewById(R.id.LoginButtonLogInFragment);
        userIDEditText = view.findViewById(R.id.UserIDEditTextLogInFragment);
        passwordEditText = view.findViewById(R.id.PasswordEditTextLogInFragment);
    }

    private void loginUser() {
        if (!CheckElementHelper.checkIsEmpty(userIDEditText) && !CheckElementHelper.checkIsEmpty(passwordEditText)) {
            SharedPreferences.Editor editor = SharedPreferencesHelper.getSharedPreferencesEditor(context);
            editor.putBoolean("isLogged", true);
            editor.putString("userID", userIDEditText.getText().toString());
            editor.apply();
            OpenFragment.openFragment(getActivity(), HomeFragment.newInstance(), MainActivity.ID_CONTAINER);
            //TODO :
            // Sprawdzić czy użytkownik istnieje oraz sprawdzić poprawność danych.

        }
    }
}