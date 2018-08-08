package org.ministryofhealth.newimci.tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.fragment.QuestionFragment;
import org.ministryofhealth.newimci.model.Question;
import org.ministryofhealth.newimci.model.QuestionChoice;
import org.ministryofhealth.newimci.model.Test;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.model.TestResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class ActualTestActivity extends AppCompatActivity implements QuestionFragment.IGetValues{
    Test test;
    public List<Question> questions = new ArrayList<>();
    List<QuestionChoice> questionChoices = new ArrayList<>();
    DatabaseHandler databaseHandler;
    public TestFragmentPagerAdapter adapter;
    ViewPager viewPager;
    Button nextButton, finishButton, backButton;
    TextView txtCounter;
    Formatter formatter;
    private static final String counterString = "Question %1$2s of %2$2s";
    TestAttempt attempt;
    int attempt_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_test);

        databaseHandler = new DatabaseHandler(this);
        questions = databaseHandler.getQuestions();

        attempt_id = getIntent().getIntExtra("attempt_id", 0);

        attempt = databaseHandler.getTestAttemptAttempt(attempt_id);

        nextButton = findViewById(R.id.nextButton);
        backButton = findViewById(R.id.backButton);
        finishButton = findViewById(R.id.finishButton);
        txtCounter = findViewById(R.id.question_counter);

        adapter = new TestFragmentPagerAdapter(getSupportFragmentManager());

        viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        StringBuilder sb = new StringBuilder();
        formatter = new Formatter(sb, Locale.US);

        setCounterText(viewPager.getCurrentItem() + 1);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HashMap<Integer, Boolean> values = mFragment.getResponses();
                QuestionFragment mFragment = (QuestionFragment) adapter.getItem(viewPager.getCurrentItem());
//                HashMap<Integer, Boolean> values = mFragment.getData();
//                Log.d("Values", values.toString());

                if (checkIfAnswered(attempt_id, questions.get(viewPager.getCurrentItem()).getId())) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    setCounterText(viewPager.getCurrentItem() + 1);
                    if (viewPager.getCurrentItem() + 1 == questions.size()) {
                        nextButton.setVisibility(View.GONE);
                        finishButton.setVisibility(View.VISIBLE);
                    } else {
                        nextButton.setVisibility(View.VISIBLE);
                        finishButton.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(ActualTestActivity.this, getString(R.string.unanswered_question_response), Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_count = viewPager.getCurrentItem() - 1;
                viewPager.setCurrentItem(current_count);
                setCounterText(viewPager.getCurrentItem() + 1);
                if (viewPager.getCurrentItem() + 1 == questions.size()){
                    nextButton.setVisibility(View.GONE);
                    finishButton.setVisibility(View.VISIBLE);
                }else{
                    nextButton.setVisibility(View.VISIBLE);
                    finishButton.setVisibility(View.GONE);
                }
            }
        });
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkIfAnswered(attempt_id, questions.get(viewPager.getCurrentItem()).getId())) {
                    finishTest();
                }else{
                    Toast.makeText(ActualTestActivity.this, getString(R.string.unanswered_question_response), Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position != 0) {
                    QuestionFragment mFragment = (QuestionFragment) adapter.getItem(position - 1);
                    mFragment.getFragmentData(position);
                }
                Question question = questions.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private boolean checkIfAnswered(int attempt_id, int question_id){
        int responses = databaseHandler.getResponseCount(attempt_id, questions.get(viewPager.getCurrentItem()).getId());
        if (responses > 0)
            return true;
        return false;
    }

    private void setCounterText(int position){
        txtCounter.setText("");
        txtCounter.setText(String.format(counterString, position, questions.size()));
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();;
    }

    private void finishTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Complete Test");
        builder.setMessage("Submit your responses now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                String current_time = formatter.format(currentTime);
                TestAttempt finishAttempt = databaseHandler.getTestAttemptAttempt(attempt_id);
                finishAttempt.setTest_completed(current_time);
                databaseHandler.updateTestAttempt(finishAttempt);
                Intent intent = new Intent(ActualTestActivity.this, AfterTestActivity.class);
                intent.putExtra("attempt_id", attempt_id);
                markTest();
                startActivity(intent);
                Toast.makeText(ActualTestActivity.this, "Test Complete", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    private void markTest(){
        List<TestResponse> responseList;

        responseList = databaseHandler.getTestResponses(attempt_id);
        attempt = databaseHandler.getTestAttemptAttempt(attempt_id);

        HashMap<Integer, List<Integer>> questionResponses = new HashMap<>();
        String timeStarted = attempt.getTest_started();
        String timeCompleted = attempt.getTest_completed();

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
                responseList) {
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
            boolean got_it = false;
            HashMap.Entry pair = (HashMap.Entry) it.next();
            List<Integer> values = (List<Integer>) pair.getValue();
            Question question = databaseHandler.getQuestion((Integer) pair.getKey());
            List<QuestionChoice> answers = databaseHandler.getCorrectAnswers((Integer) pair.getKey());
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
                got_it = false;
                incorrect_total++;
            }else{
                got_it = true;
                correct_total++;
            }
            int qid = question.getId();
            for (Integer response_id : values){
                TestResponse response = databaseHandler.getTestResponse(qid, attempt_id, response_id);
                response.setGot_it(got_it);
                databaseHandler.markTestResponse(response);
            }
            question_count++;
        }

        if(attempt.getCorrect_answers() == null){
            attempt.setQuestions_attempted(String.valueOf(question_count));
            attempt.setTotal_score(String.valueOf(correct_total));
            attempt.setWrong_answers(String.valueOf(incorrect_total));

            databaseHandler.updateTestAttempt(attempt);
        }
    }

    private void exitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Test?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("You will lose your progress. Continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ActualTestActivity.this, TestActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public HashMap<Integer, Boolean> getResponses(int question_id, Boolean singleType, RadioGroup radioGroup) {
        Toast.makeText(this, "" + question_id, Toast.LENGTH_SHORT).show();
        return null;
    }

    public class TestFragmentPagerAdapter extends FragmentPagerAdapter{

        public TestFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuestionFragment fragment = new QuestionFragment();
            int question = questions.get(position).getId();
            return QuestionFragment.newInstance(question, position, getCount(), attempt.getId());
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }
}
