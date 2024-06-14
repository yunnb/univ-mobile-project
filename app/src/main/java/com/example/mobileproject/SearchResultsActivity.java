package com.example.mobileproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity {

    private List<Medicine> medicineList;
    private ListView listView;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        listView = findViewById(R.id.list_view);

        ArrayList<String> selectShape = getIntent().getStringArrayListExtra("selectShape");
        ArrayList<String> selectColor = getIntent().getStringArrayListExtra("selectColor");
        ArrayList<String> selectType = getIntent().getStringArrayListExtra("selectType");

        medicineList = medicineFilter(selectShape, selectColor, selectType);
        if (medicineList.isEmpty()) {
            Toast.makeText(this, "검색된 결과가 없습니다", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new MedicineAdapter(this, medicineList);
        listView.setAdapter(adapter);
    }

    private List<Medicine> medicineFilter(ArrayList<String> shape, ArrayList<String> color, ArrayList<String> type) {
        List<Medicine> allMedicine = HomeFragment.getAllMedicines();
        List<Medicine> finishFilterMedicine = new ArrayList<>();

        for (Medicine medicine : allMedicine) {
            boolean matchesShape = shape.isEmpty() || shape.contains(medicine.getDrugShape());
            boolean matchesColor = color.isEmpty() || matchesColor(medicine.getColorClass(), color);
            boolean matchesType = type.isEmpty() || type.contains(medicine.getEtcOtcName());

            if (matchesShape && matchesColor && matchesType) finishFilterMedicine.add(medicine);
        }

        return finishFilterMedicine;
    }

    private boolean matchesColor(String medicineColor, List<String> selectColor) {
        if (medicineColor == null || selectColor.isEmpty()) return true;

        for (String color : selectColor) {
            switch (color) {
                case "하양":
                    if (medicineColor.contains("하양") || medicineColor.contains("투명")) return true;
                    break;
                case "빨강":
                    if (medicineColor.contains("분홍") || medicineColor.contains("빨강") || medicineColor.contains("자주")) return true;
                    break;
                case "노랑":
                    if (medicineColor.contains("노랑") || medicineColor.contains("주황")) return true;
                    break;
                case "초록":
                    if (medicineColor.contains("연두") || medicineColor.contains("초록") || medicineColor.contains("청록")) return true;
                    break;
                case "파랑":
                    if (medicineColor.contains("파랑") || medicineColor.contains("남색") || medicineColor.contains("보라")) return true;
                    break;
                case "기타":
                    if (medicineColor.contains("갈색") || medicineColor.contains("회색") || medicineColor.contains("검정")) return true;
                    break;
            }
        }
        return false;
    }
}
