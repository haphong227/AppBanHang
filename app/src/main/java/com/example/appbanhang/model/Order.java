package com.example.appbanhang.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    List<Cart> listItem;
    String idOrder;
    String price;
    User user;

    public Order(ArrayList<Cart> cartArrayList, String total, String uid) {
    }

    public Order(List<Cart> listItem, String idOrder, String price, User user) {
        this.listItem = listItem;
        this.idOrder = idOrder;
        this.price = price;
        this.user = user;
    }

    public String getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(String idOrder) {
        this.idOrder = idOrder;
    }

    public List<Cart> getListItem() {
        return listItem;
    }

    public void setListItem(List<Cart> listItem) {
        this.listItem = listItem;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
