package com.example.mobileproject;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MedicineInfo.class}, version = 101)
public abstract class MedicineInfoDB extends RoomDatabase {
    private static MedicineInfoDB INSTANCE = null;  // 객체는 하나만 생성
    public abstract MedicineInfoDao medicineInfoDao();

    public static MedicineInfoDB getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, MedicineInfoDB.class , "medicineInfo.db")
                    .fallbackToDestructiveMigration()   // 스키마(DB) 버전 변경 가능
                    .allowMainThreadQueries()           // Main Thread에서 IO(입출력) 가능하게 함
                    .build();
        }
        return  INSTANCE;
    }
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
