package com.example.m7sob;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {
    @GET("getUserOrders")
    Call<List<order>> getUserOrders(@Query("user") String user);

    @GET("getUser")
    Call<JsonObject> getUser(@Query("user") String user);

    @GET("getUserPayments")
    Call<List<payment>> getUserPayments(@Query("user") String user);

    @POST("addPayment")
    Call<JsonObject> addUserPayment(@Body payment payment);

    @POST("addOrder")
    Call<JsonObject> addUserOrder(@Body order order);

    @GET("removeUserOrder")
    Call<JsonObject> removeUserOrder(@Query("code") long code);
}
