package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter {

    private ArrayList<DataColumn> list;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull List<DataColumn> objects) {
        super(context, resource, objects);
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.item,null);
            viewHolder = new ViewHolder(
                    (ConstraintLayout)convertView.findViewById(R.id.constraintLayoutItem),
                    (TextView)convertView.findViewById(R.id.tvName),
                    (TextView)convertView.findViewById(R.id.tvCreator),
                    (ImageView)convertView.findViewById(R.id.imgMeal));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataColumn data = (DataColumn) getItem(position);
//        viewHolder.tvName.setText(data.getName());
//        viewHolder.tvIngredient.setText(data.getIngredients());
//        viewHolder.tvInstruction.setText(data.getInstruction());
        viewHolder.tvName.setText(data.getTitle());
        viewHolder.tvCreator.setText(data.getName());

        return convertView;
    }

    private class ViewHolder {
        private ConstraintLayout constraintLayoutItem;
        private TextView tvName,tvCreator;
        private ImageView imgMeal;

        public ViewHolder(ConstraintLayout constraintLayoutItem,TextView tvName,TextView tvCreator,ImageView imgMeal){
            this.constraintLayoutItem = constraintLayoutItem;
            this.tvName = tvName;
            this.tvCreator = tvCreator;
            this.imgMeal = imgMeal;
        }
    }
}
