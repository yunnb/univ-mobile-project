package com.example.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "favorites";
    private static final String FAVORITES_KEY = "favorite_medicines";
    private static final String TAG = "FavoriteManager";

    private ListView listView;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.list_view);

        List<Medicine> favoriteMedicines = getFavorites(this);
        if (favoriteMedicines.isEmpty()) {
            Toast.makeText(this, "즐겨찾기된 약이 없습니다", Toast.LENGTH_SHORT).show();
            finish(); // 결과가 없으면 리스트뷰 화면을 종료
            return;
        }

        adapter = new MedicineAdapter(this, favoriteMedicines);
        listView.setAdapter(adapter);
    }

    // 즐겨찾기 추가 메서드
    public static void addFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favorites.add(itemSeq);
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        Medicine medicine = HomeFragment.getMedicine(itemSeq);
        if (medicine != null) {
            Log.d(TAG, "즐겨찾기 추가: " + medicine.getItemName());
        } else {
            Log.d(TAG, "즐겨찾기 추가: 약 정보를 찾을 수 없습니다.");
        }
    }

    // 즐겨찾기 제거 메서드
    public static void removeFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favorites.remove(itemSeq);
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        Medicine medicine = HomeFragment.getMedicine(itemSeq);
        if (medicine != null) {
            Log.d(TAG, "즐겨찾기 제거: " + medicine.getItemName());
        } else {
            Log.d(TAG, "즐겨찾기 제거: 약 정보를 찾을 수 없습니다.");
        }
    }

    // 즐겨찾기 여부 확인 메서드
    public static boolean isFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(FAVORITES_KEY, new HashSet<>());
        return favorites.contains(itemSeq);
    }

    // 즐겨찾기 목록 가져오기 메서드
    public static List<Medicine> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(FAVORITES_KEY, new HashSet<>());
        List<Medicine> favoriteMedicines = new ArrayList<>();

        for (String itemSeq : favorites) {
            Medicine medicine = HomeFragment.getMedicine(itemSeq);
            if (medicine != null) {
                favoriteMedicines.add(medicine);
            }
        }

        return favoriteMedicines;
    }
}
