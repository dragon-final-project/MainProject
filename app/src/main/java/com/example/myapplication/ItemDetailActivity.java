package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
    private Button btnIngredient,btnInstruction,btnSubmit;
    private EditText etComment;
    private TextView tvTitle,tvName,tvDate;
    private boolean isLike = false;
    private ListView listView;
    private ImageView ivPic;

    private ArrayList<CommentData> list;
    private ArrayList<FavoriteData> favorite_list;
    private CommentAdapter adapter;
    private String json_url = "http://140.117.71.66/project/get_comment.php";
    private String insert_favorite_url = "http://140.117.71.66/project/add_favorite.php";
    private String delete_favorite_url = "http://140.117.71.66/project/delete_favorite.php";
    private String get_all_favorite_url = "http://140.117.71.66/project/get_all_favorite.php";
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        findViewId();
        final Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
        tvTitle.setText(bundle.getString("title"));
        tvName.setText(bundle.getString("name"));
        tvDate.setText(bundle.getString("created_at"));

//        Bitmap bitmap = (Bitmap) bytesToBitmap(bundle.getByteArray("bitmap"));
//        ivPic.setImageBitmap(bitmap);

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = GridViewAdapter.getBitmapFromURL(bundle.getString("img_path"));
                ivPic.post(new Runnable() {
                    @Override
                    public void run() {
                        ivPic.setImageBitmap(bitmap);
                    }
                });
            }
        }).start();

        new JSONTask().execute(json_url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(MainActivity.user_id!=null){
            new FavoriteTask().execute(get_all_favorite_url);
        }


//        if(!isLike){
//            ibLike.setImageResource(R.drawable.favorite1);
//        }
//        else{
//            ibLike.setImageResource(R.drawable.favorite2);
//        }
    }

    private void findViewId() {
        tvTitle = findViewById(R.id.tvTitle);
        tvName = findViewById(R.id.tvName);
        tvDate = findViewById(R.id.tvDate);
        ibLike = findViewById(R.id.ibView);
        ibLike.setOnClickListener(this);
        btnIngredient = findViewById(R.id.btnIngredient);
        btnInstruction = findViewById(R.id.btnInstruction);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnIngredient.setOnClickListener(this);
        btnInstruction.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etComment = findViewById(R.id.etComment);
        ivPic = findViewById(R.id.ivPic);
        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()){
            case R.id.ibView:
                if(MainActivity.user_id==null){
                    Toast.makeText(ItemDetailActivity.this,"請先登入再進行收藏功能!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!isLike){
                        ibLike.setImageResource(R.drawable.favorite2);
                        new FavoriteTask().execute(insert_favorite_url);
                        Toast.makeText(ItemDetailActivity.this,"已加入收藏清單!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ibLike.setImageResource(R.drawable.favorite1);
                        new FavoriteTask().execute(delete_favorite_url);
                        Toast.makeText(ItemDetailActivity.this,"已從收藏清單移除!",Toast.LENGTH_SHORT).show();
                    }
                    isLike = !isLike;
                }
                break;
            case R.id.btnIngredient:
                intent = new Intent(ItemDetailActivity.this,IngredientActivity.class);
                bundle = new Bundle();
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnInstruction:
                intent = new Intent(ItemDetailActivity.this,InstructionActivity.class);
                bundle = new Bundle();
                bundle.putString("id",id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.btnSubmit:
                etComment.setText("");
                Toast.makeText(ItemDetailActivity.this,"訊息已傳送!",Toast.LENGTH_SHORT).show();
                break;
        }
    }
//
    private class JSONTask extends AsyncTask<String,String,String> {
        private ProgressDialog dialog = null;
        int progress;

        @Override
        protected String doInBackground(String... strings) {
            while(progress<20){
                progress++;
                SystemClock.sleep(20);
            }
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
            dialog = ProgressDialog.show(ItemDetailActivity.this, "", "資料載入中...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();

            Gson gson = new Gson();
            CommentData[] data = gson.fromJson(s,CommentData[].class);
            list = new ArrayList<>(Arrays.asList(data));

            adapter = new CommentAdapter(ItemDetailActivity.this,1,list);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private class FavoriteTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> paramsMap = new HashMap<>();
            paramsMap.put("user_id",MainActivity.user_id);
            paramsMap.put("id", id);

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                builder.add(key, paramsMap.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(strings[0]).post(formBody).build();
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
            FavoriteData[] data = gson.fromJson(s,FavoriteData[].class);
            favorite_list = new ArrayList<>(Arrays.asList(data));
//            Toast.makeText(ItemDetailActivity.this,id,Toast.LENGTH_SHORT).show();

            for(int i=0;i<favorite_list.size();i++){
                if(favorite_list.get(i).getRecipe_id().equals(id)){
                    isLike = true;
                    ibLike.setImageResource(R.drawable.favorite2);
                    break;
                }
                else{
                    isLike = false;
                    ibLike.setImageResource(R.drawable.favorite1);
                }
                //Toast.makeText(ItemDetailActivity.this,favorite_list.get(i).getRecipe_id(),Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Object bytesToBitmap( byte raw[] ) {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(raw);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Object o = ois.readObject();
            return o;
        }
        catch (IOException e){
            e.printStackTrace();
            return null;
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }
    }
}
