package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {

    private EditText editText;
    private Button searchButton;
    private ListView listView;
    private MedicineAdapter adapter;
    private List<Medicine> medicines;
    private static Map<String, Medicine> medicineMap = new HashMap<>(); // 추가된 부분

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMedicine();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Medicine selectedMedicine = medicines.get(position);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("medicine", selectedMedicine);
                startActivity(intent);
            }
        });

        searchMedicine();

        return view;
    }

    private void searchMedicine() {
        new Thread(() -> {
            String query = editText.getText().toString();
            List<Medicine> result = fetchMedicines(query);
            getActivity().runOnUiThread(() -> {
                medicines.clear();
                medicines.addAll(result);
                adapter.notifyDataSetChanged();
            });
        }).start();
    }

    private List<Medicine> fetchMedicines(String query) {
        List<Medicine> resultList = new ArrayList<>();
        String encodedQuery = URLEncoder.encode(query);
        String serviceKey = "%2Bw%2Fg7mULHBT87Ex0nvqurAjT%2FPHyQ80zN%2BPic6VLF9JCiZJmzpdH6ezn308hRgfiRtayw9lAxh2Luw2CZpg%2F6g%3D%3D";
        String queryUrl1 = "https://apis.data.go.kr/1471000/MdcinGrnIdntfcInfoService01/getMdcinGrnIdntfcInfoList01?serviceKey=" + serviceKey + "&numOfRows=40&pageNo=1&type=xml&item_name=" + encodedQuery;

        try {
            URL url = new URL(queryUrl1);
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

                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("item") && medicine != null) {
                            resultList.add(medicine);
                            medicineMap.put(medicine.getItemSeq(), medicine); // 추가된 부분
                            medicine = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public static List<Medicine> getAllMedicines() {
        return new ArrayList<>(medicineMap.values());
    }

    public static Medicine findMedicineByItemSeq(String itemSeq) {
        return medicineMap.get(itemSeq);
    }
}
