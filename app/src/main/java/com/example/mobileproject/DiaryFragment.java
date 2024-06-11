package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class DiaryFragment extends Fragment {

    private String filename;
    private DatePicker dp;
    private Button btnWrite;
    private EditText edtDiary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);

        dp = view.findViewById(R.id.datePicker1);
        btnWrite = view.findViewById(R.id.btnWrite);
        edtDiary = view.findViewById(R.id.edtDiary);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        filename = cYear + "_" + (cMonth + 1) + "_" + cDay + ".txt";
        String str = readDiary(filename);
        edtDiary.setText(str);
        btnWrite.setEnabled(true);

        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                filename = year + "_" + (month + 1) + "_" + day + ".txt";
                String str = readDiary(filename);
                edtDiary.setText(str);
                btnWrite.setEnabled(true);
            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileOutputStream outfs = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    String str = edtDiary.getText().toString();
                    outfs.write(str.getBytes());
                    outfs.close();
                    Toast.makeText(getContext(), filename + "이 저장됨", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

    private String readDiary(String filename) {
        String diaryStr = null;
        FileInputStream infs;
        try {
            infs = getContext().openFileInput(filename);
            byte[] txt = new byte[500];
            infs.read(txt);
            infs.close();
            diaryStr = (new String(txt)).trim();
            btnWrite.setText("수정하기");
        } catch (FileNotFoundException e) {
            edtDiary.setHint("일기없음");
            btnWrite.setText("새로 저장");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return diaryStr;
    }
}
