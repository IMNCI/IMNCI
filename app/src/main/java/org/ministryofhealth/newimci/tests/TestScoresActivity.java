package org.ministryofhealth.newimci.tests;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.AppHelper;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.tests.fragments.TestResponseFragment;

import java.util.List;

public class TestScoresActivity extends AppCompatActivity {
    List<TestAttempt> attemptList;
    DatabaseHandler db;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_scores);

        db = new DatabaseHandler(this);
        attemptList = db.getTestAttempts();

        recyclerView = findViewById(R.id.test_scores);

        getSupportActionBar().setTitle("Test Scores");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ScoreboardAdapter adapter = new ScoreboardAdapter();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
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

    public class ScoreboardAdapter extends RecyclerView.Adapter<TestScoresActivity.ScoreboardAdapter.ScoreboardViewHolder>{

        ScoreboardAdapter(){

        }

        @NonNull
        @Override
        public TestScoresActivity.ScoreboardAdapter.ScoreboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_layout, parent, false);
            TestScoresActivity.ScoreboardAdapter.ScoreboardViewHolder holder = new TestScoresActivity.ScoreboardAdapter.ScoreboardViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull TestScoresActivity.ScoreboardAdapter.ScoreboardViewHolder holder, final int position) {
            String timeStarted = attemptList.get(position).getTest_started();
            String totalScore = attemptList.get(position).getTotal_score();
            String questionsAttempted = attemptList.get(position).getQuestions_attempted();
            String timeCompleted = attemptList.get(position).getTest_completed();

            AppHelper appHelper = new AppHelper();
            try {
                String dateStarted = appHelper.formatDate(timeStarted, "yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
                String _timeStarted = appHelper.formatDate(timeStarted, "yyyy-MM-dd HH:mm:ss", "HH:mm:ss");

                holder.attemptDateText.setText(dateStarted);
                holder.attemptTimeText.setText(_timeStarted);
            }catch(Exception ex){
                Log.e("TestScoreDateFormatting", ex.getMessage());
            }

            float percentage = 0;
            if(timeCompleted != null) {
                percentage = Float.parseFloat(totalScore) / Float.parseFloat(questionsAttempted) * 100;
            }
            if(questionsAttempted == null || timeCompleted == null){
                holder.scoreText.setText("Not completed");
            }else {
                holder.scoreText.setText((int) percentage + "%");
            }

            float stars = 0;
            if (percentage > 0 && percentage < 25){
                stars = 1;
            }else if (percentage >= 25 && percentage < 50){
                stars = 2;
            } else if (percentage >= 50 && percentage < 75){
                stars = 3;
            } else if (percentage >= 75 && percentage < 100){
                stars = 4;
            } else if(percentage == 100){
                stars = 5;
                int icon = R.drawable.ic_winner;
                holder.imgAchievement.setImageResource(icon);
            }

            holder.ratingBar.setRating(stars);


            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TestScoresActivity.this, TestReviewActivity.class);
                    intent.putExtra("attempt_id", attemptList.get(position).getId());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return attemptList.size();
        }

        public class ScoreboardViewHolder extends RecyclerView.ViewHolder{
            TextView attemptDateText, attemptTimeText, scoreText;
            AppCompatRatingBar ratingBar;
            LinearLayout itemLayout;
            ImageView imgAchievement;

            public ScoreboardViewHolder(View itemView) {
                super(itemView);

                attemptDateText = itemView.findViewById(R.id.attempt_date);
                attemptTimeText = itemView.findViewById(R.id.attempt_time);
                scoreText = itemView.findViewById(R.id.score);
                ratingBar = itemView.findViewById(R.id.test_rating);
                itemLayout = itemView.findViewById(R.id.itemLayout);
                imgAchievement = itemView.findViewById(R.id.icon);

                LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
                stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }
}
