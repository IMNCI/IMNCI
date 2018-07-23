package org.ministryofhealth.newimci.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.AppHelper;
import org.ministryofhealth.newimci.model.Question;
import org.ministryofhealth.newimci.model.QuestionChoice;
import org.ministryofhealth.newimci.model.Test;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.model.TestResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class QuestionFragment extends Fragment {
    HashMap<Integer, Boolean> responses = new HashMap<>();
    int question_id, attempt_id;
    List<QuestionChoice> choices, correct_answers;
    RadioGroup radioGroup;
    boolean singleType = false;
    IGetValues mGetValues;
    Button btnExit;
    Context context;
    TestResponse response;
    long response_id;
    DatabaseHandler db;
    int number_checked = 0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("StringFormatMatches")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String counterString = getContext().getString(R.string.counterString);
        final View swipeView = inflater.inflate(R.layout.question_fragment, container, false);
        db = new DatabaseHandler(getContext());
        TextView tv = swipeView.findViewById(R.id.question);
        TextView title = swipeView.findViewById(R.id.toolbar);
        TextView questionText = swipeView.findViewById(R.id.question_text);
        radioGroup = swipeView.findViewById(R.id.question_choices_radiogroup);
        btnExit = swipeView.findViewById(R.id.exit_test);
        this.context = getContext();
        Bundle args = getArguments();
        question_id = args.getInt("question_id");
        int position = args.getInt("position");
        int questions = args.getInt("questions");
        attempt_id = args.getInt("attempt_id");
        Question question = db.getQuestion(question_id);
        Test test = db.getTest(question.getTests_id());
        title.setText(test.getTest_name());
        questionText.setText(String.format(counterString, position + 1, questions));

        choices = db.getQuestionChoices(question.getId());
        correct_answers = db.getCorrectAnswers(question.getId());

        JSONArray correctAnswersJsonArray = new JSONArray();
        for (QuestionChoice choice:
             choices) {
            if (choice.correct_answer)
                correctAnswersJsonArray.put(choice.toJSONObject());
        }

        response = db.getTestResponse(question_id, attempt_id);

        response.setQuestion_id(question_id);
        response.setAttempt_id(attempt_id);

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestAttempt attempt = db.getTestAttemptAttempt(attempt_id);
                Date currentTime = Calendar.getInstance().getTime();
                SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
                String current_time = formatter.format(currentTime);
                attempt.cancelTest(db, current_time);
                AppHelper.exitTest(getActivity());
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) swipeView.findViewById(checkedId);
                response.setResponse_id(checkedId);
                response.setResponse(button.getText().toString());

                response = db.addTestResponse(response);
                Log.d("ADDRESPONSE", response.getId() + "");
            }
        });

        tv.setText(question.getQuestion());
//            RadioGroup ll = new RadioGroup(getContext());
        if(question.getQuestion_type().equals("Single Choice")){
            singleType = true;
            setupradiobuttons(choices, swipeView);
        }
        else{
            singleType = false;
            setupcheckboxbuttons(choices, swipeView);
        }
        return swipeView;
    }

    private void setupradiobuttons(List<QuestionChoice> choices, View swipeView){

        radioGroup.setVisibility(View.VISIBLE);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        for (QuestionChoice choice:
                choices) {
            RadioButton button = new RadioButton(getContext());
            button.setId(choice.getId());
            button.setText(choice.getChoice());
            button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.muli));
            radioGroup.addView(button);
        }
    }

    private void setupcheckboxbuttons(final List<QuestionChoice> choices, View swipeView){
        LinearLayout checkGroup = swipeView.findViewById(R.id.question_choices_check);
        checkGroup.setVisibility(View.VISIBLE);
        checkGroup.setOrientation(LinearLayout.VERTICAL);
        for (QuestionChoice choice:
                choices) {
            CheckBox button = new  CheckBox(getContext());
            button.setId(choice.getId());
            button.setText(choice.getChoice());
            button.setTypeface(ResourcesCompat.getFont(getContext(), R.font.muli));

            button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int id = buttonView.getId();
                    if (isChecked){
                        number_checked++;
                        if(number_checked == choices.size()){
                            Toast.makeText(getContext(), "You cannot check all choices", Toast.LENGTH_SHORT).show();
                            buttonView.toggle();
                            number_checked--;
                        }else {
                            response.setResponse_id(id);
                            response.setResponse(buttonView.getText().toString());
                            response.setId(0);
                            response = db.addTestResponse(response);
                        }
                    }else{
                        number_checked--;
                        db.removeResponse(question_id, attempt_id, id);
                    }
                }
            });
            checkGroup.addView(button);
        }
    }

    public static QuestionFragment newInstance(int question_id, int position, int questions, int attempt_id){
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt("question_id", question_id);
        args.putInt("position", position);
        args.putInt("questions", questions);
        args.putInt("attempt_id", attempt_id);
        fragment.setArguments(args);
        return fragment;
    }

    public void getFragmentData(int pos){
//        int selected_radio = radioGroup.getCheckedRadioButtonId();
//        Log.d("FragmentData", "Called from Fragment (" + selected_radio + ")");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        try{
            mGetValues = (IGetValues) context;
        }catch (ClassCastException ex){
            Log.e("QuestionFragmentAttach", ex.getMessage());
            Toast.makeText(context, "There was an error casting the activity", Toast.LENGTH_SHORT).show();
        }
    }

    public interface IGetValues{
        HashMap<Integer, Boolean> getResponses(int question_id, Boolean singleType, RadioGroup radioGroup);
    }
}
