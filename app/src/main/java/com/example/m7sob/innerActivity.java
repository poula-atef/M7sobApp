package com.example.m7sob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class innerActivity extends AppCompatActivity {

    TextView name,total,profit,shop;
    EditText ordCode,shopMoney,paidMoney;
    String user,uname;
    float totalWanted = 0;
    UserModelView vm;
    List<order>orders = null;
    double allShop = 0.0,allClient = 0.0,allProfit = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inner);
        if(haveInternetConnection()) {
            user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", "no user");
            uname = PreferenceManager.getDefaultSharedPreferences(this).getString("name", "no name");
            name = findViewById(R.id.name);
            name.setText(uname);
            shop = findViewById(R.id.shop_day);
            profit = findViewById(R.id.profit_day);
            total = findViewById(R.id.total_day);
            shop = findViewById(R.id.shop_day);
            ordCode = findViewById(R.id.order_code);
            shopMoney = findViewById(R.id.shop_money);
            paidMoney = findViewById(R.id.client_paid);
            vm = ViewModelProviders.of(this).get(UserModelView.class);
            vm.getUserOrders(user);
            vm.getOrdersLiveData().observe(this, new Observer<List<order>>() {
                @Override
                public void onChanged(List<order> ords) {
                    orders = new ArrayList<>();
                    orders.addAll(ords);
                    for (int i = 0; i < orders.size(); i++) {
                        allShop += orders.get(i).getShop();
                        allClient += orders.get(i).getClient();
                    }
                    NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                    DecimalFormat df = (DecimalFormat) nf;
                    df.applyPattern("0.00");

                    allProfit = allClient - allShop;
                    total.setText(df.format(allClient) + " EGP");
                    shop.setText(df.format(allShop) + " EGP");
                    profit.setText(df.format(allProfit) + " EGP");
                }
            });
        }
        else{
            ((ConstraintLayout)findViewById(R.id.components)).setVisibility(View.GONE);
            ((ConstraintLayout)findViewById(R.id.no_connection)).setVisibility(View.VISIBLE);
        }
    }

    public void onComponentClick(View view) {
        if(haveInternetConnection()) {
            int id = view.getId();
            if (id == R.id.logout) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
                editor.putString("user", "no user");
                editor.putString("name", "no name");
                editor.commit();
                Intent intent = new Intent(innerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
            else if (id == R.id.add) {
                if (TextUtils.isEmpty(shopMoney.getText().toString()) || TextUtils.isEmpty(paidMoney.getText().toString()) || TextUtils.isEmpty(ordCode.getText().toString())) {
                    Toast.makeText(this, getResources().getString(R.string.req), Toast.LENGTH_SHORT).show();
                } else if (Float.parseFloat(shopMoney.getText().toString()) > Float.parseFloat(paidMoney.getText().toString())) {
                    Toast.makeText(this, getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                } else {
                    String date = new SimpleDateFormat("EEEE//yyyy-M-dd", Locale.ENGLISH).format(new Date());
                    String time = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(new Date());

                    vm.addOrder(new order(Long.parseLong(ordCode.getText().toString()), Double.parseDouble(shopMoney.getText().toString()),
                            Double.parseDouble(paidMoney.getText().toString()), user,date));
                    vm.getJsonObjectLiveData().observe(this, new Observer<JsonObject>() {
                        @Override
                        public void onChanged(JsonObject object) {
                            Toast.makeText(innerActivity.this, getResources().getString(R.string.added), Toast.LENGTH_SHORT).show();
                            NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                            DecimalFormat df = (DecimalFormat) nf;
                            df.applyPattern("0.00");
                            total.setText(df.format(allClient + Float.parseFloat(paidMoney.getText().toString())) + " EGP");
                            shop.setText(df.format(allShop + Float.parseFloat(shopMoney.getText().toString())) + " EGP");
                            profit.setText(df.format(allProfit + (Float.parseFloat(paidMoney.getText().toString()) - Float.parseFloat(shopMoney.getText().toString()))) + " EGP");

                            ordCode.getText().clear();
                            shopMoney.getText().clear();
                            paidMoney.getText().clear();
                        }
                    });

                }
            }
            else if(id == R.id.payments){
                Intent intent = new Intent(innerActivity.this,paymentsActivity.class);
                intent.putExtra("allShop",allShop);
                startActivity(intent);
                finish();
            }
            else if(id == R.id.day){
                Intent intent = new Intent(innerActivity.this,OrdersActivity.class);
                startActivity(intent);
                finish();
            }
        }
        else{
            Toast.makeText(this, getResources().getString(R.string.not_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public boolean haveInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info: networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI") || info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    return true;
        }
        return false;
    }
}