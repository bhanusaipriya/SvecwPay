package com.svecw.a0563;

public class Inner {
    private String date;
    private  String amount;
    private String type;

    public Inner() {
    }

    public String getAmount() {
        return amount;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public Inner(String date, String amount, String type) {
        this.date = date;
        this.amount = amount;
        this.type = type;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
