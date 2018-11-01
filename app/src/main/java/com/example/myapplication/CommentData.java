package com.example.myapplication;

public class CommentData {
    private String context;
    private String user_id;
    private String name;
    private String pic_path;

    public CommentData(String context, String name, String pic_path) {
        this.context = context;
        this.name = name;
        this.pic_path = pic_path;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic_path() {
        return pic_path;
    }

    public void setPic_path(String pic_path) {
        this.pic_path = pic_path;
    }
}
