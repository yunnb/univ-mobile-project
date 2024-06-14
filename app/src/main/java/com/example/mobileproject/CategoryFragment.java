package com.example.mobileproject;

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

public class CategoryFragment extends Fragment {
    private CheckBox checkboxCircle, checkboxOval, checkboxRectangular, checkboxSemiCircle, checkboxDiamond, checkboxTriangle, checkboxSquare, checkboxPentagon, checkboxHexagon, checkboxOctagon, checkboxOther;
    private CheckBox checkboxWhite, checkboxRed, checkboxYellow, checkboxGreen, checkboxBlue, checkboxOtherColor;
    private CheckBox checkboxEtc, checkboxOtc;
    private Button searchButton, viewFavoritesButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // 체크박스 초기화
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

        // 검색 버튼 초기화 및 클릭 이벤트 설정
        searchButton = view.findViewById(R.id.categoryBtn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!anyCheckboxChecked()) {
                    Toast.makeText(getContext(), "체크박스를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                ArrayList<String> selectedShapes = getSelectedShapes();
                ArrayList<String> selectedColors = getSelectedColors();
                ArrayList<String> selectedTypes = getSelectedTypes();

                if (selectedShapes.isEmpty() && selectedColors.isEmpty() && selectedTypes.isEmpty()) {
                    Toast.makeText(getContext(), "선택된 필터가 없습니다", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putStringArrayListExtra("selectedShapes", selectedShapes);
                intent.putStringArrayListExtra("selectedColors", selectedColors);
                intent.putStringArrayListExtra("selectedTypes", selectedTypes);
                startActivity(intent);
            }
        });

        // 즐겨찾기 보기 버튼 초기화 및 클릭 이벤트 설정
        viewFavoritesButton = view.findViewById(R.id.view_favorites_button);
        viewFavoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    // 하나 이상의 체크박스가 선택되었는지 확인하는 메서드
    private boolean anyCheckboxChecked() {
        return checkboxCircle.isChecked() || checkboxOval.isChecked() || checkboxRectangular.isChecked() || checkboxSemiCircle.isChecked() ||
                checkboxDiamond.isChecked() || checkboxTriangle.isChecked() || checkboxSquare.isChecked() || checkboxPentagon.isChecked() ||
                checkboxHexagon.isChecked() || checkboxOctagon.isChecked() || checkboxOther.isChecked() ||
                checkboxWhite.isChecked() || checkboxRed.isChecked() || checkboxYellow.isChecked() || checkboxGreen.isChecked() ||
                checkboxBlue.isChecked() || checkboxOtherColor.isChecked() ||
                checkboxEtc.isChecked() || checkboxOtc.isChecked();
    }

    // 선택된 모양 체크박스 값을 가져오는 메서드
    private ArrayList<String> getSelectedShapes() {
        ArrayList<String> selectedShapes = new ArrayList<>();
        if (checkboxCircle.isChecked()) selectedShapes.add("원형");
        if (checkboxOval.isChecked()) selectedShapes.add("타원형");
        if (checkboxRectangular.isChecked()) selectedShapes.add("직사각형");
        if (checkboxSemiCircle.isChecked()) selectedShapes.add("반원형");
        if (checkboxDiamond.isChecked()) selectedShapes.add("다이아몬드형");
        if (checkboxTriangle.isChecked()) selectedShapes.add("삼각형");
        if (checkboxSquare.isChecked()) selectedShapes.add("정사각형");
        if (checkboxPentagon.isChecked()) selectedShapes.add("오각형");
        if (checkboxHexagon.isChecked()) selectedShapes.add("육각형");
        if (checkboxOctagon.isChecked()) selectedShapes.add("팔각형");
        if (checkboxOther.isChecked()) selectedShapes.add("기타");

        return selectedShapes;
    }

    // 선택된 색상 체크박스 값을 가져오는 메서드
    private ArrayList<String> getSelectedColors() {
        ArrayList<String> selectedColors = new ArrayList<>();
        if (checkboxWhite.isChecked()) {
            selectedColors.add("하양");
            selectedColors.add("투명");
        }
        if (checkboxRed.isChecked()) {
            selectedColors.add("분홍");
            selectedColors.add("빨강");
            selectedColors.add("자주");
        }
        if (checkboxYellow.isChecked()) {
            selectedColors.add("노랑");
            selectedColors.add("주황");
        }
        if (checkboxGreen.isChecked()) {
            selectedColors.add("연두");
            selectedColors.add("초록");
            selectedColors.add("청록");
        }
        if (checkboxBlue.isChecked()) {
            selectedColors.add("파랑");
            selectedColors.add("남색");
            selectedColors.add("보라");
        }
        if (checkboxOtherColor.isChecked()) {
            selectedColors.add("갈색");
            selectedColors.add("회색");
            selectedColors.add("검정");
        }

        return selectedColors;
    }

    // 선택된 타입 체크박스 값을 가져오는 메서드
    private ArrayList<String> getSelectedTypes() {
        ArrayList<String> selectedTypes = new ArrayList<>();
        if (checkboxEtc.isChecked()) selectedTypes.add("전문의약품");
        if (checkboxOtc.isChecked()) selectedTypes.add("일반의약품");

        return selectedTypes;
    }
}
