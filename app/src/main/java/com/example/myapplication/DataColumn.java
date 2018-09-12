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
    private String title;
    private String name;

    public DataColumn(String title,String name){
        this.title = title;
        this.name = name;
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
}
