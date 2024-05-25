package com.example.mobileproject;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicineInfo")
public class MedicineInfo {
    @PrimaryKey
    private int id;             // 품목 일련 번호
    private String effect;      // 효능효과
    private String capacity;    // 용법용량
    private String caution;     // 주의사항
    private String attach;      // 첨부문서
    private String storage;     // 저장방법
    private String validity;    // 유효기간

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getCaution() {
        return caution;
    }

    public void setCaution(String caution) {
        this.caution = caution;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }
}
