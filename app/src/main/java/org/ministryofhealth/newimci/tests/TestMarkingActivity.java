package org.ministryofhealth.newimci.tests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Question;
import org.ministryofhealth.newimci.model.QuestionChoice;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.model.TestResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class TestMarkingActivity extends AppCompatActivity {
    int attempt_id;
    DatabaseHandler db;
    TestAttempt attempt;
    List<TestResponse> responseList;

    AppCompatRatingBar performanceRating;
    TextView txtTimeTaken, txtTimePerQuestion, txtQuestionsAttempted, txtCorrectAnswers, txtIncorrectAnswers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_marking);

        getSupportActionBar().setTitle("Test Results");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        attempt_id = getIntent().getIntExtra("attempt_id", 0);
        db = new DatabaseHandler(this);

        attempt = db.getTestAttemptAttempt(attempt_id);
        responseList = db.getTestResponses(attempt_id);

        performanceRating = (AppCompatRatingBar) findViewById(R.id.performanceRating);
        txtTimeTaken = (TextView) findViewById(R.id.time_taken);
        txtTimePerQuestion = (TextView) findViewById(R.id.time_per_question);
        txtQuestionsAttempted = (TextView) findViewById(R.id.questions_attempted);
        txtCorrectAnswers = (TextView) findViewById(R.id.correct_answers);
        txtIncorrectAnswers = (TextView) findViewById(R.id.incorrect_answers);

        performanceRating.setRating(5);

        markTest(responseList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(TestMarkingActivity.this, ScoreboardActivity.class));
                finish();
                break;
        }
        return true;
    }

    public void markTest(List<TestResponse> responses){
        // Get the correct answers while looping
        HashMap<Integer, List<Integer>> questionResponses = new HashMap<>();
        String timeStarted = attempt.getTest_started();
        String timeCompleted = attempt.getTest_completed();

        Log.d("TimeCalculation", timeStarted + " -> " + timeCompleted);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date timeStartedDate = null;
        Date timeCompletedDate = null;

        float differenceMinutes = 0, differenceSeconds = 0;

        try{
            timeStartedDate = format.parse(timeStarted);
            timeCompletedDate = format.parse(timeCompleted);

            long difference = timeCompletedDate.getTime() - timeStartedDate.getTime();
            Log.d("TimeCalculation", "Difference => " + difference);

            differenceSeconds = (float) difference / 1000 % 60;
            differenceMinutes = (float)difference / (60 * 1000) % 60;

            Log.d("TimeCalculation", "Difference Minutes => " + differenceMinutes + " => " + differenceSeconds);
        }catch (Exception ex){
            Log.e("TimeCalculation", ex.getMessage());
        }
        for (TestResponse response:
             responses) {
            List<Integer> responseListing = new ArrayList<>();
            if (questionResponses.containsKey(response.getQuestion_id())){
                responseListing = questionResponses.get(response.getQuestion_id());
            }

            responseListing.add(response.getResponse_id());
            questionResponses.put(response.getQuestion_id(), responseListing);
        }

        Iterator it = questionResponses.entrySet().iterator();
        int question_count = 0, incorrect_total = 0, correct_total = 0;
        while(it.hasNext()){
            HashMap.Entry pair = (HashMap.Entry) it.next();
            List<Integer> values = (List<Integer>) pair.getValue();
            Question question = db.getQuestion((Integer) pair.getKey());
            List<QuestionChoice> answers = db.getCorrectAnswers((Integer) pair.getKey());
            int correct_count = 0, incorrect_count = 0;
            List<Integer> answerIDs = new ArrayList<>();
            for (QuestionChoice oneAnswer:
                 answers) {
                answerIDs.add(oneAnswer.getId());
            }
            for (Integer value:
                 values) {
                if (answerIDs.contains(value)){
                    correct_count++;
                }else{
                    incorrect_count++;
                }
            }
            if (incorrect_count > 0){
                incorrect_total++;
            }else{
                correct_total++;
            }
            question_count++;
        }

        double percentage = ((double) correct_total / question_count) * 100;
        if (percentage >= 0 && percentage < 25){
            performanceRating.setRating((float)1);
        }else if (percentage >= 25 && percentage < 50){
            performanceRating.setRating((float)2);
        } else if (percentage >= 50 && percentage < 75){
            performanceRating.setRating((float)3);
        } else if (percentage >= 75 && percentage < 100){
            performanceRating.setRating((float)4);
        } else{
            performanceRating.setRating((float)5);
        }
        if(attempt.getCorrect_answers() == null){
            attempt.setQuestions_attempted(String.valueOf(question_count));
            attempt.setTotal_score(String.valueOf(correct_total));
            attempt.setWrong_answers(String.valueOf(incorrect_total));

            db.updateTestAttempt(attempt);
        }
        txtTimeTaken.setText((int) differenceMinutes + " m " + (int) differenceSeconds + " s");
        txtTimePerQuestion.setText((differenceMinutes / question_count) + " Mins");
        txtQuestionsAttempted.setText("" + question_count);
        txtCorrectAnswers.setText("" + correct_total);
        txtIncorrectAnswers.setText("" + incorrect_total);
    }
}
