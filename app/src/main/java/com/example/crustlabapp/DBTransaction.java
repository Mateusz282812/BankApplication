package com.example.crustlabapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.crustlabapp.DataModel.TransactionData;
import com.example.crustlabapp.Interfaces.IApplicationTransaction;

import java.util.List;

public class DBTransaction implements IApplicationTransaction {

    private static final String DB_TRANSACTION_TABLE = "CrustLab_Transaction_";

    private static final String SQL_TRANSACTION_TABLE = " (" +
            TransactionData.TRANSACTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            TransactionData.TRANSACTION_DATE + " TEXT," +
            TransactionData.TRANSACTION_AMOUNT + " TEXT," +
            TransactionData.TRANSACTION_RECIPIENT_ID + " TEXT," +
            TransactionData.TRANSACTION_DESCRIPTION + " TEXT," +
            TransactionData.TRANSACTION_BALANCE + " TEXT," +
            TransactionData.TRANSACTION_TYPE + " INTEGER," +
            TransactionData.TRANSACTION_CURRENCY + " TEXT)";

    private final SQLiteOpenHelper sqLiteOpenHelper;
    private final DBHandler dbHandler;
    private final String transactionTableName;

    public DBTransaction(DBHandler dbHandler, String userID) {
        this.dbHandler = dbHandler;
        this.sqLiteOpenHelper = (SQLiteOpenHelper) dbHandler;
        transactionTableName = DB_TRANSACTION_TABLE + userID;
    }

    public void createTransactionTable(SQLiteDatabase db) {
        if (db != null) {
            db.execSQL("CREATE TABLE IF NOT EXISTS " + transactionTableName + SQL_TRANSACTION_TABLE);
        }
    }

    @Override
    public TransactionData getAllStatistics() {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + transactionTableName, null);
        TransactionData transactionData = new TransactionData();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                transactionData.id.add(cursor.getInt(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_ID)));
                transactionData.date.add(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_DATE)));
                transactionData.amount.add(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_AMOUNT)).replace(',','.')));
                transactionData.recipient.add(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_RECIPIENT_ID)));
                transactionData.description.add(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_DESCRIPTION)));
                transactionData.balance.add(Double.parseDouble(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_BALANCE)).replace(',','.')));
                transactionData.type.add(cursor.getInt(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_TYPE)));
                transactionData.currency.add(cursor.getString(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_CURRENCY)));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return transactionData;
    }

    @Override
    public void addNewTransaction(String date, Double amount, String recipientID, String description, Double balance, Integer type, String currency) {
        ContentValues values = new ContentValues();
        values.put(TransactionData.TRANSACTION_DATE, date);
        String am = String.format("%.2f", amount);
        values.put(TransactionData.TRANSACTION_AMOUNT, am);
        values.put(TransactionData.TRANSACTION_RECIPIENT_ID, recipientID);
        values.put(TransactionData.TRANSACTION_DESCRIPTION, description);
        String bal = String.format("%.2f", balance);
        values.put(TransactionData.TRANSACTION_BALANCE, bal);
        values.put(TransactionData.TRANSACTION_TYPE, type);
        values.put(TransactionData.TRANSACTION_CURRENCY, currency);

        dbHandler.addDataToDataBases(values, transactionTableName);
    }

    @Override
    public List<?> getTransactionProperties(String transactionProperties) {
        return null;
    }

    @Override
    public Integer getLastIdOfTransactionTable() {
        SQLiteDatabase db = sqLiteOpenHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + TransactionData.TRANSACTION_ID + " FROM " + transactionTableName + " order by " + TransactionData.TRANSACTION_ID + " DESC limit 1", null);
        int lastId = -1;
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                lastId = cursor.getInt(cursor.getColumnIndexOrThrow(TransactionData.TRANSACTION_ID));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return lastId;
    }

    @Override
    public void deleteTransactionTable() {
        dbHandler.deleteTableOfDataBases(transactionTableName);
    }

    @Override
    public void deleteGivenTransaction(Integer id) {
        dbHandler.deleteRecordOfTable(transactionTableName, TransactionData.TRANSACTION_ID, Integer.toString(id));
    }
}
