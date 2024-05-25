package com.example.mobileproject;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

/*
* Data Access Object
*/
@Dao
public interface MedicineDao {

    @Insert
    void setInsertMedicine(Medicine medicine);
    @Delete
    void setDeleteMedicine(Medicine medicine);

    // 조회 쿼리
    @Query("SELECT * FROM medicine")  // 데이터베이스에 요청하는 명령문
    List<Medicine> getAll();

    @Query("SELECT name FROM Medicine")
    List<String> getNameAll();


}
