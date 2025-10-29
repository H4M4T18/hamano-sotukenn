package com.example.demo.model;

public class Video {
    private String grade;
    private String title;
    private String url;

    public Video(String grade, String title, String url) {
        this.grade = grade;
        this.title = title;
        this.url = url;
    }

    public String getGrade() { return grade; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
}