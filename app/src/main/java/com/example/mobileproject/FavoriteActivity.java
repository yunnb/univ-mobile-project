package com.example.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private static final String NAME = "user_favorites";
    private static final String KEY = "favorite_medicine";

    private ListView listView;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = findViewById(R.id.list_view);
        List<Medicine> favoriteMedicine = getFavorite(this);
        if (favoriteMedicine.isEmpty()) {
            Toast.makeText(this, "즐겨찾기된 약이 없습니다", Toast.LENGTH_SHORT).show();
            finish(); // 결과가 없으면 리스트 뷰 화면을 종료
            return;
        }
        adapter = new MedicineAdapter(this, favoriteMedicine);
        listView.setAdapter(adapter);
    }

    public static void addFavorite(Context context, String itemSeq) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Set<String> favorite = new HashSet<>(sp.getStringSet(KEY, new HashSet<>()));
        favorite.add(itemSeq);
        sp.edit().putStringSet(KEY, favorite).apply();
    }

    public static void removeFavorite(Context context, String itemSeq) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Set<String> favorite = new HashSet<>(sp.getStringSet(KEY, new HashSet<>()));
        favorite.remove(itemSeq);
        sp.edit().putStringSet(KEY, favorite).apply();
    }

    public static boolean isFavorite(Context context, String itemSeq) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Set<String> favorite = sp.getStringSet(KEY, new HashSet<>());
        return favorite.contains(itemSeq);
    }

    public static List<Medicine> getFavorite(Context context) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        Set<String> favorite = sp.getStringSet(KEY, new HashSet<>());
        List<Medicine> favoriteMedicine = new ArrayList<>();

        for (String itemSeq : favorite) {
            Medicine medicine = HomeFragment.getMedicine(itemSeq);
            if (medicine != null) favoriteMedicine.add(medicine);
        }
        return favoriteMedicine;
    }
}
