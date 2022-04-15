package com.example.crustlabapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.crustlabapp.DBHandler;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.ui.HistoryActivity;
import com.example.crustlabapp.ui.MainActivity;
import com.example.crustlabapp.ui.payments.TransferMoneyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class HomeFragment extends Fragment {

    private TextView balanceTextView, currencyTextView;
    private FloatingActionButton floatingActionButton;
    private Context context;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(context);
        String balance = sharedPreferences.getString("actualBalance", "20000");
        String currency = sharedPreferences.getString("actualCurrency", "PLN");
        String userID = sharedPreferences.getString("userID", "12345");
        DBHandler dbHandler = new DBHandler(getContext(), userID);
        balanceTextView.setText(balance);
        currencyTextView.setText(currency);
        floatingActionButton.setOnClickListener(v -> startActivity(new Intent(context, HistoryActivity.class)));
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    private void initialize(View view) {
        floatingActionButton = view.findViewById(R.id.FloatingBtnHomeFragment);
        balanceTextView = view.findViewById(R.id.BalanceTextHomeFragment);
        currencyTextView = view.findViewById(R.id.CurrencyTextViewHomeFragment);
    }

}