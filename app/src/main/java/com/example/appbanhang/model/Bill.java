package com.example.appbanhang.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Bill implements Serializable {
    String address;
    String CurrentTime;
    String CurrentDate;
    String idBill;
    String idOrder;
    String email;
    String price;
    int quantity;
    ArrayList<Cart> cartArrayList;

    public Bill() {
    }

    public Bill(String address, String currentTime, String currentDate, String idBill, String idOrder, String email, String price, int quantity, ArrayList<Cart> cartArrayList) {
        this.address = address;
        CurrentTime = currentTime;
        CurrentDate = currentDate;
        this.idBill = idBill;
        this.idOrder = idOrder;
        this.email = email;
        this.price = price;
        this.quantity = quantity;
        this.cartArrayList = cartArrayList;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getIdUser() {
        return email;
    }

    public void setIdUser(String idUser) {
        this.email = idUser;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<Cart> getCartArrayList() {
        return cartArrayList;
    }

    public void setCartArrayList(ArrayList<Cart> cartArrayList) {
        this.cartArrayList = cartArrayList;
    }
}
