package com.example.examination_portal.model;

public class Result {
    private String testName;
    private int testScore;

    public Result() {
    }

    public Result(String testName, int testScore) {
        this.testName = testName;
        this.testScore = testScore;
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
}
