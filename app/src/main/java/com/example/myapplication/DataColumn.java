package com.example.myapplication;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class DataColumn {

    private String id;
    private String title;
    private String name;
    private String created_at;
    private String img_path;
    private Bitmap bitmap;

    public DataColumn(String id,String title,String name,String created_at,String img_path,Bitmap bitmap){
        this.id = id;
        this.title = title;
        this.name = name;
        this.created_at = created_at;
        this.img_path = img_path;
        this.bitmap = bitmap;
    }

    public DataColumn(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
