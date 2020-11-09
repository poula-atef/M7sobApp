package com.example.m7sob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity implements daysAdapter.onDayClickListener{

    RecyclerView rec;
    daysAdapter adapter;
    String user;
    UserModelView vm;
    List<order> orders;
    boolean inDays = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        rec = findViewById(R.id.rec);
        user = PreferenceManager.getDefaultSharedPreferences(this).getString("user","no user");
        orders = new ArrayList<>();
        vm = ViewModelProviders.of(this).get(UserModelView.class);
        vm.getUserOrders(user);
        vm.getOrdersLiveData().observe(this, new Observer<List<order>>() {
            @Override
            public void onChanged(List<order> ords) {
                orders.addAll(ords);
                List<Double> all = new ArrayList<>();
                List<Double> shop = new ArrayList<>();
                List<String> days = new ArrayList<>();
                List<Integer> number = new ArrayList<>();

                for(int i=0;i<ords.size();i++){
                    int dt = days.indexOf(ords.get(i).getDate());
                    if(dt >= 0){
                        all.set(dt,all.get(dt) + ords.get(i).getClient());
                        shop.set(dt,shop.get(dt) + ords.get(i).getShop());
                        number.set(dt,number.get(dt) + 1);
                    }
                    else{
                        all.add(ords.get(i).getClient());
                        shop.add(ords.get(i).getShop());
                        days.add(ords.get(i).getDate());
                        number.add(1);
                    }
                }
                adapter = new daysAdapter();
                adapter.setDays(days);
                adapter.setAll(all);
                adapter.setShop(shop);
                adapter.setNumbers(number);
                adapter.setListener(OrdersActivity.this);
                rec.setAdapter(adapter);
                rec.setHasFixedSize(true);
                rec.setLayoutManager(new LinearLayoutManager(OrdersActivity.this));
            }
        });
        
    }

    @Override
    public void onBackPressed() {
        if(inDays){
            inDays = false;
            List<Double> all = new ArrayList<>();
            List<Double> shop = new ArrayList<>();
            List<String> days = new ArrayList<>();
            List<Integer> number = new ArrayList<>();

            for(int i=0;i<orders.size();i++){
                int dt = days.indexOf(orders.get(i).getDate());
                if(dt >= 0){
                    all.set(dt,all.get(dt) + orders.get(i).getClient());
                    shop.set(dt,shop.get(dt) + orders.get(i).getShop());
                    number.set(dt,number.get(dt) + 1);
                }
                else{
                    all.add(orders.get(i).getClient());
                    shop.add(orders.get(i).getShop());
                    days.add(orders.get(i).getDate());
                    number.add(1);
                }
            }
            adapter = new daysAdapter();
            adapter.setDays(days);
            adapter.setAll(all);
            adapter.setShop(shop);
            adapter.setNumbers(number);
            adapter.setListener(OrdersActivity.this);
            rec.setAdapter(adapter);
        }
        else{
            Intent intent = new Intent(OrdersActivity.this,innerActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onDayClick(String date) {
        inDays = true;
        List<order>ords = new ArrayList<>();
        for(int i=0;i<orders.size();i++){
            if(orders.get(i).getDate().equals(date))
                ords.add(orders.get(i));
        }
        adapter = new daysAdapter("days");
        adapter.setMyOrders(ords);
        adapter.setListener(this);
        rec.setAdapter(adapter);

    }

    @Override
    public void removeOrder(order ord) {
        vm.removeOrder(ord.getCode());
        vm.getJsonObjectLiveData().observe(this, new Observer<JsonObject>() {
            @Override
            public void onChanged(JsonObject object) {
                Toast.makeText(OrdersActivity.this, getResources().getString(R.string.removed_message), Toast.LENGTH_SHORT).show();
                for(int i=0;i<orders.size();i++){
                    if(orders.get(i).getCode() == ord.getCode()){
                        orders.remove(i);
                        break;
                    }
                }
            }
        });

    }
    
}