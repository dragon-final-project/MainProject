package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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

public class CommentAdapter extends BaseAdapter {
    private ArrayList<CommentData> list;
    private LayoutInflater layoutInflater;

//    public CommentAdapter(@NonNull Context context, int resource, @NonNull List objects) {
//        super(context, resource, objects);
//        layoutInflater = LayoutInflater.from(context);
//    }
    public CommentAdapter(Context context, ArrayList<CommentData> list){
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
        final ViewHolder viewHolder;

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

        final CommentData data = (CommentData) getItem(position);

        viewHolder.tvComment.setText(data.getContext());
        viewHolder.tvName.setText(data.getName());

        if(data.getPic_path().length()!=0){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = GridViewAdapter.getBitmapFromURL(data.getPic_path());
                    viewHolder.ivProfile.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.ivProfile.setImageBitmap(bitmap);
                        }
                    });
                }
            }).start();
        }

      /*  if ((position+1)%2!=0){
            viewHolder.constraintLayoutItem.setBackgroundColor(Color.rgb(214,215,215));
        }  */

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
