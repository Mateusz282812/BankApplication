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

public class TransferMoneyFragment extends Fragment  implements IOnBackPressed {

    private EditText recipientNameEditText, recipientIDEditText, amountEditText, descriptionEditText, currencyEditText;
    private Button sendButton;
    private Context context;
    private String actualCurrency;
    private Double actualBalance;
    private DBHandler dbHandler;

    public static TransferMoneyFragment newInstance() {
        return new TransferMoneyFragment();
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
        String userID = sharedPreferences.getString("userID", "123456");
        dbHandler = new DBHandler(context, userID);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        currencyEditText.setText(actualCurrency);
        sendButton.setOnClickListener(v -> sendMoney());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transfer, container, false);
    }

    private void sendMoney(){
        if (!CheckElementHelper.checkIsEmpty(recipientNameEditText) && !CheckElementHelper.checkIsEmpty(recipientIDEditText) && !CheckElementHelper.checkIsEmpty(amountEditText) &&
        !CheckElementHelper.checkIsEmpty(descriptionEditText)){
            double amount = Double.parseDouble(amountEditText.getText().toString());
            double provision = amount * MainActivity.Provision;
            if (amount > 0 && amount < (actualBalance - provision)){
                String recipientID = recipientIDEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                actualBalance = actualBalance - amount - provision;
                dbHandler.addNewTransaction(DateFormatHelper.getActualDateAndTime(), amount, recipientID, description, actualBalance, UploadTransaction.TRANSACTION_TRANSFER, actualCurrency);
                SharedPreferences.Editor editor = SharedPreferencesHelper.getSharedPreferencesEditor(context);
                editor.putString("actualBalance", String.valueOf(actualBalance)).apply();
                OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
            } else {
                Toast.makeText(context, "Invalid amount", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initialize(View view) {
        recipientNameEditText = view.findViewById(R.id.RecipientNameEditTextTransferFragment);
        recipientIDEditText = view.findViewById(R.id.RecipientIDEditTextTransferFragment);
        amountEditText = view.findViewById(R.id.AmountEditTextTransferFragment);
        descriptionEditText = view.findViewById(R.id.DescriptionEditTextTransferFragment);
        currencyEditText = view.findViewById(R.id.CurrencyEditTextTransferFragment);
        sendButton = view.findViewById(R.id.SendBtnTransferFragment);
    }

    @Override
    public boolean onBackPressed() {
        OpenFragment.openFragment(getActivity(), PaymentsFragment.newInstance(), MainActivity.ID_CONTAINER);
        return true;
    }
}