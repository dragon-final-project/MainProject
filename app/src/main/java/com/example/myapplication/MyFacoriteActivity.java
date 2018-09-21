package com.example.myapplication;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
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

public class MyFacoriteActivity extends AppCompatActivity {
    private ListView listView;
    private String json_url = "http://140.117.71.66/project/get_favorite.php";
    private MyFacoriteAdapter adapter;
    private static ArrayList<DataColumn> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_facorite);

        findViewId();
//        list = new ArrayList<>();
//        list.add(new DataColumn("1","普羅旺斯牛排","小當家","2018.09.20"));
//
//        adapter = new MyFacoriteAdapter(MyFacoriteActivity.this,1,list);
//        listView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
        new JSONTask().execute(json_url);
    }

    private void findViewId() {
        listView = findViewById(R.id.listView);
    }

    private class JSONTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("user_id",MainActivity.user_id);

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
                Toast.makeText(MyFacoriteActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
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

            adapter = new MyFacoriteAdapter(MyFacoriteActivity.this,1,list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
