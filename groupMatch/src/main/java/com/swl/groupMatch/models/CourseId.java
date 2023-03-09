package com.swl.groupMatch.models;

public class CourseId {
    private String courseName;
    private String day;
    private String startDT;
    private String loc;

    public CourseId(String courseName, String day, String startDT, String loc) {
        this.courseName = courseName;
        this.day = day;
        this.startDT = startDT;
        this.loc = loc;
    }
}
