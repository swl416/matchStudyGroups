package com.swl.groupMatch.models;

public class GroupId {
    private String groupName;
    private String semester;

    public GroupId(String groupName, String semester) {
        this.groupName = groupName;
        this.semester = semester;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }
}
