package org.ministryofhealth.newimci.tests;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.TestAttempt;

import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class ScoreboardActivity extends AppCompatActivity {
    List<TestAttempt> attemptList;
    DatabaseHandler db;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        db = new DatabaseHandler(this);
        attemptList = db.getTestAttempts();

        recyclerView = findViewById(R.id.all_test_attempts);
        ScoreboardAdapter adapter = new ScoreboardAdapter();
        RecyclerView.LayoutManager recyclerViewlayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewlayoutManager);
        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle("Your Tests");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);

        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .addSizes(new Size(12, 5))
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .streamFor(300, 5000L);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(ScoreboardActivity.this, TestActivity.class));
            finish();
        }
        return true;
    }

    public class ScoreboardAdapter extends RecyclerView.Adapter<ScoreboardAdapter.ScoreboardViewHolder>{

        @NonNull
        @Override
        public ScoreboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoreboard_layout, parent, false);
            ScoreboardViewHolder holder = new ScoreboardViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ScoreboardViewHolder holder, final int position) {
            String timeStarted = attemptList.get(position).getTest_started();
            String totalScore = attemptList.get(position).getTotal_score();
            String questionsAttempted = attemptList.get(position).getQuestions_attempted();
            holder.attemptTimeText.setText(timeStarted);
            if(questionsAttempted == null){
                holder.scoreText.setText("Not completed");
            }else {
                holder.scoreText.setText(totalScore + "/" + questionsAttempted);
            }

            holder.attemptTimeText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScoreboardActivity.this, TestMarkingActivity.class);
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
            TextView attemptTimeText, scoreText;
            public ScoreboardViewHolder(View itemView) {
                super(itemView);
                attemptTimeText = itemView.findViewById(R.id.attempt_time);
                scoreText = itemView.findViewById(R.id.score);
            }
        }
    }
}
