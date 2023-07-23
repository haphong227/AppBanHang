package com.example.appbanhang.model;

import java.io.Serializable;

public class Food implements Serializable {
    private String des;
    private String id;
    private String idCate;
    private String image;
    private String name;
    private String price;
    private int quantity;

    public Food() {
    }

    public Food(String des, String id, String idCate, String image, String name, String price, int quantity) {
        this.des = des;
        this.id = id;
        this.idCate = idCate;
        this.image = image;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public String getIdCate() {
        return idCate;
    }

    public void setIdCate(String idCate) {
        this.idCate = idCate;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}