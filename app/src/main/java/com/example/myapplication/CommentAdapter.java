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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter {
    private LayoutInflater layoutInflater;

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.comment_item,null);
            viewHolder = new ViewHolder(
                    (ConstraintLayout)convertView.findViewById(R.id.constraintLayoutItem),
                    (TextView)convertView.findViewById(R.id.tvComment),
                    (TextView)convertView.findViewById(R.id.tvName),
                    (ImageView)convertView.findViewById(R.id.ivProfile));
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CommentData data = (CommentData) getItem(position);

        viewHolder.tvComment.setText(data.getContext());
        viewHolder.tvName.setText(data.getName());

        if (position%2==1){
            viewHolder.constraintLayoutItem.setBackgroundColor(Color.rgb(214,215,215));
        }

        return convertView;
    }

    private class ViewHolder {
        private ConstraintLayout constraintLayoutItem;
        private TextView tvComment,tvName;
        private ImageView ivProfile;

        public ViewHolder(ConstraintLayout constraintLayoutItem,TextView tvComment,TextView tvName,ImageView ivProfile){
            this.constraintLayoutItem = constraintLayoutItem;
            this.tvComment = tvComment;
            this.tvName = tvName;
            this.ivProfile = ivProfile;
        }
    }
}
