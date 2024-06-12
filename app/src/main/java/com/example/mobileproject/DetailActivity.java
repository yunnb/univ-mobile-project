package com.example.mobileproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    private Medicine medicine;
    private Button favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        medicine = (Medicine) getIntent().getSerializableExtra("medicine");

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
        favoriteButton = findViewById(R.id.favorite_button);

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

        updateFavoriteButton();

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite();
                updateFavoriteButton();
            }
        });
    }

    private void updateFavoriteButton() {
        boolean isFavorite = FavoriteManager.isFavorite(this, medicine.getItemSeq());
        favoriteButton.setText(isFavorite ? "★" : "☆");
    }

    private void toggleFavorite() {
        boolean isFavorite = FavoriteManager.isFavorite(this, medicine.getItemSeq());
        if (isFavorite) {
            FavoriteManager.removeFavorite(this, medicine.getItemSeq());
            Toast.makeText(this, "즐겨찾기에서 제거되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            FavoriteManager.addFavorite(this, medicine.getItemSeq());
            Toast.makeText(this, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
