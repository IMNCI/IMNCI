package org.ministryofhealth.newimci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ministryofhealth.newimci.tests.TestIntroductionActivity;
import org.ministryofhealth.newimci.tests.TestListActivity;
import org.ministryofhealth.newimci.util.Tools;

import java.util.Calendar;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView imgTimeOfDay;
    LinearLayout testLayout, statisticsLayout, informationLayout, exitLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        imgTimeOfDay = findViewById(R.id.time_of_day);

        testLayout = findViewById(R.id.test_click);
        statisticsLayout = findViewById(R.id.statistics_click);
        informationLayout = findViewById(R.id.test_information);
        exitLayout = findViewById(R.id.exit_test_module);

        String salutation = getSalutation();
        initToolbar(salutation);

        testLayout.setOnClickListener(this);
        statisticsLayout.setOnClickListener(this);
        informationLayout.setOnClickListener(this);
        exitLayout.setOnClickListener(this);
    }

    private void initToolbar(String salutation) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(salutation);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.test_click:
                startActivity(new Intent(this, TestIntroductionActivity.class));
                break;
            default:
                Toast.makeText(this, "Feature coming soon! Hang in there... :D", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
