package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchResultData implements Parcelable {
    private ArrayList<StepData> instructions;
    private String name,title,img_path,avg_star;
    private ArrayList<StepData> ingredients;
    private String id;

    protected SearchResultData(Parcel in) {
        name = in.readString();
        title = in.readString();
        img_path = in.readString();
        avg_star = in.readString();
        id = in.readString();
    }

    public static final Creator<SearchResultData> CREATOR = new Creator<SearchResultData>() {
        @Override
        public SearchResultData createFromParcel(Parcel in) {
            return new SearchResultData(in);
        }

        @Override
        public SearchResultData[] newArray(int size) {
            return new SearchResultData[size];
        }
    };

    public ArrayList<StepData> getInstructions() {
        return instructions;
    }

    public void setInstructions(ArrayList<StepData> instructions) {
        this.instructions = instructions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getAvg_star() {
        return avg_star;
    }

    public void setAvg_star(String avg_star) {
        this.avg_star = avg_star;
    }

    public ArrayList<StepData> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<StepData> ingredients) {
        this.ingredients = ingredients;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(title);
        parcel.writeString(img_path);
        parcel.writeString(avg_star);
        parcel.writeString(id);
    }
}