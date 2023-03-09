package com.swl.groupMatch.documents;

import com.swl.groupMatch.models.CourseId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("courses")
public class Course {
    @Id
    CourseId courseId;

    public Course(CourseId courseId) {
        this.courseId = courseId;
    }

    public CourseId getCourseId() {
        return courseId;
    }

    public void setCourseId(CourseId courseId) {
        this.courseId = courseId;
    }
}
