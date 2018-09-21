package com.example.myapplication;

import java.util.ArrayList;

public class DataColumn {
//    private String name;
//    private String ingredients;
//    private String instruction;

//    private String id;
//    private String creator_id;
//    private String title;
//    private String avg_star;
//    private String img_path;
    private String id;
    private String title;
    private String name;
    private String created_at;

    public DataColumn(String id,String title,String name,String created_at){
        this.id = id;
        this.title = title;
        this.name = name;
        this.created_at = created_at;
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
}
