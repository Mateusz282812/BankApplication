package com.example.crustlabapp.DataModel;

import java.util.ArrayList;
import java.util.List;

public class TransactionData {

    public static final String TRANSACTION_ID = "Id";
    public static final String TRANSACTION_DATE = "Date";
    public static final String TRANSACTION_AMOUNT = "Amount";
    public static final String TRANSACTION_RECIPIENT_ID = "Recipient";
    public static final String TRANSACTION_DESCRIPTION = "Description";
    public static final String TRANSACTION_BALANCE = "Balance";
    public static final String TRANSACTION_TYPE = "Type";
    public static final String TRANSACTION_CURRENCY = "Currency";

    public List<Integer> id = new ArrayList<>();
    public List<String> date = new ArrayList<>();
    public List<String> currency = new ArrayList<>();
    public List<String> description = new ArrayList<>();
    public List<Double> amount = new ArrayList<>();
    public List<String> recipient = new ArrayList<>();
    public List<Double> balance = new ArrayList<>();
    public List<Integer> type = new ArrayList<>();

}
