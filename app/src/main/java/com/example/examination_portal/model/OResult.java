package com.example.examination_portal.model;

public class OResult {
    private String testName;
    private int testScore;
    private String userName;

    public OResult(String testName, int testScore, String userName) {
        this.testName = testName;
        this.testScore = testScore;
        this.userName = userName;
    }

    public OResult() {
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getTestScore() {
        return testScore;
    }

    public void setTestScore(int testScore) {
        this.testScore = testScore;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
