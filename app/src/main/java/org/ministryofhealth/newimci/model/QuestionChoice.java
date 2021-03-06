package org.ministryofhealth.newimci.model;

import org.json.JSONException;
import org.json.JSONObject;

public class QuestionChoice {
    public int id, questions_id;
    public String choice;
    public boolean correct_answer;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuestions_id() {
        return questions_id;
    }

    public void setQuestions_id(int questions_id) {
        this.questions_id = questions_id;
    }

    public boolean isCorrect_answer() {
        return correct_answer;
    }

    public void setCorrect_answer(boolean correct_answer) {
        this.correct_answer = correct_answer;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public JSONObject toJSONObject(){
        JSONObject json = new JSONObject();

        try {
            json.put("id", id);
            json.put("questions_id", questions_id);
            json.put("choice", choice);
            json.put("correct_answer", correct_answer);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }
}
