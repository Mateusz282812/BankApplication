package com.example.crustlabapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.crustlabapp.DataModel.TransactionData;
import com.example.crustlabapp.Interfaces.IApplicationTransaction;

import java.util.List;

public class DBHandler extends SQLiteOpenHelper implements IApplicationTransaction {
    private static final String DB_NAME = "CrustLabDataBases";

    private final DBTransaction dbTransaction;

    public DBHandler(Context context, String userID) {
        super(context, DB_NAME, null, 1);
        dbTransaction = new DBTransaction(this, userID);
    }

    public void addDataToDataBases(ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(tableName, null, values);
        db.close();
    }

    public void deleteTableOfDataBases(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db);
        db.close();
    }

    public void deleteRecordOfTable(String tableName, String columnName, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, columnName + "=?", new String[]{id});
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        dbTransaction.createTransactionTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public TransactionData getAllStatistics() {
        return dbTransaction.getAllStatistics();
    }

    @Override
    public void addNewTransaction(String date, Double amount, String recipientID, String description, Double balance, Integer type, String currency) {
        dbTransaction.addNewTransaction(date, amount, recipientID, description, balance, type, currency);
    }

    @Override
    public List<?> getTransactionProperties(String transactionProperties) {
        return null;
    }

    @Override
    public Integer getLastIdOfTransactionTable() {
        return dbTransaction.getLastIdOfTransactionTable();
    }

    @Override
    public void deleteTransactionTable() {
        dbTransaction.deleteTransactionTable();
    }

    @Override
    public void deleteGivenTransaction(Integer id) {
        dbTransaction.deleteGivenTransaction(id);
    }
}
