package com.example.mobileproject;

public class Alarm {
    private int hour;
    private int minute;
    private boolean isEnabled;
    private String medicineName;

    public Alarm(int hour, int minute, boolean isEnabled, String medicineName) {
        this.hour = hour;
        this.minute = minute;
        this.isEnabled = isEnabled;
        this.medicineName = medicineName;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public String getMedicineName() {
        return medicineName;
    }
}
