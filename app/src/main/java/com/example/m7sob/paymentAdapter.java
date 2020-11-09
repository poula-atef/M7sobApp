package com.example.m7sob;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class paymentAdapter extends RecyclerView.Adapter<paymentAdapter.paymentViewHolder> {

    private List<payment>payments;

    @NonNull
    @Override
    public paymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new paymentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull paymentViewHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        holder.date.setText(payments.get(position).getDate());
        holder.money.setText(payments.get(position).getMoney() + " EGP");

    }

    @Override
    public int getItemCount() {
        if(payments == null)
            return 0;
        return payments.size();
    }

    public List<payment> getPayments() {
        return payments;
    }

    public void setPayments(List<payment> payments) {
        this.payments = payments;
    }

    public class paymentViewHolder extends RecyclerView.ViewHolder {
        TextView date,money;
        public paymentViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.date);
            money = itemView.findViewById(R.id.prof_day);
        }
    }
}
