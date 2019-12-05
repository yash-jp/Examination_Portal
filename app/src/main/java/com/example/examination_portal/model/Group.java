package com.example.examination_portal.model;

public class Group {
    private int groupID;
    private String groupName;


    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public Group(int groupID, String groupName) {
        this.groupID = groupID;
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
