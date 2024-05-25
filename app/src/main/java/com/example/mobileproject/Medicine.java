package com.example.mobileproject;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine")  // Room에서 데이터 모델로써 활용하기 위함
public class Medicine {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private int id;             // 품목 일련 번호
    @ColumnInfo(name = "name")
    private String name;        // 품목명
    @ColumnInfo(name = "company")
    private String company;     // 회사명
    @ColumnInfo(name = "icon")
    private String icon;        // 성상
    @ColumnInfo(name = "img")
    private String img;         // 이미지
    @ColumnInfo(name = "shape")
    private String shape;       // 제형
    @ColumnInfo(name = "color")
    private String color;       // 색상
    @ColumnInfo(name = "classNum")
    private Integer classNum;       // 분류 번호
    @ColumnInfo(name = "className")
    private String className;   // 분류명
    @ColumnInfo(name = "type")
    private String type;        // 제형 코드


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getClassNum() {
        return classNum;
    }

    public void setClassNum(int classNum) {
        this.classNum = classNum;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

