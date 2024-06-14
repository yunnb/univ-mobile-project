package com.example.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {  // 즐겨찾기 관리
    private static final String name = "favorites";
    private static final String key = "favorite_medicines";


    public static void addFavorite(Context context, String itemSeq) {
        // SharedPreferences: 키-값으로 데이터 저장. 불러옴
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Set<String> fSet = new HashSet<>(sp.getStringSet(key, new HashSet<>())); // 중복 허용 않는 자료구조
        fSet.add(itemSeq);
        sp.edit().putStringSet(key, fSet).apply(); // 변경사항 저장
    }

    public static void removeFavorite(Context context, String itemSeq) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Set<String> fSet = new HashSet<>(sp.getStringSet(key, new HashSet<>()));
        fSet.remove(itemSeq);
        sp.edit().putStringSet(key, fSet).apply();
    }

    public static boolean isFavorite(Context context, String itemSeq) {
        SharedPreferences sp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Set<String> fSet = sp.getStringSet(key, new HashSet<>());
        return fSet.contains(itemSeq);
    }

    // 즐겨찾기에 포함된 모든 약을 가져오는 메서드
    public static List<Medicine> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(key, new HashSet<>());
        List<Medicine> favoriteMedicines = new ArrayList<>();

        // 즐겨찾기 목록에서 각 약의 정보를 가져와 리스트에 추가
        for (String itemSeq : favorites) {
            Medicine medicine = HomeFragment.getMedicine(itemSeq);
            if (medicine != null) {
                favoriteMedicines.add(medicine);
            }
        }

        return favoriteMedicines; // 즐겨찾기에 있는 모든 약을 포함한 리스트 반환
    }
}
