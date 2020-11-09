package com.example.m7sob;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class paymentsActivity extends AppCompatActivity {

    String user;
    RecyclerView rec;
    paymentAdapter adapter;
    TextView TotalWanted;
    EditText et;
    double wanted = 0;
    UserModelView vm;
    List<payment>pays = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        rec = findViewById(R.id.rec);
        et = findViewById(R.id.et);
        TotalWanted = findViewById(R.id.textView);
        user = PreferenceManager.getDefaultSharedPreferences(this).getString("user","no user");
        wanted = getIntent().getDoubleExtra("allShop",0);
        vm = ViewModelProviders.of(this).get(UserModelView.class);
        vm.getUserPayments(user);
        vm.getPaymentsLiveData().observe(this, new Observer<List<payment>>() {
            @Override
            public void onChanged(List<payment> payments) {
                double allPaied = 0.0;
                for(int i=0;i<payments.size();i++){
                    allPaied += payments.get(i).getMoney();
                }
                wanted -= allPaied;
                pays = new ArrayList<>();
                pays = payments;
                if(wanted > 0){
                    TotalWanted.setText(wanted + " EGP");
                }
                else if(wanted == 0){
                    TotalWanted.setText(getResources().getString(R.string.all_paid));
                }
                else{
                    wanted*=-1;
                    TotalWanted.setText(wanted + " EGP");
                }
                adapter = new paymentAdapter();
                adapter.setPayments(payments);
                rec.setAdapter(adapter);
                rec.setHasFixedSize(true);
                rec.setLayoutManager(new LinearLayoutManager(paymentsActivity.this));
            }
        });

    }

    public void onComponentClick(View view){
        int id = view.getId();
        if(id == R.id.pay){
            if(!TextUtils.isEmpty(et.getText().toString())){
                double money = Double.parseDouble(et.getText().toString());
                payment pay = new payment((new SimpleDateFormat("EEEE//yyyy-M-dd", Locale.ENGLISH).format(new Date())),user,
                        money);
                vm.addPayment(pay);
                vm.getJsonObjectLiveData().observe(this, new Observer<JsonObject>() {
                    @Override
                    public void onChanged(JsonObject object) {
                        Toast.makeText(paymentsActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
                        et.getText().clear();
                        TotalWanted.setText(wanted - money + " EGP");
                        pays.add(pay);
                        adapter.setPayments(pays);
                        rec.setAdapter(adapter);
                        rec.getAdapter().notifyItemInserted(0);
                    }
                });
            }
            else{
                Toast.makeText(this, getResources().getString(R.string.enter_money), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(paymentsActivity.this,innerActivity.class);
        startActivity(intent);
        finish();
    }
}
