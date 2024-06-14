package com.example.mobileproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
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
    private Button writeBtn;
    private EditText diaryEdit;
    private RatingBar ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_diary, container, false);

        dp = v.findViewById(R.id.datePicker);
        writeBtn = v.findViewById(R.id.writeBtn);
        diaryEdit = v.findViewById(R.id.diaryEdit);
        ratingBar = v.findViewById(R.id.ratingBar);

        Calendar cal = Calendar.getInstance();
        int cYear = cal.get(Calendar.YEAR);
        int cMonth = cal.get(Calendar.MONTH);
        int cDay = cal.get(Calendar.DAY_OF_MONTH);

        filename = cYear + "_" + (cMonth + 1) + "_" + cDay + ".txt";
        String[] diaryContent = readDiary(filename);
        diaryEdit.setText(diaryContent[0]);
        if (diaryContent[1] != null) ratingBar.setRating(Float.parseFloat(diaryContent[1]));

        writeBtn.setEnabled(true);
        dp.init(cYear, cMonth, cDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                filename = year + "_" + (month + 1) + "_" + day + ".txt";
                String[] str = readDiary(filename);
                diaryEdit.setText(str[0]);
                if (str[1] != null) ratingBar.setRating(Float.parseFloat(str[1]));
                else ratingBar.setRating(0);

                writeBtn.setEnabled(true);
            }
        });

        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (diaryEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "일기를 작성해 주세요!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (ratingBar.getRating() == 0) Toast.makeText(getContext(), "오늘의 기분을 별점으로 남겨주세요!", Toast.LENGTH_SHORT).show();

                try {
                    FileOutputStream fos = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    String str = diaryEdit.getText().toString() + "\n" + ratingBar.getRating();
                    fos.write(str.getBytes());
                    fos.close();
                    Toast.makeText(getContext(), "일기가 저장되었습니다!", Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return v;
    }

    private String[] readDiary(String filename) {
        String[] diaryStr = new String[2];
        FileInputStream infs;
        try {
            infs = getContext().openFileInput(filename);
            byte[] txt = new byte[500];
            int len = infs.read(txt);
            infs.close();
            String content = (new String(txt, 0, len)).trim();

            int sIdx = content.lastIndexOf("\n");
            if (sIdx != -1) {
                diaryStr[0] = content.substring(0, sIdx);
                diaryStr[1] = content.substring(sIdx + 1);
            } else diaryStr[0] = content;

            writeBtn.setText("수정하기");
        } catch (FileNotFoundException e) {
            diaryEdit.setHint("일기를 작성해 보세요!");
            writeBtn.setText("저장하기");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diaryStr;
    }
}
