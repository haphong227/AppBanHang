package com.example.appbanhang.model;

import java.io.Serializable;

public class Like implements Serializable {
    private int img;

    public Like(int img) {
        this.img = img;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
