package com.example.crustlabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.example.crustlabapp.DataModel.TransactionData;

public class UploadTransaction {

    public static final Integer TRANSACTION_INCOME = 1;
    public static final Integer TRANSACTION_OUTCOME = 2;
    public static final Integer TRANSACTION_TRANSFER = 3;
    public static final Integer TRANSACTION_EXCHANGE = 4;

    private final Context context;
    private TransactionData transactionData;
    private final DBHandler db;
    private final RecyclerAdapter recyclerAdapter;

    public UploadTransaction(Context context, TransactionData transactionData, DBHandler db, RecyclerAdapter recyclerAdapter) {
        this.context = context;
        this.transactionData = transactionData;
        this.db = db;
        this.recyclerAdapter = recyclerAdapter;
    }

    public void createItemStatistic(RecyclerAdapter.Holder holder, Integer position) {
        holder.dateItemTextView.setText(transactionData.date.get(position));
        String amountWithCurrency = transactionData.amount.get(position)+ "\n" + transactionData.currency.get(position);
        if (!transactionData.type.get(position).equals(TRANSACTION_INCOME) && !transactionData.type.get(position).equals(TRANSACTION_EXCHANGE)) {
            holder.amountItemTextView.setTextColor(ContextCompat.getColor(context, R.color.red));
            amountWithCurrency = "- " + amountWithCurrency;
        }else {
            holder.amountItemTextView.setTextColor(ContextCompat.getColor(context, R.color.black));
        }
        holder.amountItemTextView.setText(amountWithCurrency);
        holder.descriptionItemTextView.setText(transactionData.description.get(position));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.layout_transaction_information, null);
        ImageButton deleteTransactionImgButton = view.findViewById(R.id.deleteTransactionBtnInformationLayout);
        TextView dateTransactionTextView = view.findViewById(R.id.DateTransactionTextInformationLayout);
        TextView recipientTransactionTextView = view.findViewById(R.id.RecipientTransactionTextInformationLayout);
        TextView amountTransactionTextView = view.findViewById(R.id.AmountTransactionTextInformationLayout);
        TextView descriptionTransactionTextView = view.findViewById(R.id.DescriptionTransactionTextInformationLayout);
        TextView balanceTransactionTextView = view.findViewById(R.id.BalanceTransactionTextInformationLayout);
        TextView typeTransactionTextView = view.findViewById(R.id.TypeTransactionTextInformationLayout);
        TextView currencyTransactionTextView = view.findViewById(R.id.CurrencyTransactionTextInformationLayout);
        dateTransactionTextView.setText(transactionData.date.get(position));
        recipientTransactionTextView.setText(transactionData.recipient.get(position));
        amountTransactionTextView.setText(String.valueOf(transactionData.amount.get(position)));
        descriptionTransactionTextView.setText(transactionData.description.get(position));
        balanceTransactionTextView.setText(String.valueOf(transactionData.balance.get(position)));
        String transactionType = "MONEY TRANSFER";
        if(transactionData.type.get(position).equals(TRANSACTION_INCOME)){
            transactionType = "CASH DEPOSIT";
        }else if (transactionData.type.get(position).equals(TRANSACTION_OUTCOME)){
            transactionType = " CASH WITHDRAWAL";
        }else if (transactionData.type.get(position).equals(TRANSACTION_TRANSFER)){
            transactionType = "MONEY TRANSFER";
        } else if (transactionData.type.get(position).equals(TRANSACTION_EXCHANGE)){
            transactionType = "MONEY EXCHANGE";
        }
        typeTransactionTextView.setText(transactionType);
        currencyTransactionTextView.setText(transactionData.currency.get(position));

        builder.setView(view)
                .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        holder.relativeLayout.setOnClickListener(v -> alertDialog.show());
        deleteTransactionImgButton.setOnClickListener(v -> {
            alertDialog.dismiss();
            db.deleteGivenTransaction(transactionData.id.get(position));
            recyclerAdapter.notifyItemRemoved(position);
            recyclerAdapter.notifyItemRangeChanged(position, transactionData.id.size());
            transactionData = db.getAllStatistics();
            recyclerAdapter.setTransactionData(transactionData);
        });
    }
    public void updateTransactionData(TransactionData transactionData){
        this.transactionData = transactionData;
    }
}
