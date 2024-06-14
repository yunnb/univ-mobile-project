package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MedicineDetailActivity extends AppCompatActivity {

    private Medicine medicine;
    private Button favoriteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        medicine = (Medicine) getIntent().getSerializableExtra("medicine");

        TextView itemNameText = findViewById(R.id.itemName);
        TextView chartText = findViewById(R.id.chart);
        TextView drugShapeText = findViewById(R.id.drugShape);
        TextView colorClass1Text = findViewById(R.id.colorClass1);
        TextView classNameText = findViewById(R.id.className);
        TextView etcOtcNameText = findViewById(R.id.etcOtcName);
        TextView formCodeNameText = findViewById(R.id.formCodeName);
        favoriteBtn = findViewById(R.id.favorite_btn);

        itemNameText.setText(medicine.getItemName());
        chartText.setText(medicine.getChart());
        drugShapeText.setText(medicine.getDrugShape());
        colorClass1Text.setText(medicine.getColorClass());
        classNameText.setText(medicine.getClassName());
        etcOtcNameText.setText(medicine.getEtcOtcName());
        formCodeNameText.setText(medicine.getFormCodeName());

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { updateFavorite(); }

        });
    }

    private void updateFavorite() {
        boolean isFavorite = FavoriteActivity.isFavorite(this, medicine.getItemSeq());
        if (isFavorite) {
            FavoriteActivity.removeFavorite(this, medicine.getItemSeq());
            Toast.makeText(this, "즐겨찾기에서 제거되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            FavoriteActivity.addFavorite(this, medicine.getItemSeq());
            Toast.makeText(this, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
