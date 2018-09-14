package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ibLike;
    private TextView tvTitle,tvName,tvDate;
    private boolean isLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        tvTitle.setText(bundle.getString("title"));
        tvName.setText(bundle.getString("name"));
        tvDate.setText(bundle.getString("created_at"));
    }

    private void findViewId() {
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        ibLike = findViewById(R.id.ibLike);
        ibLike.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibLike:
                if(!isLike){
                    ibLike.setImageResource(R.drawable.favorite2);
                    Toast.makeText(ItemDetailActivity.this,"已加入收藏清單!",Toast.LENGTH_SHORT).show();
                }
                else{
                    ibLike.setImageResource(R.drawable.favorite1);
                    Toast.makeText(ItemDetailActivity.this,"已從收藏清單移除!",Toast.LENGTH_SHORT).show();
                }
                isLike = !isLike;
                break;
        }
    }
}
