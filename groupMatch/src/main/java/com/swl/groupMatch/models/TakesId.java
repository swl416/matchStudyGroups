package com.swl.groupMatch.models;

public class TakesId {
    private String email;
    private String semester;

    public TakesId(String email, String semester) {
        this.email = email;
        this.semester = semester;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}