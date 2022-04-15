package com.example.crustlabapp.Interfaces;

import com.example.crustlabapp.DataModel.TransactionData;

import java.util.List;

public interface IApplicationTransaction {

    TransactionData getAllStatistics();

    void addNewTransaction(String date, Double amount, String recipientID, String description, Double balance, Integer type, String currency);

    List<?> getTransactionProperties(String transactionProperties);

    Integer getLastIdOfTransactionTable();

    void deleteTransactionTable();

    void deleteGivenTransaction(Integer id);
}
