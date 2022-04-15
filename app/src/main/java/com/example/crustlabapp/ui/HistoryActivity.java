package com.example.crustlabapp.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crustlabapp.DBHandler;
import com.example.crustlabapp.DataModel.TransactionData;
import com.example.crustlabapp.DateFormatHelper;
import com.example.crustlabapp.R;
import com.example.crustlabapp.RecyclerAdapter;
import com.example.crustlabapp.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private ImageButton filterImgButton;
    private TextView labelTextView;
    private TransactionData transactionData;
    private DBHandler dbHandler;
    private RecyclerAdapter recyclerAdapter;
    private List<Integer> positionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initialize();
        SharedPreferences sharedPreferences = SharedPreferencesHelper.getSharedPreferences(this);
        String userID = sharedPreferences.getString("userID", "123456");
        dbHandler = new DBHandler(this, userID);
        transactionData = new TransactionData();
        transactionData = dbHandler.getAllStatistics();
        String label = "User " + userID;
        labelTextView.setText(label);
        filterImgButton.setOnClickListener(v -> filterHistory());
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.RecyclerViewHistoryActivity);
        recyclerAdapter = new RecyclerAdapter(this, transactionData, dbHandler);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initialize() {
        filterImgButton = findViewById(R.id.FilterImgBtnHistoryActivity);
        labelTextView = findViewById(R.id.UserTextViewHistoryActivity);
    }

    private void filterHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alerdialog_filter_history, null);
        Button filterButton = view.findViewById(R.id.FilterBtnHistoryAlertDialog);
        RadioButton typeRadioButton = view.findViewById(R.id.TypeOperationRadioBtn);
        RadioButton dateRadioButton = view.findViewById(R.id.DateOperationRadioBtn);
        RadioButton currencyRadioButton = view.findViewById(R.id.CurrencyRadioBtn);
        LinearLayout dateLinearLayout = view.findViewById(R.id.DateOperationLayoutHistoryAlertDialog);
        Button startDateButton = view.findViewById(R.id.StartDateHistoryAlertDialog);
        Button endDateButton = view.findViewById(R.id.EndDateHistoryAlertDialog);
        Spinner typeOperationSpinner = view.findViewById(R.id.TypeOperationSpinnerHistoryAlertDialog);
        Spinner currencyOperationSpinner = view.findViewById(R.id.CurrencySpinnerHistoryAlertDialog);
        typeRadioButton.setChecked(true);
        typeRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> setVisibility(isChecked, typeOperationSpinner));
        dateRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> setVisibility(isChecked, dateLinearLayout));
        currencyRadioButton.setOnCheckedChangeListener((buttonView, isChecked) -> setVisibility(isChecked, currencyOperationSpinner));
        setSpinnerAdapter(currencyOperationSpinner, new String[]{"ALL", "PLN", "USD", "EUR"});
        setSpinnerAdapter(typeOperationSpinner, new String[]{"ALL", "CASH DEPOSIT", "CASH WITHDRAWAL", "MONEY TRANSFER", "MONEY EXCHANGE"});
        String actualDate = DateFormatHelper.getActualDate();
        startDateButton.setText(actualDate);
        endDateButton.setText(actualDate);
        startDateButton.setOnClickListener(v -> getCalendarAlertDialog(startDateButton));
        endDateButton.setOnClickListener(v -> getCalendarAlertDialog(endDateButton));

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        filterButton.setOnClickListener(v -> {
            if (transactionData.id.size() > 0) {
                positionList = new ArrayList<>();
                positionList.addAll(transactionData.id);
                if (typeRadioButton.isChecked() && typeOperationSpinner.getSelectedItemPosition() != 0) {
                    Integer typeOperation = typeOperationSpinner.getSelectedItemPosition();
                    positionList = new ArrayList<>();
                    for (int i = 0; i < transactionData.id.size(); i++) {
                        if (transactionData.type.get(i).equals(typeOperation)) {
                            positionList.add(transactionData.id.get(i));
                        }
                    }
                }
                if (dateRadioButton.isChecked()) {
                    Date startDate = DateFormatHelper.getDateAndTimeOfStringDate(startDateButton.getText().toString() + " 00:00:00");
                    Date endDate = DateFormatHelper.getDateAndTimeOfStringDate(endDateButton.getText().toString() + " 23:59:59");
                    positionList = new ArrayList<>();
                    for (int i = 0; i < transactionData.id.size(); i++) {
                        Date date = DateFormatHelper.getDateAndTimeOfStringDate(transactionData.date.get(i));
                        if (date.after(startDate) && date.before(endDate)) {
                            positionList.add(transactionData.id.get(i));
                        }
                    }
                }
                if (currencyRadioButton.isChecked() && currencyOperationSpinner.getSelectedItemPosition() != 0) {
                    String actualCurrency = currencyOperationSpinner.getItemAtPosition(currencyOperationSpinner.getSelectedItemPosition()).toString();
                    positionList = new ArrayList<>();
                    for (int i = 0; i < transactionData.id.size(); i++) {
                        if (transactionData.currency.get(i).equals(actualCurrency)) {
                            positionList.add(transactionData.id.get(i));
                        }
                    }
                }
                TransactionData filterData = new TransactionData();
                for (int position = 1; position <= positionList.size(); position++) {
                    Integer pos = positionList.get(position-1) - 1;
                    filterData.id.add(position);
                    filterData.amount.add(transactionData.amount.get(pos));
                    filterData.recipient.add(transactionData.recipient.get(pos));
                    filterData.description.add(transactionData.description.get(pos));
                    filterData.balance.add(transactionData.balance.get(pos));
                    filterData.date.add(transactionData.date.get(pos));
                    filterData.currency.add(transactionData.currency.get(pos));
                    filterData.type.add(transactionData.type.get(pos));
                }
                recyclerAdapter.setTransactionData(filterData);
                recyclerAdapter.notifyDataSetChanged();
            }
            alertDialog.dismiss();
        });
    }


    private void setVisibility(boolean visibility, View view) {
        if (visibility) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    private void setSpinnerAdapter(Spinner spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        int spinnerPosition = adapter.getPosition("ALL");
        spinner.setSelection(spinnerPosition);
    }

    private void getCalendarAlertDialog(Button button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.alertdialog_calendar, null);
        DatePicker datePicker = view.findViewById(R.id.datePickerCalendarAlertDialog);
        builder.setView(view).setCancelable(false)
                .setPositiveButton("Ok", (dialogInterface, i) -> {
                    button.setText(DateFormatHelper.getDateFromDatePicker(datePicker));
                    dialogInterface.dismiss();
                })
                .create().show();
    }
}