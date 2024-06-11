package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;  // 이 줄을 추가합니다
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private List<Medicine> medicines;
    private ListView listView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        listView = findViewById(R.id.list_view);

        ArrayList<String> medicineNames = getIntent().getStringArrayListExtra("medicineNames");
        medicines = (List<Medicine>) getIntent().getSerializableExtra("medicines");

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicineNames);
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
