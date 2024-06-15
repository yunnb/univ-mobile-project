package com.example.mobileproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmFragment extends Fragment {

    private TimePicker timePicker;
    private Button setAlarmBtn, selectMedicineBtn;
    private TextView selectMedicineText;
    private ListView alarmListView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;
    private Medicine selectMedicine;

    private static final String NAME = "alarm";
    private static final String KEY = "alarm_list";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);
        timePicker = view.findViewById(R.id.time_picker);
        setAlarmBtn = view.findViewById(R.id.set_alarm_button);
        selectMedicineBtn = view.findViewById(R.id.select_medicine_button);
        selectMedicineText = view.findViewById(R.id.selected_medicine_text);
        alarmListView = view.findViewById(R.id.alarm_list_view);

        alarmList = loadAlarm(getContext());
        if (alarmList == null) alarmList = new ArrayList<>();


        alarmAdapter = new AlarmAdapter(getContext(), alarmList);
        alarmListView.setAdapter(alarmAdapter);

        selectMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMedicine();
            }
        });

        setAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        return view;
    }

    private void selectMedicine() {
        List<Medicine> favorite = FavoriteActivity.getFavorite(getContext());
        if (favorite.isEmpty()) {
            Toast.makeText(getContext(), "즐겨찾기된 약이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] medicineNames = new String[favorite.size()];
        for (int i = 0; i < favorite.size(); i++) medicineNames[i] = favorite.get(i).getItemName();

        new AlertDialog.Builder(getContext()).setTitle("약 선택").setItems(medicineNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                selectMedicine = favorite.get(which);
                selectMedicineText.setText("선택된 약: " + selectMedicine.getItemName());
            }
        }).show();
    }

    private void setAlarm() {
        if (selectMedicine == null) {
            Toast.makeText(getContext(), "약을 선택하세요", Toast.LENGTH_SHORT).show();
            return;
        }

        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        intent.putExtra("medicine_name", selectMedicine.getItemName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmList.size(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Alarm alarm = new Alarm(hour, minute, true, selectMedicine.getItemName());
            alarmList.add(alarm);
            alarmAdapter.notifyDataSetChanged();
            saveAlarm(getContext(), alarmList);
        }
    }

    private class AlarmAdapter extends ArrayAdapter<Alarm> {
        private Context context;
        private ArrayList<Alarm> alarmList;

        public AlarmAdapter(Context context, ArrayList<Alarm> alarmList) {
            super(context, 0, alarmList);
            this.context = context;
            this.alarmList = alarmList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Alarm alarm = getItem(position);
            if (convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);

            TextView time = convertView.findViewById(R.id.alarm_time);
            TextView medicine = convertView.findViewById(R.id.alarm_medicine);
            Switch alarmSwitch = convertView.findViewById(R.id.alarm_switch);
            Button deleteBtn = convertView.findViewById(R.id.delete_alarm_button);

            time.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute())); // 알람 시간 설정
            alarmSwitch.setChecked(alarm.isEnabled()); // 알람 스위치 상태 설정

            String medicineName = alarm.getMedicineName();  // 15글자 이상 출력 제어
            if (medicineName.length() > 15) medicineName = medicineName.substring(0, 15) + "...";

            if (medicineName != null) medicine.setText(medicineName);
            else medicine.setText("");

            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setEnabled(isChecked);
                    if (isChecked) onAlarm(alarm, position);
                    else offAlarm(position);
                    saveAlarm(context, alarmList); // 알람 상태 변경시 저장
                }
            });

            deleteBtn.setOnClickListener(new View.OnClickListener() {  // 알람 삭제
                @Override
                public void onClick(View v) {
                    offAlarm(position);
                    AlarmFragment.this.alarmList.remove(position);
                    notifyDataSetChanged();
                    saveAlarm(context, alarmList); // 알람 삭제시 저장
                }
            });
            return convertView;
        }

        private void onAlarm(Alarm alarm, int requestCode) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarm.getMedicineName() != null) intent.putExtra("medicine_name", alarm.getMedicineName());
            if (alarmManager != null) alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        private void offAlarm(int requestCode) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) alarmManager.cancel(pendingIntent);
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String medicineName = intent.getStringExtra("medicine_name");
            Toast.makeText(context, medicineName + " 복용할 시간 입니다!", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAlarm(Context context, List<Alarm> alarmList) {
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        StringBuilder sb = new StringBuilder();
        for (Alarm alarm : alarmList) {
            sb.append(alarm.getHour()).append(",")
                    .append(alarm.getMinute()).append(",")
                    .append(alarm.isEnabled()).append(",")
                    .append(alarm.getMedicineName()).append(";");
        }
        editor.putString(KEY, sb.toString());
        editor.apply();
    }

    private ArrayList<Alarm> loadAlarm(Context context) { // 알람 리스트 불러오기
        SharedPreferences sp = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        String savedAlarms = sp.getString(KEY, null);
        ArrayList<Alarm> alarmList = new ArrayList<>();
        if (savedAlarms != null) {
            String[] alarms = savedAlarms.split(";");
            for (String alarmString : alarms) {
                if (alarmString.isEmpty()) continue;
                String[] alarmData = alarmString.split(",");
                int hour = Integer.parseInt(alarmData[0]);
                int minute = Integer.parseInt(alarmData[1]);
                boolean isEnabled = Boolean.parseBoolean(alarmData[2]);
                String medicineName = alarmData[3];
                alarmList.add(new Alarm(hour, minute, isEnabled, medicineName));
            }
        }
        return alarmList;
    }
}
