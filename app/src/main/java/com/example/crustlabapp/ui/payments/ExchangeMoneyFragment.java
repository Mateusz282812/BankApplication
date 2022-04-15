package com.example.crustlabapp.ui.payments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.crustlabapp.DBHandler;
import com.example.crustlabapp.DateFormatHelper;
import com.example.crustlabapp.ExchangeHelper;
import com.example.crustlabapp.Interfaces.IOnBackPressed;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.UploadTransaction;
import com.example.crustlabapp.ui.MainActivity;


public class ExchangeMoneyFragment extends Fragment implements AdapterView.OnItemSelectedListener, IOnBackPressed {

    private TextView setBalanceTextView, actualBalanceTextView;
    private Spinner actualCurrencySpinner, setCurrencySpinner;
    private Button exchangeButton;
    private Context context;
    private String actualCurrency;
    private String userID;
    private Double actualBalance;
    private DBHandler dbHandler;

    public static ExchangeMoneyFragment newInstance() {
        return new ExchangeMoneyFragment();
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
        String[] currencyItems = new String[]{"PLN", "USD", "EUR"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_spinner_item, currencyItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setCurrencySpinner.setAdapter(adapter);
        actualCurrencySpinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition(actualCurrency);
        actualCurrencySpinner.setSelection(spinnerPosition);
        setCurrencySpinner.setSelection(spinnerPosition);
        setCurrencySpinner.setOnItemSelectedListener(this);
        actualCurrencySpinner.setEnabled(false);
        actualCurrencySpinner.setClickable(false);
        actualBalanceTextView.setText(String.valueOf(actualBalance));
        exchangeButton.setOnClickListener(v -> exchangeMoney());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_exchange, container, false);
    }

    private void initialize(View view) {
        setBalanceTextView = view.findViewById(R.id.SetBalanceTextViewExchangeFragment);
        actualBalanceTextView = view.findViewById(R.id.ActualBalanceTextViewExchangeFragment);
        actualCurrencySpinner = view.findViewById(R.id.SpinnerActualCurrencyExchangeFragment);
        setCurrencySpinner = view.findViewById(R.id.SpinnerSetCurrencyExchangeFragment);
        exchangeButton = view.findViewById(R.id.ExchangeBtnExchangeFragment);
    }

    private void exchangeMoney() {
        actualBalance = Double.parseDouble( setBalanceTextView.getText().toString().replace(',','.'));
        actualBalance = actualBalance - (actualBalance * MainActivity.Provision);
        String actualCurrency = setCurrencySpinner.getItemAtPosition(setCurrencySpinner.getSelectedItemPosition()).toString();
        SharedPreferences.Editor editor = SharedPreferencesHelper.getSharedPreferencesEditor(context);
        editor.putString("actualBalance", String.valueOf(actualBalance))
                .putString("actualCurrency", actualCurrency)
                .apply();
        String actualData = DateFormatHelper.getActualDateAndTime();
        dbHandler.addNewTransaction(actualData, actualBalance, userID, " MONEY EXCHANGE", actualBalance, UploadTransaction.TRANSACTION_EXCHANGE, actualCurrency);
        OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                switch (actualCurrency) {
                    case "PLN":
                        setBalanceTextView.setText(formatDouble(actualBalance));
                        break;
                    case "USD":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangeUSDToPLN(actualBalance)));
                        break;
                    case "EUR":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangeEURToPLN(actualBalance)));
                        break;
                }
                break;
            case 1:
                switch (actualCurrency) {
                    case "USD":
                        setBalanceTextView.setText(formatDouble(actualBalance));
                        break;
                    case "PLN":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangePLNToUSD(actualBalance)));
                        break;
                    case "EUR":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangeEURToUSD(actualBalance)));
                        break;
                }
                break;
            case 2:
                switch (actualCurrency) {
                    case "EUR":
                        setBalanceTextView.setText(formatDouble(actualBalance));
                        break;
                    case "USD":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangeUSDToEUR(actualBalance)));
                        break;
                    case "PLN":
                        setBalanceTextView.setText(formatDouble(ExchangeHelper.exchangePLNToEUR(actualBalance)));
                        break;
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    @Override
    public boolean onBackPressed() {
        OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
        return true;
    }

    private String formatDouble(double number){
        return String.format("%.2f", number);
    }
}