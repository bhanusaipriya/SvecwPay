package com.svecw.a0563;

import android.net.Uri;

public class Users{
    public String name;
    public String thumb_image;
    public String balance;
    public String regdno;
    public String getRegdno() {
        return regdno;
    }

    public void setRegdno(String regdno) {
        this.regdno = regdno;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public Users() {
    }



    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public Users(String name, String thumb_image, String balance,String regdno) {
        this.name = name;
        this.thumb_image = thumb_image;
        this.balance = balance;
        this.regdno = regdno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
