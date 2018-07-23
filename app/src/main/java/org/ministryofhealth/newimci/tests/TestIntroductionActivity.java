package org.ministryofhealth.newimci.tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.TestAttempt;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestIntroductionActivity extends AppCompatActivity {
    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;
    DatabaseHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_introduction);

        db = new DatabaseHandler(this);

        fab =findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.coordinator_lyt);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Begin Test");

        TextView txtIntro = findViewById(R.id.introduction_text);
        String introString = getString(R.string.about_test);
        txtIntro.setText(Html.fromHtml(introString));
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Test?");
        builder.setMessage("The test is about to begin. It is timed and you are requested not to leave the app once the test has began.");
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SharedPreferences shared = getSharedPreferences("user_details", MODE_PRIVATE);
                int user_id = shared.getInt("id", 0);
                if (user_id != 0) {
                    startTest(user_id);
                }else{
                    AlertDialog.Builder nouserBuilder = new AlertDialog.Builder(TestIntroductionActivity.this);
                    nouserBuilder.setTitle("No User Account");
                    nouserBuilder.setIcon(R.drawable.ic_account_box);
                    nouserBuilder.setMessage("We have detected you do not have a profile set up. Please ensure you have a profile setup before proceeding.");
                    nouserBuilder.setPositiveButton("Update Profile", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivityForResult(new Intent("org.ministryofhealth.newimci.SetupActivity"), 100);
                        }
                    });
                    nouserBuilder.setNegativeButton("Not Now", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Toast.makeText(TestIntroductionActivity.this, "You must register your profile before proceeding", Toast.LENGTH_LONG).show();
                        }
                    });

                    nouserBuilder.show();
                }
            }
        });
        builder.setNegativeButton("Hold on", null);
        builder.show();
    }

    private void startTest(int user_id){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String current_time = formatter.format(currentTime);

        TestAttempt attempt = new TestAttempt();

        attempt.setUser_id(user_id);
        attempt.setTest_started(current_time);

        int id = attempt.save(db);
        Intent intent = new Intent(TestIntroductionActivity.this, ActualTestActivity.class);
        intent.putExtra("attempt_id", id);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && resultCode == RESULT_OK) {
            int user_id = data.getIntExtra("user_id", 0);
            if (user_id == 0){
                showConfirmDialog();
            }else{
                startTest(user_id);
            }
        }else{
            Toast.makeText(this, "Could not find user requested", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
