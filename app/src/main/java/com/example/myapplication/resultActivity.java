package com.example.myapplication;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class resultActivity extends AppCompatActivity {
    private ListView listView,listView2;
    private recipeResultAdapter adapter;
    private StepAdapter adapter2;
    private static ArrayList<RecipeResultData> list;
    private static ArrayList<StepData> list2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listView = findViewById(R.id.listView);

        list = new ArrayList<>();
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));
        list.add(new RecipeResultData("小黃瓜",2));

        adapter = new recipeResultAdapter(resultActivity.this,1,list);
        listView.setAdapter(adapter);
        setListViewHeightBasedOnChildren(listView);
        adapter.notifyDataSetChanged();

        //--------------------
        listView2 = findViewById(R.id.listView2);

        list2 = new ArrayList<>();
        list2.add(new StepData("1","步驟步驟步驟步驟步驟步驟步驟步驟步驟"));
        list2.add(new StepData("2","步驟步驟步驟步驟步驟步驟步驟步驟步驟"));
        list2.add(new StepData("3","步驟步驟步驟步驟步驟步驟步驟步驟步驟"));
        list2.add(new StepData("4","步驟步驟步驟步驟步驟步驟步驟步驟步驟"));
        list2.add(new StepData("5","步驟步驟步驟步驟步驟步驟步驟步驟步驟"));

        adapter2 = new StepAdapter(resultActivity.this,1,list2);
        listView2.setAdapter(adapter2);
        adapter2.notifyDataSetChanged();
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
}
