package com.example.appbanhang.model;

import android.provider.SearchRecentSuggestions;

import java.io.Serializable;

public class Cart implements Serializable {
    String idFood;
    String productImg;
    String productName, productPrice;
    String totalPrice;
    String totalQuantity;


    public Cart() {
    }

    public Cart(String idFood, String productImg, String productName, String productPrice, String totalPrice, String totalQuantity) {
        this.idFood = idFood;
        this.productImg = productImg;
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalPrice = totalPrice;
        this.totalQuantity = totalQuantity;
    }

    public String getIdFood() {
        return idFood;
    }

    public void setIdFood(String idFood) {
        this.idFood = idFood;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getProductImg() {
        return productImg;
    }

    public void setProductImg(String productImg) {
        this.productImg = productImg;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
