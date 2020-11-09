package com.example.m7sob;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class daysAdapter extends RecyclerView.Adapter<daysAdapter.dayViewHolder> {

    private List<Double>all;
    private List<Double> shop;
    private List<Integer> numbers;
    private List<String> days;
    private onDayClickListener listener;
    private String type;
    private List<order>myOrders;
    private Context context;

    public daysAdapter() {
    }

    public daysAdapter(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public dayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new dayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.day_element,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull dayViewHolder holder, int position) {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df = (DecimalFormat) nf;
        df.applyPattern("0.00");
        if(type == null) {
            holder.day.setText(days.get(position));
            holder.total.setText(df.format(all.get(position)) + " EGP");
            holder.shop.setText(df.format(shop.get(position)) + " EGP");
            double pd = all.get(position) - shop.get(position);
            holder.paid.setText(df.format(pd) + " EGP");
            holder.totalOrders.setText(numbers.get(position) + " orders");
        }
        else{
            holder.day.setText(myOrders.get(position).getCode()+"");
            holder.total.setText(df.format(myOrders.get(position).getClient()) + " EGP");
            holder.shop.setText(df.format(myOrders.get(position).getShop()) + " EGP");
            double pd = myOrders.get(position).getClient() - myOrders.get(position).getShop();
            holder.paid.setText(df.format(pd) + " EGP");
            holder.remove.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(type == null){
            if(all == null)
                return 0;
            return days.size();
        }
        else{
            if(myOrders == null)
                return 0;
            return myOrders.size();
        }
    }

    public List<Double> getAll() {
        return all;
    }

    public void setAll(List<Double> all) {
        this.all = all;
    }

    public List<Double> getShop() {
        return shop;
    }

    public void setShop(List<Double> shop) {
        this.shop = shop;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public List<order> getMyOrders() {
        return myOrders;
    }

    public void setMyOrders(List<order> myOrders) {
        this.myOrders = myOrders;
    }

    public onDayClickListener getListener() {
        return listener;
    }

    public void setListener(onDayClickListener listener) {
        this.listener = listener;
    }

    public List<Integer> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<Integer> numbers) {
        this.numbers = numbers;
    }

    interface onDayClickListener{
        void onDayClick(String date);
        void removeOrder(order ord);
    }

    public class dayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView total,shop,paid,day,totalOrders;
        Button remove;
        public dayViewHolder(@NonNull View itemView) {
            super(itemView);
            total = itemView.findViewById(R.id.total_day);
            shop = itemView.findViewById(R.id.shop_day);
            paid = itemView.findViewById(R.id.prof_day);
            day = itemView.findViewById(R.id.date);
            remove = itemView.findViewById(R.id.remove);
            totalOrders = itemView.findViewById(R.id.total_order);
            if(type != null){
                totalOrders.setVisibility(View.GONE);
            }
            itemView.setOnClickListener(this);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    order ord = myOrders.get(pos);
                    myOrders.remove(getLayoutPosition());
                    notifyItemRemoved(getLayoutPosition());
                    new AlertDialog.Builder(context)
                            .setTitle(itemView.getResources().getString(R.string.dialog_title))
                            .setMessage(itemView.getResources().getString(R.string.dialog_body))
                            .setIcon(android.R.drawable.ic_input_delete)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    listener.removeOrder(ord);
                                }})
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
                                    myOrders.add(pos,ord);
                                    notifyItemInserted(pos);
                                }}).show();
                }
            });
        }

        @Override
        public void onClick(View v) {
            if(type == null)
                listener.onDayClick(days.get(getLayoutPosition()));
        }
    }
}
