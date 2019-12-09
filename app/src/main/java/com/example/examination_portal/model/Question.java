package com.example.examination_portal.model;

public class Question {
    int questionID;
    int questionTestID;
    String questionSentence;
    String optionA;
    String optionB;
    String optionC;
    String optionD;
    String questionAnswer;

    public String getAnswerSelected() {
        return answerSelected;
    }

    public void setAnswerSelected(String answerSelected) {
        this.answerSelected = answerSelected;
    }

    String answerSelected;

    public Question(int questionID, int questionTestID, String questionSentence, String optionA, String optionB, String optionC, String optionD, String questionAnswer) {
        this.questionID = questionID;
        this.questionTestID = questionTestID;
        this.questionSentence = questionSentence;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.questionAnswer = questionAnswer;
        this.answerSelected = "a";
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public int getQuestionTestID() {
        return questionTestID;
    }

    public void setQuestionTestID(int questionTestID) {
        this.questionTestID = questionTestID;
    }

    public String getQuestionSentence() {
        return questionSentence;
    }

    public void setQuestionSentence(String questionSentence) {
        this.questionSentence = questionSentence;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public String getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(String questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
}
