package com.example.crustlabapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.crustlabapp.DataModel.TransactionData;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {
    private TransactionData transactionData;
    private final UploadTransaction uploadTransaction;

    public RecyclerAdapter(Context context, TransactionData transactionData, DBHandler dbHandler) {
        this.transactionData = transactionData;
        uploadTransaction = new UploadTransaction(context, transactionData, dbHandler, this);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview_item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        if (getItemCount() > 0) {
            uploadTransaction.createItemStatistic(holder, position);
        }
    }


    @Override
    public int getItemCount() {
        return transactionData.id.size();
    }

    public void setTransactionData(TransactionData transactionData){
        this.transactionData = transactionData;
        uploadTransaction.updateTransactionData(transactionData);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        public RelativeLayout relativeLayout;
        public TextView descriptionItemTextView, dateItemTextView, amountItemTextView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.itemRelativeLayout);
            dateItemTextView = itemView.findViewById(R.id.DateItemTextView);
            amountItemTextView = itemView.findViewById(R.id.AmountTextView);
            descriptionItemTextView = itemView.findViewById(R.id.DescriptionItemTextView);
        }
    }

}
