package com.example.mobileproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
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
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Calendar;

public class MyFragment extends Fragment {

    private TimePicker timePicker;
    private Button setAlarmButton;
    private ListView alarmListView;
    private ArrayList<Alarm> alarmList;
    private AlarmAdapter alarmAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        timePicker = view.findViewById(R.id.time_picker);
        setAlarmButton = view.findViewById(R.id.set_alarm_button);
        alarmListView = view.findViewById(R.id.alarm_list_view);

        alarmList = new ArrayList<>();
        alarmAdapter = new AlarmAdapter(getContext(), alarmList);
        alarmListView.setAdapter(alarmAdapter);

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlarm();
            }
        });

        return view;
    }

    private void setAlarm() {
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(), alarmList.size(), intent, PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(getContext(), "알람이 설정되었습니다", Toast.LENGTH_SHORT).show();

            Alarm alarm = new Alarm(hour, minute, true, alarmList.size());
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
            Toast.makeText(context, "알람이 울립니다!", Toast.LENGTH_SHORT).show();
        }
    }

    private class Alarm {
        private int hour;
        private int minute;
        private boolean isEnabled;
        private int requestCode;

        public Alarm(int hour, int minute, boolean isEnabled, int requestCode) {
            this.hour = hour;
            this.minute = minute;
            this.isEnabled = isEnabled;
            this.requestCode = requestCode;
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
    }
}
