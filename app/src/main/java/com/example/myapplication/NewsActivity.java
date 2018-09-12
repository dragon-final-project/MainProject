package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    private GridView gridView;
    private ProgressBar progressBar;

    private String json_url = "http://140.117.71.66/project/get_all_recipe_json.php";
    private GridViewAdapter adapter;
    private static ArrayList<DataColumn> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        findViewId();
        new JSONTask().execute(json_url);
    }

    private void findViewId() {
        gridView = findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    private class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            try {
                Request request = new Request.Builder().url(strings[0]).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            }catch (IOException e){
                Toast.makeText(NewsActivity.this,"網路連線錯誤!",Toast.LENGTH_SHORT).show();
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
            DataColumn[] data = gson.fromJson(s,DataColumn[].class);
            list = new ArrayList<>(Arrays.asList(data));

            adapter = new GridViewAdapter(NewsActivity.this,1,list);
            gridView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
