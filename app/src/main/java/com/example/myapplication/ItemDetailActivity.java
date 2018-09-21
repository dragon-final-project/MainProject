package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ItemDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton ibLike;
    private TextView tvTitle,tvName,tvDate;
    private boolean isLike = false;
    private ListView listView;

    private ArrayList<CommentData> list;
    private CommentAdapter adapter;
    private String json_url = "http://140.117.71.66/project/get_comment.php";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        tvTitle.setText(bundle.getString("title"));
        tvName.setText(bundle.getString("name"));
        tvDate.setText(bundle.getString("created_at"));

        new JSONTask().execute(json_url);
    }

    private void findViewId() {
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        ibLike = findViewById(R.id.ibView);
        ibLike.setOnClickListener(this);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ibView:
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

    private class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("id", id);

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(json_url).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Toast.makeText(ItemDetailActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Gson gson = new Gson();
            CommentData[] data = gson.fromJson(s,CommentData[].class);
            list = new ArrayList<>(Arrays.asList(data));

            adapter = new CommentAdapter(ItemDetailActivity.this,1,list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
