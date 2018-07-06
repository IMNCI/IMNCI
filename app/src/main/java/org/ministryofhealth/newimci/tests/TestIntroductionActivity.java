package org.ministryofhealth.newimci.tests;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;

public class TestIntroductionActivity extends AppCompatActivity {
    FloatingActionButton fab;
    CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_introduction);

        fab =findViewById(R.id.fab);
        coordinatorLayout = findViewById(R.id.coordinator_lyt);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmDialog();
            }
        });
    }

    private void showConfirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Start Test?");
        builder.setMessage("The test is about to begin. It is timed and you are requested not to leave the app once the test has began.");
        builder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(TestIntroductionActivity.this, ActualTestActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Hold on", null);
        builder.show();
    }
}
