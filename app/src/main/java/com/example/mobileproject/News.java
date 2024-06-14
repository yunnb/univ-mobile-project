package com.example.mobileproject;

public class News {
    private String title;
    private int imageResId;
    private String content;

    public News(String title, String content) {
        this.title = title;
        this.imageResId = 0; // 기본 이미지 리소스 ID는 0으로 설정
        this.content = content;
    }

    public News(String title, int imageResId, String content) {
        this.title = title;
        this.imageResId = imageResId;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getContent() {
        return content;
    }
}
