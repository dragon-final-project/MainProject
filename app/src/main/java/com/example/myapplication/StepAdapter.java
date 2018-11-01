package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends BaseAdapter {
    private ArrayList<StepData> list;
    private LayoutInflater layoutInflater;

//    public StepAdapter(@NonNull Context context, int resource, @NonNull List objects) {
//        super(context, resource, objects);
//        layoutInflater = LayoutInflater.from(context);
//    }
    public StepAdapter(Context context, ArrayList<StepData> list){
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        //these two methods tells the system that whether adapter
        // needs to recycle the rows or not, and also how many types of rows we want to display
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.step,null);
            viewHolder = new ViewHolder(
                    (ConstraintLayout)convertView.findViewById(R.id.constraintLayoutItem),
                    (TextView)convertView.findViewById(R.id.tvStep),
                    (TextView)convertView.findViewById(R.id.tvText));
                    //(ImageView)convertView.findViewById(R.id.ivStep));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        StepData data = (StepData) getItem(position);

        viewHolder.tvStep.setText("Step "+data.getStep());
        viewHolder.tvText.setText(data.getText());

        if((position+1)%2!=0){
            viewHolder.constraintLayoutItem.setBackgroundColor(Color.rgb(226,235,203));
        }

        return convertView;
    }

    private class ViewHolder {
        private ConstraintLayout constraintLayoutItem;
        private TextView tvStep,tvText;
        //private ImageView ivStep;

        public ViewHolder(ConstraintLayout constraintLayoutItem,TextView tvStep,TextView tvText){
            this.constraintLayoutItem = constraintLayoutItem;
            this.tvStep = tvStep;
            this.tvText = tvText;
            //this.ivStep = ivStep;
        }
    }
}
