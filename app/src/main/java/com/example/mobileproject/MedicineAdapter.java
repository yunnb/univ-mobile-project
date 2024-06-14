package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MedicineAdapter extends ArrayAdapter<Medicine> {

    private List<Medicine> medicineList;
    private Context context;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        super(context, 0, medicineList);
        this.context = context;
        this.medicineList = medicineList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_list_item, parent, false);

        Medicine medicine = medicineList.get(position);
        TextView medicineText = convertView.findViewById(R.id.item_name);
        Button favoriteBtn = convertView.findViewById(R.id.favorite_btn);

        medicineText.setText(medicine.getItemName());
        updateFavoriteButton(favoriteBtn, medicine);

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(medicine);
                updateFavoriteButton(favoriteBtn, medicine);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MedicineDetailActivity.class);
                intent.putExtra("medicine", medicine);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    private void updateFavoriteButton(Button button, Medicine medicine) {
        boolean isFavorite = FavoriteActivity.isFavorite(context, medicine.getItemSeq());
        button.setText(isFavorite ? "★" : "☆");
    }

    private void toggleFavorite(Medicine medicine) {
        boolean isFavorite = FavoriteActivity.isFavorite(context, medicine.getItemSeq());
        if (isFavorite) {
            FavoriteActivity.removeFavorite(context, medicine.getItemSeq());
            Toast.makeText(context, "즐겨찾기에서 제거되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            FavoriteActivity.addFavorite(context, medicine.getItemSeq());
            Toast.makeText(context, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
