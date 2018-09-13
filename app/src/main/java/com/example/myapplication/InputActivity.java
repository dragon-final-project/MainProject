package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class InputActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTextInput,btnCameraInput;
    private EditText etSearch;
    private TextView tvTitle;
    private static int INPUT_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        INPUT_TYPE = bundle.getInt("INPUT_TYPE");

        if(INPUT_TYPE==1){
            tvTitle.setText("吃什麼?");
        }
        else if(INPUT_TYPE==2){
            tvTitle.setText("煮什麼?");
        }
    }

    private void findViewId() {
        btnTextInput = findViewById(R.id.btnTextInput);
        btnCameraInput = findViewById(R.id.btnCameraInput);
        btnTextInput.setOnClickListener(this);
        btnCameraInput.setOnClickListener(this);
        tvTitle = findViewById(R.id.tvTitle);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnTextInput:
                final AlertDialog.Builder builder = new AlertDialog.Builder(InputActivity.this);
                final View dialogView = LayoutInflater.from(InputActivity.this).inflate(R.layout.text_input_dialog,null);
                Button button = dialogView.findViewById(R.id.btnSearch);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        etSearch = dialogView.findViewById(R.id.etSearch);
                        Intent intent = new Intent(InputActivity.this,SearchResultActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("searchText",etSearch.getText().toString());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                builder.setView(dialogView);
                builder.show();
                break;
            case R.id.btnCameraInput:
                break;
        }
    }
}
