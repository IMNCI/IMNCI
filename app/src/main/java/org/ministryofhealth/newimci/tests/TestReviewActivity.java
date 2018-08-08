package org.ministryofhealth.newimci.tests;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.TestResponse;
import org.ministryofhealth.newimci.tests.fragments.TestResponseFragment;
import org.ministryofhealth.newimci.widgets.LineItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TestReviewActivity extends AppCompatActivity {
    DatabaseHandler db;
    int attempt_id;
    RecyclerView recyclerView;
    List<TestReviewSetting> reviewSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_review);
        db = new DatabaseHandler(this);
        recyclerView = findViewById(R.id.test_review);

        attempt_id = getIntent().getIntExtra("attempt_id", 0);

        getSupportActionBar().setTitle("Review Test");
        getSupportActionBar().setSubtitle("Click on question to view your response");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        reviewSettings = getTestReviewSettings();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        TestScoresClickListener listener = new TestScoresClickListener() {
            @Override
            public void onClick(View view, int position) {

                int question_id = reviewSettings.get(position).getId();
                String question = reviewSettings.get(position).getQuestion();
                showTestResponseFragment(question_id, question, attempt_id);
            }
        };

        TestReviewAdapter adapter = new TestReviewAdapter(listener);

        recyclerView.setAdapter(adapter);
    }

    private void showTestResponseFragment(int question_id, String question, int attempt_id) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        TestResponseFragment newFragment = new TestResponseFragment();
        Bundle args = new Bundle();
        args.putInt("question_id", question_id);
        args.putInt("attempt_id", attempt_id);
        args.putString("question", question);
        newFragment.setArguments(args);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment previousFragment = fragmentManager.findFragmentByTag("dialog");
        if (previousFragment != null){
            transaction.remove(previousFragment);
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.add(android.R.id.content, newFragment, "dialog").addToBackStack(null).commit();
    }

    private List<TestReviewSetting> getTestReviewSettings(){
        List<TestResponse> responseList = db.getTestResponses(attempt_id);
        List<TestReviewSetting> review_settings = new ArrayList<>();
        HashMap<Integer, TestReviewSetting> responseMappings = new HashMap<>();
        for (TestResponse response:
             responseList) {
            if (!responseMappings.containsKey(response.getQuestion_id())){
                TestReviewSetting testReviewSetting = new TestReviewSetting();
                testReviewSetting.setId(response.getQuestion_id());
                testReviewSetting.setQuestion(db.getQuestion(response.getQuestion_id()).question);
                testReviewSetting.setResponse(response.getGot_it());

                responseMappings.put(response.getQuestion_id(), testReviewSetting);
            }
        }

        for (Object o : responseMappings.entrySet()) {
            HashMap.Entry pair = (HashMap.Entry) o;
            TestReviewSetting testReviewSetting = (TestReviewSetting) pair.getValue();

            review_settings.add(testReviewSetting);
        }

        return review_settings;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        finish();
    }

    class TestReviewSetting{
        private int id;
        private String question;
        private boolean response;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getQuestion() {
            return question;
        }

        public void setQuestion(String question) {
            this.question = question;
        }

        public boolean isResponse() {
            return response;
        }

        public void setResponse(boolean response) {
            this.response = response;
        }
    }

    public class TestReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        private TestScoresClickListener mListener;

        TestReviewAdapter(TestScoresClickListener listener){
            mListener = listener;
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder;
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_review_item_layout, parent, false);
            viewHolder = new TestReviewViewHolder(view, mListener);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if(holder instanceof TestReviewViewHolder){
                final TestReviewViewHolder view = (TestReviewViewHolder) holder;

                TestReviewSetting reviewSetting = reviewSettings.get(position);
                view.txtQuestion.setText(Html.fromHtml(reviewSetting.getQuestion()));
                if (reviewSetting.isResponse()){
                    view.imgIcon.setImageResource(R.drawable.ic_check_mark);
                }else{
                    view.imgIcon.setImageResource(R.drawable.ic_cross_mark);
                }

                view.txtNumber.setText(String.valueOf(position + 1));
            }
        }

        @Override
        public int getItemCount() {
            return reviewSettings.size();
        }

        public class TestReviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView txtNumber, txtQuestion;
            ImageButton imgIcon;

            private TestScoresClickListener mListener;

            public TestReviewViewHolder(View itemView, TestScoresClickListener listener) {
                super(itemView);

                imgIcon = itemView.findViewById(R.id.imageBalance);
                txtNumber = itemView.findViewById(R.id.number);
                txtQuestion = itemView.findViewById(R.id.question);

                mListener = listener;

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                mListener.onClick(v, getAdapterPosition());
            }
        }
    }

    public interface TestScoresClickListener{
        void onClick(View view, int position);
    }
}
