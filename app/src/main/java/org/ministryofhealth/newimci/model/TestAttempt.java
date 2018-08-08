package org.ministryofhealth.newimci.model;

import org.ministryofhealth.newimci.database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestAttempt {
    private int id, user_id, uploaded, deleted;
    private String test_started, test_cancelled, test_completed, questions_attempted, total_score, correct_answers, wrong_answers;
    private List<TestResponse> responses;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getTest_started() {
        return test_started;
    }

    public void setTest_started(String test_started) {
        this.test_started = test_started;
    }

    public String getTest_cancelled() {
        return test_cancelled;
    }

    public void setTest_cancelled(String test_cancelled) {
        this.test_cancelled = test_cancelled;
    }

    public String getTest_completed() {
        return test_completed;
    }

    public void setTest_completed(String test_completed) {
        this.test_completed = test_completed;
    }

    public String getQuestions_attempted() {
        return questions_attempted;
    }

    public void setQuestions_attempted(String questions_attempted) {
        this.questions_attempted = questions_attempted;
    }

    public String getTotal_score() {
        return total_score;
    }

    public void setTotal_score(String total_score) {
        this.total_score = total_score;
    }

    public String getCorrect_answers() {
        return correct_answers;
    }

    public void setCorrect_answers(String correct_answers) {
        this.correct_answers = correct_answers;
    }

    public String getWrong_answers() {
        return wrong_answers;
    }

    public void setWrong_answers(String wrong_answers) {
        this.wrong_answers = wrong_answers;
    }

    public int save(DatabaseHandler db) {
        return db.addTestAttempt(this);
    }

    public void cancelTest(DatabaseHandler db, String current_time) {

        this.setTest_cancelled(current_time);
        db.updateTestAttempt(this);
    }

    public int getUploaded() {
        return uploaded;
    }

    public void setUploaded(int uploaded) {
        this.uploaded = uploaded;
    }

    public int getDeleted() {
        return deleted;
    }

    public void setDeleted(int deleted) {
        this.deleted = deleted;
    }

    public List<TestResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<TestResponse> responses) {
        this.responses = responses;
    }
}
