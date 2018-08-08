package org.ministryofhealth.newimci.tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.TestAttempt;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class AfterTestActivity extends AppCompatActivity implements View.OnClickListener {
    int attempt_id;
    DatabaseHandler db;
    TestAttempt attempt;
    AppCompatRatingBar ratingBar;
    TextView timeTakenTextView, scoreTextView;
    String scoreString;
    String timeString;
    Boolean seconds = false;
    Button btnReview, btnTakeAnotherTest, backHome;
    ImageView winnerIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_test);

        db = new DatabaseHandler(this);

        attempt_id = getIntent().getIntExtra("attempt_id", 0);
        attempt = db.getTestAttemptAttempt(attempt_id);
        scoreString = getString(R.string.scoreString);
        timeString = getString(R.string.timeString);

        KonfettiView viewKonfetti = findViewById(R.id.viewKonfetti);
        ratingBar = findViewById(R.id.performanceRating);
        timeTakenTextView = findViewById(R.id.time_taken);
        scoreTextView = findViewById(R.id.score);
        btnReview = findViewById(R.id.review_button);
        btnTakeAnotherTest = findViewById(R.id.take_another);
        backHome = findViewById(R.id.backHome);
        winnerIcon = findViewById(R.id.winnerIcon);

        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.parseColor("#FFD700"), PorterDuff.Mode.SRC_ATOP);

        setUI();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            viewKonfetti.build()
                    .addColors(Color.parseColor("#009688"), Color.parseColor("#F44336"), Color.parseColor("#FFEB3B"))
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 5f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5))
                    .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                    .streamFor(300, 5000L);
        }

        btnTakeAnotherTest.setOnClickListener(this);
        btnReview.setOnClickListener(this);
        backHome.setOnClickListener(this);
    }

    private void setUI(){
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        int correct_answers = Integer.parseInt(attempt.getTotal_score());
        int total_questions = Integer.parseInt(attempt.getQuestions_attempted());

        float stars = getStars(correct_answers, total_questions);
        float timeInMinutes = calculateTimeTaken();

        ratingBar.setRating(stars);
        String units = (seconds) ? "Secs" : "Mins";
        timeTakenTextView.setText(String.format(timeString, twoDForm.format(timeInMinutes), units));
        scoreTextView.setText(String.format(scoreString, correct_answers, total_questions));

    }

    private float getStars(int correct_answers, int total_questions){
        int stars;
        double percentage = ((double) correct_answers / total_questions) * 100;
        if (percentage >= 0 && percentage < 25){
            stars = 1;
        }else if (percentage >= 25 && percentage < 50){
            stars = 2;
        } else if (percentage >= 50 && percentage < 75){
            stars = 3;
        } else if (percentage >= 75 && percentage < 100){
            stars = 4;
        } else{
            stars = 5;
            winnerIcon.setImageResource(R.drawable.ic_winner);
        }
        return (float)stars;
    }

    private float calculateTimeTaken(){
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

            differenceSeconds = (float) difference / 1000 % 60;
            differenceMinutes = (float)difference / (60 * 1000) % 60;
            if(differenceMinutes >= 1) {
                seconds = false;
                return differenceMinutes;
            }else {
                seconds = true;
                return differenceSeconds;
            }
        }catch (Exception ex){
            Log.e("TimeCalculation", ex.getMessage());
            return 0;
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.take_another:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Take another test");
                builder.setMessage("Do you really want to start another test?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(AfterTestActivity.this, TestIntroductionActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.show();
                break;

            case R.id.review_button:
                Intent intent = new Intent(this, TestReviewActivity.class);
                intent.putExtra("attempt_id", attempt_id);
                startActivity(intent);
                break;
            case R.id.backHome:
                Intent homeIntent = new Intent(this, TestActivity.class);
                startActivity(homeIntent);
                finish();
                break;
        }
    }
}
