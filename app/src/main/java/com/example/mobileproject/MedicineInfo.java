package com.example.mobileproject;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MedicineInfo extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.medicine_info);

        // 전달된 데이터 수신
        int medicineId = getIntent().getIntExtra("MEDICINE_ID", -1);
        String medicineName = getIntent().getStringExtra("MEDICINE_NAME");

        // UI 업데이트
        TextView idTextView = findViewById(R.id.medicineIdTextView);
        TextView nameTextView = findViewById(R.id.medicineNameTextView);

        idTextView.setText("ID: " + medicineId);
        nameTextView.setText("Name: " + medicineName);
    }
}
