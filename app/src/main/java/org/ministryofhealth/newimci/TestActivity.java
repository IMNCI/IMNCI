package org.ministryofhealth.newimci;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.TestAttempt;
import org.ministryofhealth.newimci.tests.AboutTestActivity;
import org.ministryofhealth.newimci.tests.ActualTestActivity;
import org.ministryofhealth.newimci.tests.AfterTestActivity;
import org.ministryofhealth.newimci.tests.ScoreboardActivity;
import org.ministryofhealth.newimci.tests.TestIntroductionActivity;
import org.ministryofhealth.newimci.tests.TestListActivity;
import org.ministryofhealth.newimci.util.Tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgTimeOfDay;
    TextView pointsText, testCompletedText;
    LinearLayout testLayout, statisticsLayout, informationLayout, exitLayout;
    DatabaseHandler db;
    TestAttempt latestAttempt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        db = new DatabaseHandler(this);

        latestAttempt = db.getLatestTestAttempt();

        imgTimeOfDay = findViewById(R.id.time_of_day);

        testLayout = findViewById(R.id.test_click);
        statisticsLayout = findViewById(R.id.statistics_click);
        informationLayout = findViewById(R.id.test_information);
        exitLayout = findViewById(R.id.exit_test_module);
        pointsText = findViewById(R.id.pointsText);
        testCompletedText = findViewById(R.id.testCompletedText);

        String salutation = getSalutation();
        initToolbar(salutation);

        testLayout.setOnClickListener(this);
        statisticsLayout.setOnClickListener(this);
        informationLayout.setOnClickListener(this);
        exitLayout.setOnClickListener(this);

        if (latestAttempt.getId() != 0){
            try {
//                Jun 12, 2018 - 3pm
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date testCompletedDate = format.parse(latestAttempt.getTest_completed());

                format = new SimpleDateFormat("MMM dd, yyyy - h a");
                String formattedTestCompleted = format.format(testCompletedDate);

                testCompletedText.setText(formattedTestCompleted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            catch (Exception ex){
                ex.printStackTrace();
            }
            pointsText.setText(latestAttempt.getTotal_score() + "/" + latestAttempt.getQuestions_attempted() + " Points");
        }else{
            pointsText.setText("Not Available");
        }
    }

    private void initToolbar(String salutation) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(salutation);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        Tools.setSystemBarColor(this, R.color.orange_500);
    }

    private String getSalutation(){
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if(timeOfDay >= 0 && timeOfDay < 12){
            imgTimeOfDay.setImageResource(R.drawable.ic_goodmorning);
            return "Good Morning";
        }else if(timeOfDay >= 12 && timeOfDay < 16){
            imgTimeOfDay.setImageResource(R.drawable.ic_post_meridiem);
            return "Good Afternoon";
        }else if(timeOfDay >= 16 && timeOfDay < 21){
            imgTimeOfDay.setImageResource(R.drawable.ic_sunset);
            return "Good Evening";
        }else if(timeOfDay >= 21 && timeOfDay < 24){
            imgTimeOfDay.setImageResource(R.drawable.ic_moon);
            return "Good Night";
        }

        return "Hello";
    }

    @Override
    protected void onResume() {
        super.onResume();
        initToolbar(getSalutation());
    }

    public void exitTest(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit Test?");
        builder.setIcon(R.drawable.ic_warning);
        builder.setMessage("You will lose your progress. Continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(TestActivity.this, MainPageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_click:
                startActivity(new Intent(this, TestIntroductionActivity.class));
                break;

            case R.id.exit_test_module:
                exitTest();
                break;

            case R.id.statistics_click:
                startActivity(new Intent(this, AfterTestActivity.class));
                break;

            case R.id.test_information:
                startActivity(new Intent(this, AboutTestActivity.class));
                break;
            default:
                Toast.makeText(this, "Feature coming soon! Hang in there... :D", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                exitTest();
                break;
        }
        return true;
    }
}
