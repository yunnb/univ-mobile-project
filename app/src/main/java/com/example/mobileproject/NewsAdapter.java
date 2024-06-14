package com.example.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {
    private Context context;
    private List<News> newsList;

    public NewsAdapter(@NonNull Context context, @NonNull List<News> objects) {
        super(context, 0, objects);
        this.context = context;
        this.newsList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        News news = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        ImageView newsImage = convertView.findViewById(R.id.news_image);
        TextView newsTitle = convertView.findViewById(R.id.news_title);

        newsTitle.setText(news.getTitle());
        newsImage.setImageResource(news.getImageResId());

        return convertView;
    }
}
