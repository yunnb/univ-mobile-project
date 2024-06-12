package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;  // 이 줄을 추가합니다
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private List<Medicine> medicines;
    private ListView listView;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        listView = findViewById(R.id.list_view);

        ArrayList<String> medicineNames = getIntent().getStringArrayListExtra("medicineNames");
        medicines = (List<Medicine>) getIntent().getSerializableExtra("medicines");

        adapter = new MedicineAdapter(this, medicines);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine selectedMedicine = medicines.get(position);
                Intent intent = new Intent(SearchResultsActivity.this, DetailActivity.class);
                intent.putExtra("medicine", selectedMedicine);
                startActivity(intent);
            }
        });
    }
}
