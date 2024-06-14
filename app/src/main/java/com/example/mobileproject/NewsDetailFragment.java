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

        ImageView newsImageView = view.findViewById(R.id.news_detail_img);
        TextView newsTitleView = view.findViewById(R.id.news_detail_title);
        TextView newsContentView = view.findViewById(R.id.news_detail_content);

        Bundle args = getArguments();
        if (args != null) {
            String title = args.getString("news_title");
            int img = args.getInt("news_image");
            String content = args.getString("news_content");

            newsTitleView.setText(title);
            if (img != 0) newsImageView.setImageResource(img);
            else newsImageView.setVisibility(View.GONE);

            newsContentView.setText(content);
        }

        return view;
    }
}
