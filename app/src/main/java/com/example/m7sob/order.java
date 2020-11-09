package com.example.m7sob;

public class order {
    private long code;
    private Double shop,client;
    private String user;
    private String date;
    public order() {
    }

    public order(long code, Double shop, Double client, String user, String date) {
        this.code = code;
        this.shop = shop;
        this.client = client;
        this.user = user;
        this.date = date;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public Double getShop() {
        return shop;
    }

    public void setShop(Double shop) {
        this.shop = shop;
    }

    public Double getClient() {
        return client;
    }

    public void setClient(Double client) {
        this.client = client;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
