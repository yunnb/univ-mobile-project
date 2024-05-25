package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MedicineDB medicineDB = null;
    /*private MedicineInfoDB medicineInfoDB = null;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button searchBtn;
        searchBtn = (Button) findViewById(R.id.searchBtn);

        medicineDB = MedicineDB.getInstance(this);    // 약 디비
        List<Medicine> medicines = medicineDB.medicineDao().getAll();
        System.out.println("medicines = " + medicines);

    /*    medicineInfoDB = MedicineInfoDB.getInstance(this);   // 약 정보 디비
        List<MedicineInfo> medicineInfos = medicineInfoDB.medicineInfoDao().getAll();
        System.out.println("medicineInfos = " + medicineInfos);*/

        ListView mList = (ListView) findViewById(R.id.medicineList);    // 리스트 뷰
        List<String> list = new ArrayList<>();                          // 리스트 뷰에서 사용할 리스트

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        mList.setAdapter(adapter);  // 리스트 뷰와 데이터 연결할 어댑터

        /*
        list.add()*/

        /*
        List<String> m_nameList = database.medicineDao().getNameAll();  // MedicineDB의 약 이름 저장한 리스트
        // 얘 실행 시 오류남

        String[] nameArr = m_nameList.toArray(new String[m_nameList.size()]);

        for (int i = 0; i < nameArr.length; i++) {
            list.add(nameArr[i]);
        }*/
        list.add("ㅎㅇ");
/*
        List<Medicine> ml = medicineDao.getAll();*/


    }
}