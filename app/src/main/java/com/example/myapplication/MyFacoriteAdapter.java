package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class MyFacoriteAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;

    public MyFacoriteAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.favorite_item,null);
            viewHolder = new ViewHolder(
                    (ConstraintLayout)convertView.findViewById(R.id.constraintLayoutItem),
                    (TextView)convertView.findViewById(R.id.tvTitle),
                    (TextView)convertView.findViewById(R.id.tvName));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataColumn data = (DataColumn) getItem(position);

        viewHolder.tvTitle.setText(data.getTitle());
        viewHolder.tvName.setText(data.getName());

        return convertView;
    }

    private class ViewHolder {
        private ConstraintLayout constraintLayoutItem;
        private TextView tvTitle,tvName;

        public ViewHolder(ConstraintLayout constraintLayoutItem,TextView tvTitle,TextView tvName){
            this.constraintLayoutItem = constraintLayoutItem;
            this.tvTitle = tvTitle;
            this.tvName = tvName;
        }
    }
}
