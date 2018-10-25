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

public class recipeResultAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;

    public recipeResultAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.ingredient_result_item,null);
            viewHolder = new ViewHolder(
                    (ConstraintLayout)convertView.findViewById(R.id.constraintLayoutItem),
                    (TextView)convertView.findViewById(R.id.tvIngredient),
                    (TextView)convertView.findViewById(R.id.tvNum));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        RecipeResultData data = (RecipeResultData) getItem(position);

        viewHolder.tvIngredient.setText(data.getName());
        viewHolder.tvNum.setText(Integer.toString(data.getCount()));

        return convertView;
    }

    private class ViewHolder {
        private ConstraintLayout constraintLayoutItem;
        private TextView tvIngredient,tvNum;

        public ViewHolder(ConstraintLayout constraintLayoutItem,TextView tvIngredient,TextView tvNum){
            this.constraintLayoutItem = constraintLayoutItem;
            this.tvIngredient = tvIngredient;
            this.tvNum = tvNum;
        }
    }
}
