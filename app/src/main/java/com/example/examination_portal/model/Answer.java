package com.example.examination_portal.model;

public class Answer {
    private  boolean answer = false;;

    public Answer() {
    }

    public Answer(boolean answer) {
        this.answer = answer;
    }

    public boolean isAnswer() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
