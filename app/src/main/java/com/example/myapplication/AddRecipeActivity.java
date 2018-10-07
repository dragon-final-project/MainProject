package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;

public class AddRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

    }
    public void onClick(View v){
        EditText ett = (EditText)findViewById(R.id.ett);
        Editable StrName;
        StrName = ett.getText();
        String Name = StrName.toString();

        Intent intent = new Intent();
        intent.setClass(this, AddIngredientActivity.class);
        intent.putExtra("name",Name);

        startActivity(intent);

    }
}
