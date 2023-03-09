package com.swl.groupMatch.documents;

import com.swl.groupMatch.models.StudentId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("students")
public class Student {
    @Id
    private StudentId studentId;

    private String studentName;
    private String pw;

    public Student(StudentId studentId, String studentName, String pw) {
        super();
        this.studentId = studentId;
        this.studentName = studentName;
        this.pw = pw;
    }

    public StudentId getStudentId() {
        return studentId;
    }

    public void setStudentId(StudentId studentId) {
        this.studentId = studentId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
