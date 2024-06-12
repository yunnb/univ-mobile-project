// AlarmFragment.java

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
    private Button selectMedicineButton;
    private TextView selectedMedicineText;
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
        selectMedicineButton = view.findViewById(R.id.select_medicine_button);
        selectedMedicineText = view.findViewById(R.id.selected_medicine_text);
        alarmListView = view.findViewById(R.id.alarm_list_view);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(getContext(), alarmList);
        alarmListView.setAdapter(alarmAdapter);

        selectMedicineButton.setOnClickListener(new View.OnClickListener() {
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
        List<Medicine> favoriteMedicines = FavoriteManager.getFavorites(getContext());
        if (favoriteMedicines.isEmpty()) {
            Toast.makeText(getContext(), "즐겨찾기된 약이 없습니다", Toast.LENGTH_SHORT).show();
            return;
        }

        String[] medicineNames = new String[favoriteMedicines.size()];
        for (int i = 0; i < favoriteMedicines.size(); i++) {
            medicineNames[i] = favoriteMedicines.get(i).getItemName();
        }

        new AlertDialog.Builder(getContext())
                .setTitle("약 선택")
                .setItems(medicineNames, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedMedicine = favoriteMedicines.get(which);
                        selectedMedicineText.setText("선택된 약: " + selectedMedicine.getItemName());
                    }
                })
                .show();
    }

    private void setAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        if (selectedMedicine != null) {
            intent.putExtra("medicine_name", selectedMedicine.getItemName());
        }
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmList.size(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getContext(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();

            Alarm alarm = new Alarm(hour, minute, true, alarmList.size(), selectedMedicine != null ? selectedMedicine.getItemName() : null);
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

            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.alarm_item, parent, false);
            }

            TextView alarmTime = convertView.findViewById(R.id.alarm_time);
            Switch alarmSwitch = convertView.findViewById(R.id.alarm_switch);
            Button deleteButton = convertView.findViewById(R.id.delete_alarm_button);

            alarmTime.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
            alarmSwitch.setChecked(alarm.isEnabled());

            if (alarm.getMedicineName() != null) {
                alarmTime.setText(String.format("%02d:%02d - %s", alarm.getHour(), alarm.getMinute(), alarm.getMedicineName()));
            } else {
                alarmTime.setText(String.format("%02d:%02d", alarm.getHour(), alarm.getMinute()));
            }

            alarmSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    alarm.setEnabled(isChecked);
                    if (isChecked) {
                        setAlarm(alarm, position);
                    } else {
                        cancelAlarm(position);
                    }
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

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            if (alarm.getMedicineName() != null) {
                intent.putExtra("medicine_name", alarm.getMedicineName());
            }
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        }

        private void cancelAlarm(int requestCode) {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

            if (alarmManager != null) {
                alarmManager.cancel(pendingIntent);
            }
        }
    }

    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String medicineName = intent.getStringExtra("medicine_name");
            if (medicineName != null) {
                Toast.makeText(context, "알람이 울립니다! 약: " + medicineName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "알람이 울립니다!", Toast.LENGTH_SHORT).show();
            }
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
