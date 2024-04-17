package com.example.libraryapp.Model;

import java.io.Serializable;

public class Category implements Serializable {
    private String id;
    private String name;
    private String img;
    private int view;

    public Category() {
    }

    public Category(String name, String img,int view) {
        this.name = name;
        this.img = img;
        this.view = view;
    }

    public Category(String id, String name, String img,int view) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.view = view;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }
}
