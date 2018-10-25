package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GridViewAdapter extends ArrayAdapter {

    private ArrayList<DataColumn> list;
    private LayoutInflater layoutInflater;

    public GridViewAdapter(@NonNull Context context, int resource, @NonNull List<DataColumn> objects) {
        super(context, resource, objects);
        this.list = (ArrayList) objects;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ViewHolder viewHolder;

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

        final DataColumn data = (DataColumn) getItem(position);
//        viewHolder.tvName.setText(data.getName());
//        viewHolder.tvIngredient.setText(data.getIngredients());
//        viewHolder.tvInstruction.setText(data.getInstruction());

        if(data.getTitle().length()>15){
            viewHolder.tvName.setText(data.getTitle().substring(0,15)+"...");
        }
        else{
            viewHolder.tvName.setText(data.getTitle());
        }
        viewHolder.tvCreator.setText(data.getName());

        if(!data.getImg_path().equals("none")){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = getBitmapFromURL(data.getImg_path());
                    viewHolder.imgMeal.post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.imgMeal.setImageBitmap(bitmap);
//                            for(int i=0;i<list.size();i++){
//                                if(i==list.size()-1){
//                                    list.get(i).setBitmap(bitmap);
//                                }
//                            }
                        }
                    });
                }
            }).start();
        }

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

    public static Bitmap getBitmapFromURL(String src){
        try{
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            input.close();
            return bitmap;
        }
        catch(IOException e){
            e.printStackTrace();
            return null;
        }
    }

}
