package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    private List<Medicine> medicines;
    private CheckBox checkboxEtc;
    private CheckBox checkboxOtc;
    private CheckBox checkboxCircle;
    private CheckBox checkboxOval;
    private CheckBox checkboxRectangular;
    private CheckBox checkboxSemiCircle;
    private CheckBox checkboxDiamond;
    private CheckBox checkboxTriangle;
    private CheckBox checkboxSquare;
    private CheckBox checkboxPentagon;
    private CheckBox checkboxHexagon;
    private CheckBox checkboxOctagon;
    private CheckBox checkboxOther;
    private CheckBox checkboxWhite;
    private CheckBox checkboxRed;
    private CheckBox checkboxYellow;
    private CheckBox checkboxGreen;
    private CheckBox checkboxBlue;
    private CheckBox checkboxOtherColor;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        // Initialize checkboxes
        checkboxEtc = view.findViewById(R.id.checkbox_etc);
        checkboxOtc = view.findViewById(R.id.checkbox_otc);
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
        searchButton = view.findViewById(R.id.categoryBtn);

        // Initialize the medicine list
        medicines = new ArrayList<>();

        // Set up search button click listener
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicine();
            }
        });

        return view;
    }

    private void searchMedicine() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Medicine> result = fetchMedicines();
                ArrayList<String> medicineNames = new ArrayList<>();
                for (Medicine med : result) {
                    medicineNames.add(med.getItemName());
                }

                Log.d("CategoryFragment", "Medicine Names: " + medicineNames);

                // Start the SearchResultsActivity with the filtered medicine names and medicine objects
                Intent intent = new Intent(getActivity(), SearchResultsActivity.class);
                intent.putStringArrayListExtra("medicineNames", medicineNames);
                intent.putExtra("medicines", new ArrayList<>(result)); // Pass the medicine objects
                startActivity(intent);
            }
        }).start();
    }

    private List<Medicine> fetchMedicines() {
        List<Medicine> resultList = new ArrayList<>();
        try {
            String serviceKey = "%2Bw%2Fg7mULHBT87Ex0nvqurAjT%2FPHyQ80zN%2BPic6VLF9JCiZJmzpdH6ezn308hRgfiRtayw9lAxh2Luw2CZpg%2F6g%3D%3D";
            String queryUrl = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=" + serviceKey + "&numOfRows=40&pageNo=1&type=xml";

            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            int eventType = xpp.getEventType();
            Medicine medicine = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        String tag = xpp.getName();
                        if (tag.equals("item")) {
                            medicine = new Medicine();
                        } else if (medicine != null) {
                            if (tag.equals("ITEM_SEQ")) {
                                xpp.next();
                                medicine.setItemSeq(xpp.getText());
                            } else if (tag.equals("ITEM_NAME")) {
                                xpp.next();
                                medicine.setItemName(xpp.getText());
                            } else if (tag.equals("CHART")) {
                                xpp.next();
                                medicine.setChart(xpp.getText());
                            } else if (tag.equals("DRUG_SHAPE")) {
                                xpp.next();
                                medicine.setDrugShape(xpp.getText());
                                Log.d("CategoryFragment", "Drug Shape: " + xpp.getText());
                            } else if (tag.equals("COLOR_CLASS1")) {
                                xpp.next();
                                medicine.setColorClass(xpp.getText());
                                Log.d("CategoryFragment", "Color Class: " + xpp.getText());
                            } else if (tag.equals("CLASS_NAME")) {
                                xpp.next();
                                medicine.setClassName(xpp.getText());
                            } else if (tag.equals("ETC_OTC_NAME")) {
                                xpp.next();
                                medicine.setEtcOtcName(xpp.getText());
                            } else if (tag.equals("FORM_CODE_NAME")) {
                                xpp.next();
                                medicine.setFormCodeName(xpp.getText());
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("item") && medicine != null) {
                            resultList.add(medicine);
                            medicine = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Filter results based on selected checkboxes
        resultList = filterMedicines(resultList);

        return resultList;
    }

    private List<Medicine> filterMedicines(List<Medicine> medicines) {
        List<Medicine> filteredList = new ArrayList<>();

        for (Medicine medicine : medicines) {
            boolean matchesType = checkboxEtc.isChecked() && "전문의약품".equals(medicine.getEtcOtcName()) ||
                    checkboxOtc.isChecked() && "일반의약품".equals(medicine.getEtcOtcName());

            boolean matchesShape = checkboxCircle.isChecked() && "원형".equals(medicine.getDrugShape()) ||
                    checkboxOval.isChecked() && "타원형".equals(medicine.getDrugShape()) ||
                    checkboxRectangular.isChecked() && "장방형".equals(medicine.getDrugShape()) ||
                    checkboxSemiCircle.isChecked() && "반원형".equals(medicine.getDrugShape()) ||
                    checkboxDiamond.isChecked() && "마름모".equals(medicine.getDrugShape()) ||
                    checkboxTriangle.isChecked() && "삼각형".equals(medicine.getDrugShape()) ||
                    checkboxSquare.isChecked() && "사각형".equals(medicine.getDrugShape()) ||
                    checkboxPentagon.isChecked() && "오각형".equals(medicine.getDrugShape()) ||
                    checkboxHexagon.isChecked() && "육각형".equals(medicine.getDrugShape()) ||
                    checkboxOctagon.isChecked() && "팔각형".equals(medicine.getDrugShape()) ||
                    checkboxOther.isChecked() && "기타".equals(medicine.getDrugShape());

            boolean matchesColor = checkboxWhite.isChecked() && isWhiteColor(medicine.getColorClass()) ||
                    checkboxRed.isChecked() && isRedColor(medicine.getColorClass()) ||
                    checkboxYellow.isChecked() && isYellowColor(medicine.getColorClass()) ||
                    checkboxGreen.isChecked() && isGreenColor(medicine.getColorClass()) ||
                    checkboxBlue.isChecked() && isBlueColor(medicine.getColorClass()) ||
                    checkboxOtherColor.isChecked() && isOtherColor(medicine.getColorClass());

            Log.d("CategoryFragment", "DrugShape: " + medicine.getDrugShape() + ", ColorClass: " + medicine.getColorClass());
            Log.d("CategoryFragment", "matchesType: " + matchesType + ", matchesShape: " + matchesShape + ", matchesColor: " + matchesColor);

            if (matchesType || matchesShape || matchesColor) {
                filteredList.add(medicine);
            }
        }

        Log.d("CategoryFragment", "Filtered Medicines: " + filteredList);

        return filteredList;
    }

    private boolean isWhiteColor(String colorClass) {
        return "하양".equals(colorClass) || "투명".equals(colorClass);
    }

    private boolean isRedColor(String colorClass) {
        return "분홍".equals(colorClass) || "빨강".equals(colorClass) || "자주".equals(colorClass);
    }

    private boolean isYellowColor(String colorClass) {
        return "노랑".equals(colorClass) || "주황".equals(colorClass);
    }

    private boolean isGreenColor(String colorClass) {
        return "연두".equals(colorClass) || "초록".equals(colorClass) || "청록".equals(colorClass);
    }

    private boolean isBlueColor(String colorClass) {
        return "파랑".equals(colorClass) || "남색".equals(colorClass) || "보라".equals(colorClass);
    }

    private boolean isOtherColor(String colorClass) {
        return "갈색".equals(colorClass) || "회색".equals(colorClass) || "검정".equals(colorClass);
    }
}
