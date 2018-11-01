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
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity {
    private GridView gridView,gridView2;
    private EditText etSearchBar;
    private String searchText;
    ArrayList<SearchResultData> data;
    ArrayList<SearchResultData> main_result = new ArrayList<>();
    ArrayList<SearchResultData> other_result = new ArrayList<>();

    private SearchResultAdapter adapter;
    private int padding = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        findViewId();
        Bundle bundle = getIntent().getExtras();
        data = bundle.getParcelableArrayList("SearchResultData");
        main_result.add(data.get(0));
        for(int i=1;i<data.size();i++){
            other_result.add(data.get(i));
        }

//        searchText = bundle.getString("searchText");
//        etSearchBar.setText(searchText);

//        new JSONTask().execute(json_url);

        adapter = new SearchResultAdapter(SearchResultActivity.this,main_result);
        setHorizontalGridView(data.size(),gridView);
        gridView.setPadding(padding,0,0,0);
        gridView.setAdapter(adapter);

        adapter = new SearchResultAdapter(SearchResultActivity.this,other_result);
        setHorizontalGridView(data.size()+8,gridView2);
        gridView2.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void findViewId() {
        etSearchBar = findViewById(R.id.etSearchBar);

        gridView = findViewById(R.id.gridView);
        gridView2 = findViewById(R.id.gridView2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(SearchResultActivity.this,ItemDetailActivity.class);
                bundle.putString("id",main_result.get(i).getId());
                bundle.putString("title",main_result.get(i).getTitle());
                bundle.putString("name",main_result.get(i).getName());
                bundle.putString("created_at",main_result.get(i).getCreated_at());
                bundle.putString("img_path",main_result.get(i).getImg_path());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(SearchResultActivity.this,ItemDetailActivity.class);
                bundle.putString("id",other_result.get(i).getId());
                bundle.putString("title",other_result.get(i).getTitle());
                bundle.putString("name",other_result.get(i).getName());
                bundle.putString("created_at",other_result.get(i).getCreated_at());
                bundle.putString("img_path",other_result.get(i).getImg_path());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
        int gridviewWidth = (int) (size * (length) * density)+100;
        int itemWidth = (int) ((length) * density*3);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(100); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

        padding = (dm.widthPixels - gridviewWidth)/2 + 100;
    }
}
