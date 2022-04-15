package com.example.crustlabapp.ui.payments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.crustlabapp.Interfaces.IOnBackPressed;
import com.example.crustlabapp.OpenFragment;
import com.example.crustlabapp.R;
import com.example.crustlabapp.SharedPreferencesHelper;
import com.example.crustlabapp.ui.MainActivity;
import com.example.crustlabapp.ui.home.HomeFragment;

public class PaymentsFragment extends Fragment implements IOnBackPressed {

    private Button withdrawalButton, depositButton, transferBtn, exchangeBtn;
    private TextView informationTextView;
    private Context context;

    public static PaymentsFragment newInstance() {
        return new PaymentsFragment();
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
        return inflater.inflate(R.layout.fragment_payments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize(view);
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(context);
        String amount = sharedPreferences.getString("actualBalance", "20000");
        String currency = sharedPreferences.getString("actualCurrency", "PLN");
        String label = amount + "\n" + currency;
        informationTextView.setText(label);
        withdrawalButton.setOnClickListener(v -> OpenFragment.openFragment(getActivity(), WithdrawalCashFragment.newInstance(), MainActivity.ID_CONTAINER));
        depositButton.setOnClickListener(v -> OpenFragment.openFragment(getActivity(), DepositCashFragment.newInstance(), MainActivity.ID_CONTAINER));
        transferBtn.setOnClickListener(v -> OpenFragment.openFragment(getActivity(), TransferMoneyFragment.newInstance(), MainActivity.ID_CONTAINER));
        exchangeBtn.setOnClickListener(v -> OpenFragment.openFragment(getActivity(), ExchangeMoneyFragment.newInstance(), MainActivity.ID_CONTAINER));
    }

    private void initialize(View view) {
        withdrawalButton = view.findViewById(R.id.withdrawBtnPaymentsFragment);
        depositButton = view.findViewById(R.id.depositBtnPaymentsFragment);
        transferBtn = view.findViewById(R.id.transferBtnPaymentsFragment);
        exchangeBtn = view.findViewById(R.id.exchangeBtnPaymentsFragment);
        informationTextView = view.findViewById(R.id.informationTextViewPaymentsFragment);
    }

    @Override
    public boolean onBackPressed() {
        OpenFragment.openFragment(getActivity(), HomeFragment.newInstance(), MainActivity.ID_CONTAINER);
        return true;
    }
}