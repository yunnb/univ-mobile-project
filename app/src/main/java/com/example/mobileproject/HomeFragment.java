package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class HomeFragment extends Fragment {

    private EditText editText;
    private Button searchButton;
    private ListView listView;
    private MedicineAdapter adapter;
    private List<Medicine> medicines;
    private static Map<String, Medicine> medicineMap = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editText = view.findViewById(R.id.edit);
        searchButton = view.findViewById(R.id.searchBtn);
        listView = view.findViewById(R.id.list_view);

        medicines = new ArrayList<>();
        adapter = new MedicineAdapter(getActivity(), medicines);
        listView.setAdapter(adapter);

        searchMedicine();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { searchMedicine(); }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine selectedMedicine = medicines.get(position); // 선택된 약
                Intent intent = new Intent(getActivity(), MedicineDetailActivity.class); // 상세 페이지로 이동
                intent.putExtra("medicine", selectedMedicine); // Intent에 약 정보 추가
                startActivity(intent); // 상세 페이지
            }
        });
        return view;
    }

    // 약을 검색하는 메서드
    // 시간이 오래 걸리는 작업은 메인 스레드에서 사용 시 성능 저하
    // 새 스레드 생성 후 작업
    private void searchMedicine() {
        new Thread(() -> {
            String searchWord = editText.getText().toString(); // EditText 값
            List<Medicine> result = medicineOpenAPI(searchWord); // 값에 해당하는 약 정보
            getActivity().runOnUiThread(() -> {  // 출력 작업은 메인 스레드
                medicines.clear(); // 기존 약 리스트 초기화
                medicines.addAll(result); // 검색 결과 추가
                adapter.notifyDataSetChanged(); // 어댑터에 데이터 변경
            });
        }).start();
    }
    private List<Medicine> medicineOpenAPI(String query) {
        List<Medicine> resultList = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query);
        String serviceKey = "%2Bw%2Fg7mULHBT87Ex0nvqurAjT%2FPHyQ80zN%2BPic6VLF9JCiZJmzpdH6ezn308hRgfiRtayw9lAxh2Luw2CZpg%2F6g%3D%3D";
        String queryUrl1 = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=" + serviceKey + "&numOfRows=30&pageNo=1&type=xml&item_name=" + encodedQuery;

        try { // Open API 파싱
            URL url = new URL(queryUrl1);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            int eventType = xpp.getEventType(); // 이벤트 타입
            Medicine medicine = null; // 현재 처리 중인 약 객체

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG: // 시작 태그일 경우
                        String tag = xpp.getName(); // 태그 이름 가져오기
                        if (tag.equals("item")) medicine = new Medicine();
                        else if (medicine != null) {
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
                            } else if (tag.equals("COLOR_CLASS1")) {
                                xpp.next();
                                medicine.setColorClass(xpp.getText());
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

                    case XmlPullParser.END_TAG: // 끝 태그일 경우
                        if (xpp.getName().equals("item") && medicine != null) {
                            resultList.add(medicine); // 결과 리스트에 약 객체 추가
                            medicineMap.put(medicine.getItemSeq(), medicine); // 맵에 약 객체 추가
                            medicine = null; // 현재 약 객체 초기화
                        }
                        break;
                }
                eventType = xpp.next(); // 다음 이벤트
            }
        } catch (Exception e) {e.printStackTrace();}
        return resultList;
    }

    public static List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicineMap.values());
    }
    public static Medicine getMedicine(String itemSeq) {
        return medicineMap.get(itemSeq);
    }
}
