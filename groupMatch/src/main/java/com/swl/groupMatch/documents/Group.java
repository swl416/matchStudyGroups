package com.swl.groupMatch.documents;

import com.swl.groupMatch.models.GroupId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.List;

@Document("groups")
public class Group {
    @Id
    GroupId groupId;

    private List<HashMap<String, Object>> matchingCourses;
    private List<Student> students;

    public Group(GroupId groupId, List<HashMap<String, Object>> matchingCourses, List<Student> students) {
        this.groupId = groupId;
        this.matchingCourses = matchingCourses;
        this.students = students;
    }

    public GroupId getGroupId() {
        return groupId;
    }

    public void setGroupId(GroupId groupId) {
        this.groupId = groupId;
    }

    public List<HashMap<String, Object>> getMatchingCourses() {
        return matchingCourses;
    }

    public void setMatchingCourses(List<HashMap<String, Object>> matchingCourses) {
        this.matchingCourses = matchingCourses;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
