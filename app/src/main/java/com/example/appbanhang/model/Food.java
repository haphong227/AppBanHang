package com.example.appbanhang.model;

import java.io.Serializable;

public class Food implements Serializable {
    private int img;
    private String txtName, txtPrice;

    public Food() {
    }

    public Food(int img, String txtName, String txtPrice) {
        this.img = img;
        this.txtName = txtName;
        this.txtPrice = txtPrice;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public String getTxtName() {
        return txtName;
    }

    public void setTxtName(String txtName) {
        this.txtName = txtName;
    }

    public String getTxtPrice() {
        return txtPrice;
    }

    public void setTxtPrice(String txtPrice) {
        this.txtPrice = txtPrice;
    }
}
