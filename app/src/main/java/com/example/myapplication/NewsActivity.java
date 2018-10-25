package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NewsActivity extends AppCompatActivity {
    private GridView gridView,gridView2;

    private String json_url = "http://140.117.71.66/project/get_all_recipe_json.php";
    private String hot_recipe_json_url = "http://140.117.71.66/project/get_hot_recipe.php";
    private GridViewAdapter adapter;
    private static ArrayList<DataColumn> list;
    private static ArrayList<DataColumn> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        findViewId();
        new JSONTask().execute(hot_recipe_json_url);
        new JSONTask().execute(json_url);
    }

    private void findViewId() {
        gridView = findViewById(R.id.gridView);
        gridView2 = findViewById(R.id.gridView2);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                String title = list.get(i).getTitle();
//                String name = list.get(i).getName();
                Bundle bundle = new Bundle();
                Intent intent = new Intent(NewsActivity.this,ItemDetailActivity.class);
                bundle.putString("id",list.get(i).getId());
                bundle.putString("title",list.get(i).getTitle());
                bundle.putString("name",list.get(i).getName());
                bundle.putString("created_at",list.get(i).getCreated_at());
                bundle.putString("img_path",list.get(i).getImg_path());
                //bundle.putByteArray("bitmap",bitmapToBytes(list.get(i).getBitmap()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        gridView2.setOnItemClickListener(new itemClickListener());
    }

    private class itemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent(NewsActivity.this,ItemDetailActivity.class);
            bundle.putString("id",list2.get(i).getId());
            bundle.putString("title",list2.get(i).getTitle());
            bundle.putString("name",list2.get(i).getName());
            bundle.putString("created_at",list2.get(i).getCreated_at());
            bundle.putString("img_path",list2.get(i).getImg_path());
            //bundle.putByteArray("bitmap",bitmapToBytes(list.get(i).getBitmap()));
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    private class JSONTask extends AsyncTask<String,String,String> {
        private String url;
        private ProgressDialog dialog = null;
        int progress;

        @Override
        protected String doInBackground(String... strings) {
            while(progress<20){
                progress++;
                SystemClock.sleep(20);
            }
            url = strings[0];
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
            dialog = ProgressDialog.show(NewsActivity.this, "", "資料載入中...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Gson gson = new Gson();
            DataColumn[] data = gson.fromJson(s,DataColumn[].class);
            if(url.equals(json_url)){
                list = new ArrayList<>(Arrays.asList(data));
                adapter = new GridViewAdapter(NewsActivity.this,1,list);
                setHorizontalGridView(list.size()+10,gridView);
                gridView.setAdapter(adapter);
            }
            else{
                list2 = new ArrayList<>(Arrays.asList(data));
                adapter = new GridViewAdapter(NewsActivity.this,1,list2);
                setHorizontalGridView(list2.size()+10,gridView2);
                gridView2.setAdapter(adapter);
            }
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
//        int itemWidth = (int) ((length) * density*2);
        int itemWidth = (int) ((length) * density*3);

        @SuppressWarnings("deprecation")
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                gridviewWidth, LinearLayout.LayoutParams.FILL_PARENT);
        gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
        gridView.setColumnWidth(itemWidth); // 设置列表项宽
        gridView.setHorizontalSpacing(100); // 设置列表项水平间距
        gridView.setStretchMode(GridView.NO_STRETCH);
        gridView.setNumColumns(size); // 设置列数量=列表集合数

    }

    private byte[] bitmapToBytes(Object o){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(o);
            return baos.toByteArray();
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }
}
