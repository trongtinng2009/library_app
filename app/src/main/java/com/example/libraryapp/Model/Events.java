package com.example.libraryapp.Model;

import java.io.Serializable;

public class Events implements Serializable {
    private int img;

    public Events()
    {

    }
    public Events(int i)
    {
        img = i;
    }
    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
