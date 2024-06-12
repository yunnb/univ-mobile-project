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

    private List<Medicine> medicines;
    private Context context;

    public MedicineAdapter(Context context, List<Medicine> medicines) {
        super(context, 0, medicines);
        this.context = context;
        this.medicines = medicines;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_list_item, parent, false);
        }

        Medicine medicine = medicines.get(position);

        TextView medicineName = convertView.findViewById(R.id.item_name);
        Button favoriteButton = convertView.findViewById(R.id.favorite_button);

        medicineName.setText(medicine.getItemName());
        updateFavoriteButton(favoriteButton, medicine);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFavorite(medicine);
                updateFavoriteButton(favoriteButton, medicine);
            }
        });

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("medicine", medicine);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    private void updateFavoriteButton(Button button, Medicine medicine) {
        boolean isFavorite = FavoriteManager.isFavorite(context, medicine.getItemSeq());
        button.setText(isFavorite ? "★" : "☆");
    }

    private void toggleFavorite(Medicine medicine) {
        boolean isFavorite = FavoriteManager.isFavorite(context, medicine.getItemSeq());
        if (isFavorite) {
            FavoriteManager.removeFavorite(context, medicine.getItemSeq());
            Toast.makeText(context, "즐겨찾기에서 제거되었습니다", Toast.LENGTH_SHORT).show();
        } else {
            FavoriteManager.addFavorite(context, medicine.getItemSeq());
            Toast.makeText(context, "즐겨찾기에 추가되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
