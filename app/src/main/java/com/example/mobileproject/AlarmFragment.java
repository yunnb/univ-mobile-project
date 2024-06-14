package com.example.mobileproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
    private Button setAlarmButton;
    private Button selectMedicineBtn;
    private TextView selectMedicineText;
    private ListView alarmListView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;
    private Medicine selectedMedicine;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarm, container, false);

        timePicker = view.findViewById(R.id.time_picker);
        setAlarmButton = view.findViewById(R.id.set_alarm_button);
        selectMedicineBtn = view.findViewById(R.id.select_medicine_button);
        selectMedicineText = view.findViewById(R.id.selected_medicine_text);
        alarmListView = view.findViewById(R.id.alarm_list_view);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(getContext(), alarmList);
        alarmListView.setAdapter(alarmAdapter);

        selectMedicineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMedicineSelectionDialog();
            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        return view;
    }

    private void showMedicineSelectionDialog() {
        List<Medicine> favoriteMedicines = FavoriteActivity.getFavorites(getContext());
        if (favoriteMedicines.isEmpty()) {
            Toast.makeText(getContext(), "즐겨찾기된 약이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] medicineNames = new String[favoriteMedicines.size()];
        for (int i = 0; i < favoriteMedicines.size(); i++)
            medicineNames[i] = favoriteMedicines.get(i).getItemName();

        new AlertDialog.Builder(getContext())
                .setTitle("약 선택")
                .setItems(medicineNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedMedicine = favoriteMedicines.get(which);
                        selectMedicineText.setText("선택된 약: " + selectedMedicine.getItemName());
                    }
                })
                .show();
    }

    private void setAlarm() {
        if (selectedMedicine == null) {
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
        intent.putExtra("medicine_name", selectedMedicine.getItemName());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmList.size(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Alarm alarm = new Alarm(hour, minute, true, alarmList.size(), selectedMedicine.getItemName());
            alarmList.add(alarm);
            alarmAdapter.notifyDataSetChanged();
        }
    }

    private class AlarmAdapter extends ArrayAdapter<Alarm> {
        private Context context;
        private ArrayList<Alarm> alarms;

        public AlarmAdapter(Context context, ArrayList<Alarm> alarms) {
            super(context, 0, alarms);
            this.context = context;
            this.alarms = alarms;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Alarm alarm = getItem(position);
            if (convertView == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);

            TextView alarmTime = convertView.findViewById(R.id.alarm_time);
            TextView alarmMedicine = convertView.findViewById(R.id.alarm_medicine);
            Switch alarmSwitch = convertView.findViewById(R.id.alarm_switch);
            Button deleteButton = convertView.findViewById(R.id.delete_alarm_button);

            // 알람 시간 설정
            alarmTime.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
            // 알람 스위치 상태 설정
            alarmSwitch.setChecked(alarm.isEnabled());

            String medicineName = alarm.getMedicineName();
            if (medicineName != null && medicineName.length() > 15)
                medicineName = medicineName.substring(0, 15) + "...";

            alarmMedicine.setText(medicineName != null ? medicineName : "");

            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setEnabled(isChecked);
                    if (isChecked) setAlarm(alarm, position);
                    else cancelAlarm(position);
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelAlarm(position);
                    alarmList.remove(position);
                    notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private void setAlarm(Alarm alarm, int requestCode) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
            calendar.set(Calendar.MINUTE, alarm.getMinute());
            calendar.set(Calendar.SECOND, 0);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarm.getMedicineName() != null)
                intent.putExtra("medicine_name", alarm.getMedicineName());
            if (alarmManager != null)
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }

        private void cancelAlarm(int requestCode) {
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

    private class Alarm {
        private int hour;
        private int minute;
        private boolean isEnabled;
        private int requestCode;
        private String medicineName;

        public Alarm(int hour, int minute, boolean isEnabled, int requestCode, String medicineName) {
            this.hour = hour;
            this.minute = minute;
            this.isEnabled = isEnabled;
            this.requestCode = requestCode;
            this.medicineName = medicineName;
        }

        public int getHour() {
            return hour;
        }

        public void setHour(int hour) {
            this.hour = hour;
        }

        public int getMinute() {
            return minute;
        }

        public void setMinute(int minute) {
            this.minute = minute;
        }

        public boolean isEnabled() {
            return isEnabled;
        }

        public void setEnabled(boolean enabled) {
            isEnabled = enabled;
        }

        public int getRequestCode() {
            return requestCode;
        }

        public void setRequestCode(int requestCode) {
            this.requestCode = requestCode;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }
    }
}
