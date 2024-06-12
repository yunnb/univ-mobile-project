package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {
    private CheckBox checkboxCircle, checkboxOval, checkboxRectangular, checkboxSemiCircle, checkboxDiamond, checkboxTriangle, checkboxSquare, checkboxPentagon, checkboxHexagon, checkboxOctagon, checkboxOther;
    private CheckBox checkboxWhite, checkboxRed, checkboxYellow, checkboxGreen, checkboxBlue, checkboxOtherColor;
    private CheckBox checkboxEtc, checkboxOtc;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        checkboxCircle = view.findViewById(R.id.checkbox_circle);
        checkboxOval = view.findViewById(R.id.checkbox_oval);
        checkboxRectangular = view.findViewById(R.id.checkbox_rectangular);
        checkboxSemiCircle = view.findViewById(R.id.checkbox_semi_circle);
        checkboxDiamond = view.findViewById(R.id.checkbox_diamond);
        checkboxTriangle = view.findViewById(R.id.checkbox_triangle);
        checkboxSquare = view.findViewById(R.id.checkbox_square);
        checkboxPentagon = view.findViewById(R.id.checkbox_pentagon);
        checkboxHexagon = view.findViewById(R.id.checkbox_hexagon);
        checkboxOctagon = view.findViewById(R.id.checkbox_octagon);
        checkboxOther = view.findViewById(R.id.checkbox_other);

        checkboxWhite = view.findViewById(R.id.checkbox_white);
        checkboxRed = view.findViewById(R.id.checkbox_red);
        checkboxYellow = view.findViewById(R.id.checkbox_yellow);
        checkboxGreen = view.findViewById(R.id.checkbox_green);
        checkboxBlue = view.findViewById(R.id.checkbox_blue);
        checkboxOtherColor = view.findViewById(R.id.checkbox_other_color);

        checkboxEtc = view.findViewById(R.id.checkbox_etc);
        checkboxOtc = view.findViewById(R.id.checkbox_otc);

        searchButton = view.findViewById(R.id.categoryBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!anyCheckboxChecked()) {
                    Toast.makeText(getContext(), "체크박스를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 필터링된 약 리스트 생성
                List<Medicine> filteredMedicines = filterMedicines();
                if (filteredMedicines.isEmpty()) {
                    Toast.makeText(getContext(), "해당 조건에 맞는 약이 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 검색 결과 화면으로 이동
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                ArrayList<String> medicineNames = new ArrayList<>();
                for (Medicine med : filteredMedicines) {
                    medicineNames.add(med.getItemName());
                }
                intent.putStringArrayListExtra("medicineNames", medicineNames);
                intent.putExtra("medicines", new ArrayList<>(filteredMedicines));
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean anyCheckboxChecked() {
        return checkboxCircle.isChecked() || checkboxOval.isChecked() || checkboxRectangular.isChecked() || checkboxSemiCircle.isChecked() ||
                checkboxDiamond.isChecked() || checkboxTriangle.isChecked() || checkboxSquare.isChecked() || checkboxPentagon.isChecked() ||
                checkboxHexagon.isChecked() || checkboxOctagon.isChecked() || checkboxOther.isChecked() ||
                checkboxWhite.isChecked() || checkboxRed.isChecked() || checkboxYellow.isChecked() || checkboxGreen.isChecked() ||
                checkboxBlue.isChecked() || checkboxOtherColor.isChecked() ||
                checkboxEtc.isChecked() || checkboxOtc.isChecked();
    }

    private List<Medicine> filterMedicines() {
        List<Medicine> allMedicines = HomeFragment.getAllMedicines(); // HomeFragment에서 모든 약 정보를 가져옴
        List<Medicine> filteredMedicines = new ArrayList<>();

        for (Medicine medicine : allMedicines) {
            if (matchesFilters(medicine)) {
                filteredMedicines.add(medicine);
            }
        }

        return filteredMedicines;
    }

    private boolean matchesFilters(Medicine medicine) {
        // 여기서 medicine 객체의 필드를 체크박스 상태와 비교하여 필터링 로직을 구현
        // 예제: 모양 필터링
        boolean shapeMatches = (checkboxCircle.isChecked() && medicine.getDrugShape().equals("원형")) ||
                (checkboxOval.isChecked() && medicine.getDrugShape().equals("타원형")) ||
                (checkboxRectangular.isChecked() && medicine.getDrugShape().equals("사각형")) ||
                (checkboxSemiCircle.isChecked() && medicine.getDrugShape().equals("반원형")) ||
                (checkboxDiamond.isChecked() && medicine.getDrugShape().equals("마름모형")) ||
                (checkboxTriangle.isChecked() && medicine.getDrugShape().equals("삼각형")) ||
                (checkboxSquare.isChecked() && medicine.getDrugShape().equals("정사각형")) ||
                (checkboxPentagon.isChecked() && medicine.getDrugShape().equals("오각형")) ||
                (checkboxHexagon.isChecked() && medicine.getDrugShape().equals("육각형")) ||
                (checkboxOctagon.isChecked() && medicine.getDrugShape().equals("팔각형")) ||
                (checkboxOther.isChecked() && medicine.getDrugShape().equals("기타"));

        // 색상 필터링
        boolean colorMatches = (checkboxWhite.isChecked() && medicine.getColorClass().equals("흰색")) ||
                (checkboxRed.isChecked() && medicine.getColorClass().equals("빨강")) ||
                (checkboxYellow.isChecked() && medicine.getColorClass().equals("노랑")) ||
                (checkboxGreen.isChecked() && medicine.getColorClass().equals("초록")) ||
                (checkboxBlue.isChecked() && medicine.getColorClass().equals("파랑")) ||
                (checkboxOtherColor.isChecked() && medicine.getColorClass().equals("기타"));

        // 기타 조건 필터링
        boolean etcMatches = (checkboxEtc.isChecked() && medicine.getEtcOtcName().equals("전문의약품"));
        boolean otcMatches = (checkboxOtc.isChecked() && medicine.getEtcOtcName().equals("일반의약품"));

        return shapeMatches && colorMatches && (etcMatches || otcMatches);
    }
}
