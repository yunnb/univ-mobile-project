package com.example.mobileproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
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

        ArrayList<String> selectedShapes = getIntent().getStringArrayListExtra("selectedShapes");
        ArrayList<String> selectedColors = getIntent().getStringArrayListExtra("selectedColors");
        ArrayList<String> selectedTypes = getIntent().getStringArrayListExtra("selectedTypes");

        medicines = filterMedicines(selectedShapes, selectedColors, selectedTypes);
        if (medicines.isEmpty()) {
            Toast.makeText(this, "검색된 결과가 없습니다", Toast.LENGTH_SHORT).show();
            finish(); // 결과가 없으면 리스트뷰 화면을 종료
            return;
        }

        adapter = new MedicineAdapter(this, medicines);
        listView.setAdapter(adapter);
    }

    private List<Medicine> filterMedicines(ArrayList<String> shapes, ArrayList<String> colors, ArrayList<String> types) {
        List<Medicine> allMedicines = HomeFragment.getAllMedicines();
        List<Medicine> filteredMedicines = new ArrayList<>();

        for (Medicine medicine : allMedicines) {
            boolean matchesShape = shapes.isEmpty() || shapes.contains(medicine.getDrugShape());
            boolean matchesColor = colors.isEmpty() || matchesColor(medicine.getColorClass(), colors);
            boolean matchesType = types.isEmpty() || types.contains(medicine.getEtcOtcName());

            if (matchesShape && matchesColor && matchesType) filteredMedicines.add(medicine);
        }

        return filteredMedicines;
    }

    private boolean matchesColor(String medicineColor, List<String> selectedColors) {
        if (medicineColor == null || selectedColors.isEmpty()) return true;

        for (String color : selectedColors) {
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
