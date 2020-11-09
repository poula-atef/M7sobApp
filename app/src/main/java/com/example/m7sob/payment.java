package com.example.m7sob;

public class payment {
    private String date,user;
    private Double money;

    public payment() {
    }

    public payment(String date, String user, Double money) {
        this.date = date;
        this.user = user;
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getMoney() {
        return money;
    }

    public void setMoney(Double money) {
        this.money = money;
    }
}
