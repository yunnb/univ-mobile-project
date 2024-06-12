package com.example.mobileproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

public class FavoriteManager {

    private static final String PREFS_NAME = "favorites";
    private static final String FAVORITES_KEY = "favorite_medicines";
    private static final String TAG = "FavoriteManager";

    public static void addFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favorites.add(itemSeq);
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        Medicine medicine = HomeFragment.findMedicineByItemSeq(itemSeq);
        if (medicine != null) {
            Log.d(TAG, "즐겨찾기 추가: " + medicine.getItemName());
        } else {
            Log.d(TAG, "즐겨찾기 추가: 약 정보를 찾을 수 없습니다.");
        }
    }

    public static void removeFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = new HashSet<>(prefs.getStringSet(FAVORITES_KEY, new HashSet<>()));
        favorites.remove(itemSeq);
        prefs.edit().putStringSet(FAVORITES_KEY, favorites).apply();

        Medicine medicine = HomeFragment.findMedicineByItemSeq(itemSeq);
        if (medicine != null) {
            Log.d(TAG, "즐겨찾기 제거: " + medicine.getItemName());
        } else {
            Log.d(TAG, "즐겨찾기 제거: 약 정보를 찾을 수 없습니다.");
        }
    }

    public static boolean isFavorite(Context context, String itemSeq) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(FAVORITES_KEY, new HashSet<>());
        return favorites.contains(itemSeq);
    }

    public static List<Medicine> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> favorites = prefs.getStringSet(FAVORITES_KEY, new HashSet<>());
        List<Medicine> favoriteMedicines = new ArrayList<>();

        for (String itemSeq : favorites) {
            Medicine medicine = HomeFragment.findMedicineByItemSeq(itemSeq);
            if (medicine != null) {
                favoriteMedicines.add(medicine);
            }
        }

        return favoriteMedicines;
    }
}
