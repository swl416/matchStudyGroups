package com.swl.groupMatch.documents;

import com.swl.groupMatch.models.TakesId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document("takes")
public class Takes {
    @Id
    TakesId takesId;

    private String studentName;
    private List<Map<String, String>> courses;

    public Takes(TakesId takesId, String studentName, List<Map<String, String>> courses) {
        this.takesId = takesId;
        this.studentName = studentName;
        this.courses = courses;
    }

    public TakesId getTakesId() {
        return takesId;
    }

    public void setTakesId(TakesId takesId) {
        this.takesId = takesId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<Map<String, String>> getCourses() {
        return courses;
    }

    public void setCourses(List<Map<String, String>> courses) {
        this.courses = courses;
    }
}
