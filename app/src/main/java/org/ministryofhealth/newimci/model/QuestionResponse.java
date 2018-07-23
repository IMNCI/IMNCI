package org.ministryofhealth.newimci.model;

import java.util.List;

public class QuestionResponse {
    private int question_id;
    private List<Integer> question_response;

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public List<Integer> getQuestion_response() {
        return question_response;
    }

    public void setQuestion_response(List<Integer> question_response) {
        this.question_response = question_response;
    }
}
