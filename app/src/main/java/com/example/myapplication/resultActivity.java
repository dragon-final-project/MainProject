package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class resultActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvName;
    private Button btnUpload;
    private ListView listView,listView2;
    private recipeResultAdapter adapter;
    private StepAdapter adapter2;
    private static ArrayList<RecipeResultData> list;
    private static ArrayList<StepData> list2;

    int n,m;
    String Name;
    String[] Food = new String[30];
    String[] Number=new String[30];
    String[] Method=new String[10];
    String[] FoodNumber = new String[30];

    private String post_url = "http://140.117.71.66/project/add_recipe.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        tvName = findViewById(R.id.tvName);
        btnUpload = findViewById(R.id.btnUpload);
        btnUpload.setOnClickListener(this);

        Bundle bundle =this.getIntent().getExtras();

        Food = bundle.getStringArray("Food");
        n=bundle.getInt("n");
        m=bundle.getInt("m");
        Number = bundle.getStringArray("Number");
        Method = bundle.getStringArray("Method");
        Name = bundle.getString("Name");
        tvName.setText(Name);

        listView = findViewById(R.id.favorite_list);

        list = new ArrayList<>();

        for(int i=0;i<n;i++){
            list.add(new RecipeResultData(Food[i],Integer.parseInt(Number[i])));
            FoodNumber[i] = Food[i]+" "+Number[i];
        }

        adapter = new recipeResultAdapter(resultActivity.this,1,list);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();

        //--------------------
        listView2 = findViewById(R.id.listView2);

        list2 = new ArrayList<>();

        for(int i=0;i<m;i++){
            list2.add(new StepData(Integer.toString(i+1),Method[i]));
        }

        adapter2 = new StepAdapter(resultActivity.this,1,list2);
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
        //Test();
    }

    public void setListViewHeightBasedOnChildren(ListView listView){ //建立函數動態設定ListView的高度
        //取得ListView的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        //固定每一行的高度
        int itemHeight = 50;
        if (listAdapter == null) return;
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            if(totalHeight<300) {
                totalHeight += Dp2Px(getApplicationContext(), itemHeight) + listView.getDividerHeight();
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;
        listView.setLayoutParams(params);
    }

    public int Dp2Px(Context context, float dp) { //建立函數將dp轉換為像素
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpload:
                new SubmitTask().execute(post_url);
                //Toast.makeText(resultActivity.this,"新增成功!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(resultActivity.this,MainActivity.class);
                startActivity(intent);
                break;
        }
    }

    private class SubmitTask extends AsyncTask<String,String,String> {

        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String[]> paramsMap = new HashMap<>();
            paramsMap.put("creator_id",new String[]{MainActivity.user_id});
            paramsMap.put("title",new String[]{Name}); //菜名
            paramsMap.put("text[]",FoodNumber); //食材與數量
            paramsMap.put("step[]",Method); //步驟

            FormBody.Builder builder = new FormBody.Builder();
            for (String key : paramsMap.keySet()) {
                for (int i=0;i<paramsMap.get(key).length;i++){
                    String [] s = paramsMap.get(key);
                    if(s[i]!=null){
                        builder.add(key,s[i]);
                    }
                }
            }

            OkHttpClient client = new OkHttpClient();
            try {
                RequestBody formBody = builder.build();
                Request request = new Request.Builder().url(post_url).post(formBody).build();
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (IOException e) {
                Toast.makeText(resultActivity.this, "網路連線錯誤!", Toast.LENGTH_SHORT).show();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("successful")){
                Toast.makeText(resultActivity.this, "上傳食譜成功!", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(resultActivity.this, "上傳失敗!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Test(){
        HashMap<String, String[]> paramsMap = new HashMap<>();
        String result = "";
        paramsMap.put("creator_id",new String[]{MainActivity.user_id});
        paramsMap.put("title",new String[]{Name}); //菜名
        paramsMap.put("text",FoodNumber); //食材與數量
        paramsMap.put("step",Method); //步驟

        for (String key : paramsMap.keySet()) {
            for (int i=0;i<paramsMap.get(key).length;i++){
                String [] s = paramsMap.get(key);
                if(s[i]!=null){
                    result += key+"-"+i+" : "+s[i]+"\n";
                }
            }
        }
        Toast.makeText(resultActivity.this,result,Toast.LENGTH_LONG).show();
    }
}
