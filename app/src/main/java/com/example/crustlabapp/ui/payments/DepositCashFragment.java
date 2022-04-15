package com.example.crustlabapp.ui.payments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.crustlabapp.CheckElementHelper;
import com.example.crustlabapp.DBHandler;
import com.example.crustlabapp.DateFormatHelper;
import com.example.crustlabapp.Interfaces.IOnBackPressed;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.UploadTransaction;
import com.example.crustlabapp.ui.MainActivity;

public class DepositCashFragment extends Fragment implements IOnBackPressed {

    private EditText amountEditText, currencyEditText;
    private Button depositButton;
    private Context context;
    private String actualCurrency;
    private String userID;
    private Double actualBalance;
    private DBHandler dbHandler;

    public static DepositCashFragment newInstance() {
        return new DepositCashFragment();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(context);
        actualCurrency = sharedPreferences.getString("actualCurrency", "PLN");
        actualBalance = Double.parseDouble(sharedPreferences.getString("actualBalance", "20000"));
        userID = sharedPreferences.getString("userID", "123456");
        dbHandler = new DBHandler(context, userID);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        currencyEditText.setText(actualCurrency);
        depositButton.setOnClickListener(v -> cashDeposit());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_deposit, container, false);
    }

    private void cashDeposit() {
        if (!CheckElementHelper.checkIsEmpty(amountEditText)) {
            double amount = Double.parseDouble(amountEditText.getText().toString());
            double provision = amount * MainActivity.Provision;
            if (amount > 0) {
                actualBalance = actualBalance + amount - provision;
                dbHandler.addNewTransaction(DateFormatHelper.getActualDateAndTime(), amount, userID, "CASH DEPOSIT", actualBalance, UploadTransaction.TRANSACTION_INCOME, actualCurrency);
                SharedPreferences.Editor editor = SharedPreferencesHelper.getSharedPreferencesEditor(context);
                editor.putString("actualBalance", String.valueOf(actualBalance)).apply();
                OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
            } else {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialize(View view) {
        amountEditText = view.findViewById(R.id.AmountEditTextDepositFragment);
        currencyEditText = view.findViewById(R.id.CurrencyEditTextDepositFragment);
        depositButton = view.findViewById(R.id.DepositBtnDepositFragment);
    }

    @Override
    public boolean onBackPressed() {
        OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
        return true;
    }
}