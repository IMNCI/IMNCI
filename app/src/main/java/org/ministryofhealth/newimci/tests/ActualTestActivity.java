package org.ministryofhealth.newimci.tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import org.ministryofhealth.newimci.model.Question;
import org.ministryofhealth.newimci.model.QuestionChoice;
import org.ministryofhealth.newimci.model.Test;

import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;

public class ActualTestActivity extends AppCompatActivity {
    Test test;
    List<Question> questions = new ArrayList<>();
    List<QuestionChoice> questionChoices = new ArrayList<>();
    DatabaseHandler databaseHandler;
    TestFragmentPagerAdapter adapter;
    ViewPager viewPager;
    Button nextButton, finishButton, backButton;
    TextView txtCounter;
    Formatter formatter;
    private static final String counterString = "Question %1$2s of %2$2s";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actual_test);

        databaseHandler = new DatabaseHandler(this);
        questions = databaseHandler.getQuestions();

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
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                setCounterText(viewPager.getCurrentItem() - 1);
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

            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    private void setCounterText(int position){
        txtCounter.setText("");
        txtCounter.setText(String.format(counterString, position, questions.size()).toString());
    }

    @Override
    public void onBackPressed() {
        exitTest();
    }

    private void finishTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Complete Test");
//        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("Submit your responses now?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Intent intent = new Intent(ActualTestActivity.this, TestActivity.class);
//                startActivity(intent);
//                finish();
                Toast.makeText(ActualTestActivity.this, "Test Complete", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
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

    public class TestFragmentPagerAdapter extends FragmentPagerAdapter{

        public TestFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            QuestionFragment fragment = new QuestionFragment();
            int question = questions.get(position).getId();
            return QuestionFragment.newInstance(question);
        }

        @Override
        public int getCount() {
            return questions.size();
        }
    }

    public static class QuestionFragment extends Fragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.question_fragment, container, false);
            DatabaseHandler db = new DatabaseHandler(getContext());
            TextView tv = swipeView.findViewById(R.id.question);
            TextView title = swipeView.findViewById(R.id.toolbar);

            Bundle args = getArguments();
            int question_id = args.getInt("question_id");
            Question question = db.getQuestion(question_id);
            Test test = db.getTest(question.getTests_id());
            title.setText(test.getTest_name());

            List<QuestionChoice> choices = db.getQuestionChoices(question.getId());
            tv.setText(question.getQuestion());
//            RadioGroup ll = new RadioGroup(getContext());
            if(question.getQuestion_type().equals("Single Choice")){
                setupradiobuttons(choices, swipeView);
            }
            else{
                setupcheckboxbuttons(choices, swipeView);
            }
            return swipeView;
        }

        private void setupradiobuttons(List<QuestionChoice> choices, View swipeView){
            RadioGroup radioGroup = swipeView.findViewById(R.id.question_choices_radiogroup);
            radioGroup.setVisibility(View.VISIBLE);
            radioGroup.setOrientation(LinearLayout.VERTICAL);
            for (QuestionChoice choice:
                    choices) {
                RadioButton button = new RadioButton(getContext());
                button.setId(choice.getId());
                button.setText(choice.getChoice());
                radioGroup.addView(button);
            }
        }

        private void setupcheckboxbuttons(List<QuestionChoice> choices, View swipeView){
            LinearLayout checkGroup = swipeView.findViewById(R.id.question_choices_check);
            checkGroup.setVisibility(View.VISIBLE);
            checkGroup.setOrientation(LinearLayout.VERTICAL);
            for (QuestionChoice choice:
                    choices) {
                CheckBox button = new  CheckBox(getContext());
                button.setId(choice.getId());
                button.setText(choice.getChoice());
                checkGroup.addView(button);
            }
        }

        static QuestionFragment newInstance(int question_id){
            QuestionFragment fragment = new QuestionFragment();
            Bundle args = new Bundle();
            args.putInt("question_id", question_id);
            fragment.setArguments(args);
            return fragment;
        }
    }
}
