package org.ministryofhealth.newimci.tests;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.EntryXComparator;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.AppHelper;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.util.Tools;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import pl.pawelkleczkowski.customgauge.CustomGauge;

public class ScoreboardActivity extends AppCompatActivity implements View.OnClickListener {
    List<TestAttempt> attemptList;
    DatabaseHandler db;
    RecyclerView recyclerView;
    BarChart performanceChart;
    TextView txtNumberTests, txtPercentage, txtLatestTime;
    TestAttempt latestAttempt;
    List<TestAttempt> latestAttempts;
    CardView tests_done_layout, latest_test_score_layout;
    TextView scoreText;

    Button viewTestScores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        initToolbar();

        txtNumberTests = findViewById(R.id.number_of_tests);
        performanceChart = findViewById(R.id.chart);
        viewTestScores = findViewById(R.id.view_test_scores);
        tests_done_layout = findViewById(R.id.tests_done_layout);
        txtLatestTime = findViewById(R.id.latest_time);
        latest_test_score_layout = findViewById(R.id.latest_score_layout);
        scoreText = findViewById(R.id.scoreText);
        viewTestScores.setVisibility(View.GONE);

        db = new DatabaseHandler(this);
        attemptList = db.getTestAttempts();
        latestAttempt = db.getLatestTestAttempt();
        latestAttempts = db.getLatestTestAttempts();

        AppHelper appHelper = new AppHelper();
        double latest_percentage = 0;
        if (latestAttempt.getId() != 0 && latestAttempt.getTest_completed() != null) {
            latest_percentage = Double.parseDouble(latestAttempt.getTotal_score()) / Double.parseDouble(latestAttempt.getQuestions_attempted()) * 100;
            try {
                txtLatestTime.setText(appHelper.formatDate(latestAttempt.getTest_completed(), "yyyy-MM-dd HH:mm:ss", "MMM dd, yyyy - h a"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txtNumberTests.setText(String.valueOf(attemptList.size()));

        txtPercentage = findViewById(R.id.latest_test_percentage);
        txtPercentage.setText(Math.round(latest_percentage) + "%");
        viewTestScores.setOnClickListener(this);
        tests_done_layout.setOnClickListener(this);
        latest_test_score_layout.setOnClickListener(this);

        RotateAnimation rotate= (RotateAnimation) AnimationUtils.loadAnimation(this,R.anim.rotate_animation);
        scoreText.setAnimation(rotate);
        setUpPerformanceChart();

    }

    private void initToolbar() {
        getSupportActionBar().setTitle("Scoreboard");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpPerformanceChart(){
        List<BarEntry> chartEntries = new ArrayList<BarEntry>();
        for (TestAttempt attempt:
                latestAttempts) {
            if(attempt.getTest_completed() != null) {
                float percentage = Float.parseFloat(attempt.getTotal_score()) / Float.parseFloat(attempt.getQuestions_attempted()) * 100;
                chartEntries.add(new BarEntry(attempt.getId(), percentage));
            }
        }
        if(chartEntries.size() > 0) {
            Collections.sort(chartEntries, new EntryXComparator());

            YAxis rightAxis = performanceChart.getAxisRight();
            rightAxis.setEnabled(false);

            YAxis yAxis = performanceChart.getAxisLeft();
            yAxis.setAxisMaximum((float) 100);
            yAxis.setAxisMinimum((float) 0);
            yAxis.setDrawGridLines(false);

            XAxis xAxis = performanceChart.getXAxis();
            xAxis.setAxisMinimum(1);
            xAxis.setDrawGridLines(false);
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawLabels(false);

            performanceChart.getLegend().setEnabled(false);

            BarDataSet chartDataset = new BarDataSet(chartEntries, "Percentage Scores");
//            chartDataset.setDrawFilled(true);
            chartDataset.setDrawValues(false);
//            chartDataset.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            chartDataset.setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaroon));
//            chartDataset.setFillColor(ContextCompat.getColor(getApplicationContext(), R.color.colorMaroon));
//            chartDataset.setDrawCircles(false);

            BarData lineData = new BarData(chartDataset);
            performanceChart.getDescription().setText("Your Recent Text Scores");
            performanceChart.setData(lineData);
            performanceChart.animateY(2000);
            performanceChart.invalidate();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(ScoreboardActivity.this, TestActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_test_scores:
            case R.id.tests_done_layout:
                startActivity(new Intent(this, TestScoresActivity.class));
                break;
            case R.id.latest_score_layout:
                Intent intent = new Intent(this, TestReviewActivity.class);
                intent.putExtra("attempt_id", latestAttempt.getId());
                startActivity(intent);
                break;
        }
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
