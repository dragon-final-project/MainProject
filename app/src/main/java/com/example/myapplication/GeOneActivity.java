package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeOneActivity extends AppCompatActivity {
    private String res;
    private Button btn;
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ge_one);
        btn = findViewById(R.id.btn);
        tv = findViewById(R.id.tv);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HashMap<String, String> paramsMap = new HashMap<>();
                        paramsMap.put("id","00003a70b1");

                        FormBody.Builder builder = new FormBody.Builder();
                        for (String key : paramsMap.keySet()) {
                            final String line = paramsMap.get(key);

                            builder.add(key, paramsMap.get(key));
                        }

                        OkHttpClient client = new OkHttpClient();
                        try {
                            RequestBody formBody = builder.build();
                            Request request = new Request.Builder().url("http://140.117.71.66/project/get_one_recipe_json.php").post(formBody).build();
                            Response response = client.newCall(request).execute();

                            res = response.body().string();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText(res);
                                }
                            });

                        } catch (IOException e) {
                            Toast.makeText(GeOneActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).start();
            }
        });
    }
}
