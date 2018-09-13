package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class SearchResultActivity extends AppCompatActivity {

    private EditText etSearchBar;
    private String searchText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        searchText = bundle.getString("searchText");
        etSearchBar.setText(searchText);
    }

    private void findViewId() {
        etSearchBar = findViewById(R.id.etSearchBar);
    }
}
