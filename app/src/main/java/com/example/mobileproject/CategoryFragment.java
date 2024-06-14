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
    private CheckBox cbCircle, cbOval, cbRectangular, cbSemiCircle, cbDiamond, cbTriangle, cbSquare, cbPentagon, cbHexagon, cbOctagon, cbOther;
    private CheckBox cbWhite, cbRed, cbYellow, cbGreen, cbBlue, cbOtherColor;
    private CheckBox cbEtc, cbOtc;
    private Button searchBtn, favoriteViewBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        cbCircle = view.findViewById(R.id.cb_circle);
        cbOval = view.findViewById(R.id.cb_oval);
        cbRectangular = view.findViewById(R.id.cb_rectangular);
        cbSemiCircle = view.findViewById(R.id.cb_semi_circle);
        cbDiamond = view.findViewById(R.id.cb_diamond);
        cbTriangle = view.findViewById(R.id.cb_triangle);
        cbSquare = view.findViewById(R.id.cb_square);
        cbPentagon = view.findViewById(R.id.cb_pentagon);
        cbHexagon = view.findViewById(R.id.cb_hexagon);
        cbOctagon = view.findViewById(R.id.cb_octagon);
        cbOther = view.findViewById(R.id.cb_other);

        cbWhite = view.findViewById(R.id.cb_white);
        cbRed = view.findViewById(R.id.cb_red);
        cbYellow = view.findViewById(R.id.cb_yellow);
        cbGreen = view.findViewById(R.id.cb_green);
        cbBlue = view.findViewById(R.id.cb_blue);
        cbOtherColor = view.findViewById(R.id.cb_other_color);

        cbEtc = view.findViewById(R.id.cb_etc);
        cbOtc = view.findViewById(R.id.cb_otc);

        searchBtn = view.findViewById(R.id.category_btn);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkCheckBox()) {
                    Toast.makeText(getContext(), "체크박스를 선택하세요", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<String> selectShape = getSelectShape();
                ArrayList<String> selectColor = getSelectColor();
                ArrayList<String> selectType = getSelectType();

                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putStringArrayListExtra("selectShape", selectShape);
                intent.putStringArrayListExtra("selectColor", selectColor);
                intent.putStringArrayListExtra("selectType", selectType);
                startActivity(intent);
            }
        });

        favoriteViewBtn = view.findViewById(R.id.favorite_view_btn);
        favoriteViewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavoriteActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private boolean checkCheckBox() { // 하나 이상은 무조건 선택해야 함
        return cbCircle.isChecked() || cbOval.isChecked() || cbRectangular.isChecked() || cbSemiCircle.isChecked() ||
                cbDiamond.isChecked() || cbTriangle.isChecked() || cbSquare.isChecked() || cbPentagon.isChecked() ||
                cbHexagon.isChecked() || cbOctagon.isChecked() || cbOther.isChecked() ||
                cbWhite.isChecked() || cbRed.isChecked() || cbYellow.isChecked() || cbGreen.isChecked() ||
                cbBlue.isChecked() || cbOtherColor.isChecked() ||
                cbEtc.isChecked() || cbOtc.isChecked();
    }

    private ArrayList<String> getSelectShape() {
        ArrayList<String> selectShape = new ArrayList<>();
        if (cbCircle.isChecked()) selectShape.add("원형");
        if (cbOval.isChecked()) selectShape.add("타원형");
        if (cbRectangular.isChecked()) selectShape.add("직사각형");
        if (cbSemiCircle.isChecked()) selectShape.add("반원형");
        if (cbDiamond.isChecked()) selectShape.add("다이아몬드형");
        if (cbTriangle.isChecked()) selectShape.add("삼각형");
        if (cbSquare.isChecked()) selectShape.add("정사각형");
        if (cbPentagon.isChecked()) selectShape.add("오각형");
        if (cbHexagon.isChecked()) selectShape.add("육각형");
        if (cbOctagon.isChecked()) selectShape.add("팔각형");
        if (cbOther.isChecked()) selectShape.add("기타");

        return selectShape;
    }

    private ArrayList<String> getSelectColor() {
        ArrayList<String> selectColor = new ArrayList<>();
        if (cbWhite.isChecked()) {
            selectColor.add("하양");
            selectColor.add("투명");
        }
        if (cbRed.isChecked()) {
            selectColor.add("분홍");
            selectColor.add("빨강");
            selectColor.add("자주");
        }
        if (cbYellow.isChecked()) {
            selectColor.add("노랑");
            selectColor.add("주황");
        }
        if (cbGreen.isChecked()) {
            selectColor.add("연두");
            selectColor.add("초록");
            selectColor.add("청록");
        }
        if (cbBlue.isChecked()) {
            selectColor.add("파랑");
            selectColor.add("남색");
            selectColor.add("보라");
        }
        if (cbOtherColor.isChecked()) {
            selectColor.add("갈색");
            selectColor.add("회색");
            selectColor.add("검정");
        }

        return selectColor;
    }

    private ArrayList<String> getSelectType() {
        ArrayList<String> selectType = new ArrayList<>();
        if (cbEtc.isChecked()) selectType.add("전문의약품");
        if (cbOtc.isChecked()) selectType.add("일반의약품");

        return selectType;
    }
}
