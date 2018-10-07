package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity {
    private GridView gridView,gridView2;
    private EditText etSearchBar;
    private String searchText;

    private String json_url = "http://140.117.71.66/project/get_search_result.php";
    private GridViewAdapter adapter;
    private static ArrayList<DataColumn> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        searchText = bundle.getString("searchText");
        etSearchBar.setText(searchText);

        new JSONTask().execute(json_url);
    }

    private void findViewId() {
        etSearchBar = findViewById(R.id.etSearchBar);

        gridView = findViewById(R.id.gridView);
        gridView2 = findViewById(R.id.gridView2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String title = list.get(i).getTitle();
//                String name = list.get(i).getName();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(SearchResultActivity.this,ItemDetailActivity.class);
                bundle.putString("id",list.get(i).getId());
                bundle.putString("title",list.get(i).getTitle());
                bundle.putString("name",list.get(i).getName());
                bundle.putString("created_at",list.get(i).getCreated_at());
                intent.putExtras(bundle);
                startActivity(intent);
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
                Toast.makeText(SearchResultActivity.this,"網路連線錯誤!",Toast.LENGTH_SHORT).show();
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

            adapter = new GridViewAdapter(SearchResultActivity.this,1,list);
            setHorizontalGridView(list.size()+10,gridView);
            setHorizontalGridView(list.size()+10,gridView2);
            gridView.setAdapter(adapter);
            //gridView2.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void setHorizontalGridView(int siz, GridView gridView) {
        int size = siz;
//      int length = (int) getActivity().getResources().getDimension(
//              R.dimen.coreCourseWidth);
        int length=60;
        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        float density = dm.density;
        int gridviewWidth = (int) (size * (length) * density);
        int itemWidth = (int) ((length) * density*2);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(100); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

    }
}
