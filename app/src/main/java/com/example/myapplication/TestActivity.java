package com.example.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    private static ArrayList<DataColumn> list;
    private GridViewAdapter adapter;
    private GridView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        listView = findViewById(R.id.listView);
        list = new ArrayList<>();
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));
        list.add(new DataColumn("涼菜湯","小當家"));

        adapter = new GridViewAdapter(TestActivity.this,1,list);

        setHorizontalGridView(list.size(), listView);

        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
