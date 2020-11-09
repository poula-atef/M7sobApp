package com.example.m7sob;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserModelView extends ViewModel {
    private MutableLiveData<JsonObject>jsonObjectLiveData = new MutableLiveData<>();
    private MutableLiveData<List<order>>ordersLiveData = new MutableLiveData<>();
    private MutableLiveData<List<payment>>paymentsLiveData = new MutableLiveData<>();

    public MutableLiveData<JsonObject> getJsonObjectLiveData() {
        return jsonObjectLiveData;
    }
    public void setJsonObjectLiveData(MutableLiveData<JsonObject> jsonObjectLiveData) {
        this.jsonObjectLiveData = jsonObjectLiveData;
    }
    public MutableLiveData<List<order>> getOrdersLiveData() {
        return ordersLiveData;
    }
    public void setOrdersLiveData(MutableLiveData<List<order>> ordersLiveData) {
        this.ordersLiveData = ordersLiveData;
    }
    public MutableLiveData<List<payment>> getPaymentsLiveData() {
        return paymentsLiveData;
    }
    public void setPaymentsLiveData(MutableLiveData<List<payment>> paymentsLiveData) {
        this.paymentsLiveData = paymentsLiveData;
    }

    private void getUserData(String user){
        Retrofit retrofit = new Retrofit.Builder()
                                .addConverterFactory(GsonConverterFactory.create())
                                .baseUrl("http://m7sob.herokuapp.com/")
                                .build();
        API service = retrofit.create(API.class);
        Call<JsonObject> call = service.getUser(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObjectLiveData.setValue(response.body());
                Log.i("tag","Succ!!!!!  " + user);
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.i("tag","Fail!!!!!  " + t.getMessage() + "   " + t.getCause());
            }
        });
    }
    public void getUser(String user){
        getUserData(user);
    }

    private void getUserOrdersData(String user){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m7sob.herokuapp.com/")
                .build();
        API service = retrofit.create(API.class);
        Call<List<order>> call = service.getUserOrders(user);
        call.enqueue(new Callback<List<order>>() {
            @Override
            public void onResponse(Call<List<order>> call, Response<List<order>> response) {
                ordersLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<order>> call, Throwable t) {

            }
        });
    }
    public void getUserOrders(String user){
        getUserOrdersData(user);
    }

    private void getUserPaymentsData(String user){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m7sob.herokuapp.com/")
                .build();
        API service = retrofit.create(API.class);
        Call<List<payment>> call = service.getUserPayments(user);
        call.enqueue(new Callback<List<payment>>() {
            @Override
            public void onResponse(Call<List<payment>> call, Response<List<payment>> response) {
                paymentsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<payment>> call, Throwable t) {

            }
        });
    }
    public void getUserPayments(String user){
        getUserPaymentsData(user);
    }

    private void addPaymentData(payment payment){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m7sob.herokuapp.com/")
                .build();
        API service = retrofit.create(API.class);
        Call<JsonObject> call = service.addUserPayment(payment);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObjectLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void addPayment(payment payment){
        addPaymentData(payment);
    }

    private void addOrderData(order order){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m7sob.herokuapp.com/")
                .build();
        API service = retrofit.create(API.class);
        Call<JsonObject> call = service.addUserOrder(order);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObjectLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void addOrder(order order){
        addOrderData(order);
    }

    private void removeUserOrderData(long code){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://m7sob.herokuapp.com/")
                .build();
        API service = retrofit.create(API.class);
        Call<JsonObject> call = service.removeUserOrder(code);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                jsonObjectLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }
    public void removeOrder(long code){
        removeUserOrderData(code);
    }

}
