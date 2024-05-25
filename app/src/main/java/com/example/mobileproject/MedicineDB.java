package com.example.mobileproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Medicine.class}, version = 100)
public abstract class MedicineDB extends RoomDatabase {
    private static MedicineDB INSTANCE; // 객체는 하나만 생성
    public abstract MedicineDao medicineDao();

    // DB 객체 반환
    public static MedicineDB getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, MedicineDB.class , "medicine.db")
                    .fallbackToDestructiveMigration()   // 스키마(DB) 버전 변경 가능
                    .allowMainThreadQueries()           // Main Thread에서 IO(입출력) 가능하게 함
                    .build();
        }
        return  INSTANCE;
    }

    // DB 객체 삭제
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
