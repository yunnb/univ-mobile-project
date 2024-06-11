package com.example.mobileproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Medicine medicine = (Medicine) getIntent().getSerializableExtra("medicine");

        TextView itemNameTextView = findViewById(R.id.itemName);
        TextView chartTextView = findViewById(R.id.chart);
        TextView drugShapeTextView = findViewById(R.id.drugShape);
        TextView colorClass1TextView = findViewById(R.id.colorClass1);
        TextView classNameTextView = findViewById(R.id.className);
        TextView etcOtcNameTextView = findViewById(R.id.etcOtcName);
        TextView formCodeNameTextView = findViewById(R.id.formCodeName);
        TextView validTermTextView = findViewById(R.id.validTerm);
        TextView eeDocDataTextView = findViewById(R.id.eeDocData);
        TextView udDocDataTextView = findViewById(R.id.udDocData);
        TextView nbDocDataTextView = findViewById(R.id.nbDocData);

        itemNameTextView.setText(medicine.getItemName());
        chartTextView.setText(medicine.getChart());
        drugShapeTextView.setText(medicine.getDrugShape());
        colorClass1TextView.setText(medicine.getColorClass());
        classNameTextView.setText(medicine.getClassName());
        etcOtcNameTextView.setText(medicine.getEtcOtcName());
        formCodeNameTextView.setText(medicine.getFormCodeName());
        validTermTextView.setText(medicine.getValidTerm());
        eeDocDataTextView.setText(medicine.getEeDocData());
        udDocDataTextView.setText(medicine.getUdDocData());
        nbDocDataTextView.setText(medicine.getNbDocData());
    }
}
