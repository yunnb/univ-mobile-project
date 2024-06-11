package com.example.mobileproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;

public class HomeFragment extends Fragment {

    private EditText editText;
    private Button searchButton;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<Medicine> medicines;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchMedicine();

        editText = view.findViewById(R.id.edit);
        searchButton = view.findViewById(R.id.searchBtn);
        listView = view.findViewById(R.id.list_view);

        medicines = new ArrayList<>();
        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
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

        return view;
    }

    private void searchMedicine() {
        new Thread(() -> {
            String query = editText.getText().toString();
            List<Medicine> result = fetchMedicines(query);
            getActivity().runOnUiThread(() -> {
                medicines.clear();
                medicines.addAll(result);
                List<String> medicineNames = new ArrayList<>();
                for (Medicine med : result) {
                    medicineNames.add(med.getItemName());
                }
                adapter.clear();
                adapter.addAll(medicineNames);
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
                            medicine = null;
                        }
                        break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 여기서 두 번째 API 연동을 시도합니다.
        for (Medicine med : resultList) {
            //fetchAdditionalMedicineInfo(med);
        }

        return resultList;
    }

    private void fetchAdditionalMedicineInfo(Medicine medicine) {
        String serviceKey = "%2Bw%2Fg7mULHBT87Ex0nvqurAjT%2FPHyQ80zN%2BPic6VLF9JCiZJmzpdH6ezn308hRgfiRtayw9lAxh2Luw2CZpg%2F6g%3D%3D";
        String queryUrl2 = "https://apis.data.go.kr/1471000/DrugPrdtPrmsnInfoService05/getDrugPrdtPrmsnDtlInq04?serviceKey=" + serviceKey + "&item_seq=" + medicine.getItemSeq();

        try {
            URL url = new URL(queryUrl2);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            int eventType = xpp.getEventType();
            StringBuilder eeDocData = new StringBuilder();
            StringBuilder udDocData = new StringBuilder();
            StringBuilder nbDocData = new StringBuilder();
            String currentTag = "";
            String currentDocType = "";

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        currentTag = xpp.getName();
                        if (currentTag.equals("DOC")) {
                            currentDocType = xpp.getAttributeValue(null, "type");
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if ("VALID_TERM".equals(currentTag)) {
                            medicine.setValidTerm(xpp.getText());
                            Log.d("API Fetch", "Valid Term: " + xpp.getText());
                        } else if ("PARAGRAPH".equals(currentTag)) {
                            if ("EE".equals(currentDocType)) {
                                eeDocData.append(xpp.getText());
                                Log.d("API Fetch", "EE Doc Data: " + xpp.getText());
                            } else if ("UD".equals(currentDocType)) {
                                udDocData.append(xpp.getText());
                                Log.d("API Fetch", "UD Doc Data: " + xpp.getText());
                            } else if ("NB".equals(currentDocType)) {
                                nbDocData.append(xpp.getText());
                                Log.d("API Fetch", "NB Doc Data: " + xpp.getText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (xpp.getName().equals("DOC")) {
                            currentDocType = "";
                        }
                        currentTag = "";
                        break;
                }
                eventType = xpp.next();
            }
            medicine.setEeDocData(eeDocData.toString());
            medicine.setUdDocData(udDocData.toString());
            medicine.setNbDocData(nbDocData.toString());
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
