package com.example.examination_portal.model;

public class Test {
    int testID;
    String testName;

    public Test(int testID, String testName) {
        this.testID = testID;
        this.testName = testName;
    }

    public int getTestID() {
        return testID;
    }

    public void setTestID(int testID) {
        this.testID = testID;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
