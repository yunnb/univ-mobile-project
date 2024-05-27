package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.mobileproject.database.Medicine;
import com.example.mobileproject.database.MedicineDB;
import com.example.mobileproject.database.MedicineInfoDB;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MedicineDB medicineDB = null;               // 약 디비
    private MedicineInfoDB medicineInfoDB = null;   // 약 정보 디비
    private List<Medicine> medicineList = new ArrayList<>(); // 약 객체 리스트
    private ArrayAdapter<String> adapter;               // 어댑터

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        medicineDB = MedicineDB.getInstance(this);
        medicineInfoDB = MedicineInfoDB.getInstance(this);

        setList();

        Button searchBtn = (Button) findViewById(R.id.searchBtn);
        EditText searchEdit = (EditText) findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchMedicine = searchEdit.getText().toString();
                searchMedicine(searchMedicine);
            }
        });
    }

    void setList() {
        ListView listView = (ListView) findViewById(R.id.medicineList);                // 리스트 뷰
        medicineList = medicineDB.medicineDao().getAll();     // MedicineDB의 약 객체 리스트

        // 약 이름 리스트 생성
        List<String> nameList = new ArrayList<>();
        for (Medicine medicine : medicineList) { nameList.add(medicine.getName()); }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameList);
        listView.setAdapter(adapter);  // 리스트 뷰와 데이터 연결할 어댑터

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine selectedMedicine = medicineList.get(position);
                int selectedMedicineId = selectedMedicine.getId();
                String selectedMedicineName = selectedMedicine.getName();

                Toast.makeText(getApplicationContext(), "ID: " + selectedMedicineId + ", Name: " + selectedMedicineName, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, MedicineInfo.class);
                intent.putExtra("MEDICINE_ID", selectedMedicineId);
                intent.putExtra("MEDICINE_NAME", selectedMedicineName);
                startActivity(intent);
            }
        });
    }

    void searchMedicine(String searchMedicine) {
        List<String> filteredList = new ArrayList<>();
        for (Medicine medicine : medicineList) {
            if (medicine.getName().toLowerCase().contains(searchMedicine.toLowerCase())) {
                filteredList.add(medicine.getName());
            }
        }

        adapter.clear();
        adapter.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }
}
