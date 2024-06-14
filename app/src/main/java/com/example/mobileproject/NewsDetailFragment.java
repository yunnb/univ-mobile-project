package com.example.mobileproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NewsDetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_detail, container, false);

        ImageView newsImageView = view.findViewById(R.id.news_detail_image);
        TextView newsTitleView = view.findViewById(R.id.news_detail_title);
        TextView newsContentView = view.findViewById(R.id.news_detail_content);

        // Get data from arguments
        Bundle args = getArguments();
        if (args != null) {
            String newsTitle = args.getString("news_title");
            int newsImageResId = args.getInt("news_image");
            String newsContent = args.getString("news_content");

            // Set data to views
            newsTitleView.setText(newsTitle);
            if (newsImageResId != 0) {
                newsImageView.setImageResource(newsImageResId);
            } else {
                newsImageView.setVisibility(View.GONE);
            }
            newsContentView.setText(newsContent);
        }

        return view;
    }
}
