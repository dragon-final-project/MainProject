package com.example.myapplication;

public class FavoriteData {
    private String recipe_id;

    public FavoriteData(String recipe_id){
        this.recipe_id = recipe_id;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }
}
