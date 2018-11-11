package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
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
import com.bumptech.glide.Glide;

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
    private String insert_comment_url = "http://140.117.71.66/project/add_comment.php";
    private String id;
    private float star;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);

        ImageView imageView = (ImageView) findViewById(R.id.ivPic);
        Glide.with(this).load(R.drawable.loading).into(imageView);

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
        ibLike = findViewById(R.id.ivView);
        ibLike.setOnClickListener(this);
        btnIngredient = findViewById(R.id.btnIngredient);
        btnInstruction = findViewById(R.id.btnInstruction);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnIngredient.setOnClickListener(this);
        btnInstruction.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        etComment = findViewById(R.id.etComment);
        ivPic = findViewById(R.id.ivPic);
        listView = findViewById(R.id.comment_list);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(list.get(i).getUser_id().equals(MainActivity.user_id)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                    builder.setTitle("刪除評論");
                    builder.setMessage("確定要刪除這筆評論嗎?");
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(ItemDetailActivity.this,"評論已刪除!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                    builder.setTitle("檢舉評論");
                    builder.setMessage("確定要檢舉這筆評論嗎?");
                    builder.setNegativeButton("取消",null);
                    builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(ItemDetailActivity.this,"檢具訊息已送出!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();
                }
                return true;
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle bundle;
        switch (view.getId()){
            case R.id.ivView:
                if(!MainActivity.isLogin){
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
                if(!MainActivity.isLogin){
                    Toast.makeText(ItemDetailActivity.this,"請先登入再進行評論功能!",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(etComment.length()==0) Toast.makeText(ItemDetailActivity.this,"請輸入評論內容!",Toast.LENGTH_SHORT).show();
                    else{
                        AlertDialog.Builder builder = new AlertDialog.Builder(ItemDetailActivity.this);
                        View dialogView = LayoutInflater.from(ItemDetailActivity.this).inflate(R.layout.rating_bar,null);
                        Button btnRating = dialogView.findViewById(R.id.btnRating);
                        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
                        final RatingBar ratingBar = dialogView.findViewById(R.id.ratingBar);
                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                star = 0;
                                addComment();
                                Toast.makeText(ItemDetailActivity.this,"訊息已傳送!",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                        btnRating.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if(ratingBar.getRating()==0){
                                    Toast.makeText(ItemDetailActivity.this,"請選擇評分星等!",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    star = ratingBar.getRating();
                                    addComment();
                                    Toast.makeText(ItemDetailActivity.this,"訊息已傳送!",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                        });
                        builder.setView(dialogView);
                        builder.show();
//                        etComment.setText("");
//                        Toast.makeText(ItemDetailActivity.this,"訊息已傳送!",Toast.LENGTH_SHORT).show();
                    }
                }
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
            if (list.size()==0){
                list.add(new CommentData("此文章尚未有留言唷!","See Food小編",""));
            }

            adapter = new CommentAdapter(ItemDetailActivity.this,list);
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

    public void addComment(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, String> paramsMap = new HashMap<>();
                paramsMap.put("user_id",MainActivity.user_id);
                paramsMap.put("id", id);
                paramsMap.put("star",Float.toString(star));
                paramsMap.put("context",etComment.getText().toString());

                FormBody.Builder builder = new FormBody.Builder();
                for (String key : paramsMap.keySet()) {
                    builder.add(key, paramsMap.get(key));
                }

                OkHttpClient client = new OkHttpClient();
                try {
                    RequestBody formBody = builder.build();
                    Request request = new Request.Builder().url(insert_comment_url).post(formBody).build();
                    Response response = client.newCall(request).execute();
                } catch (IOException e) {
                    Toast.makeText(ItemDetailActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
    }
}
