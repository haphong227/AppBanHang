package com.example.appbanhang.model;

import java.io.Serializable;

public class Notification implements Serializable {
    String idNoti;
    String idBill;
    String email;
    String stateOrder;
    String time;

    public Notification() {
    }

    public Notification(String idNoti, String idBill, String email, String stateOrder, String time) {
        this.idNoti = idNoti;
        this.idBill = idBill;
        this.email = email;
        this.stateOrder = stateOrder;
        this.time = time;
    }

    public String getIdNoti() {
        return idNoti;
    }

    public void setIdNoti(String idNoti) {
        this.idNoti = idNoti;
    }

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStateOrder() {
        return stateOrder;
    }

    public void setStateOrder(String stateOrder) {
        this.stateOrder = stateOrder;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
