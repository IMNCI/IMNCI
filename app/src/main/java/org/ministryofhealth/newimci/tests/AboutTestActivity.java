package org.ministryofhealth.newimci.tests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TestActivity;

public class AboutTestActivity extends AppCompatActivity {
    TextView aboutTestTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_test);

        getSupportActionBar().setTitle("About Test");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        aboutTestTxt = findViewById(R.id.about_test);

        aboutTestTxt.setText(Html.fromHtml(getString(R.string.about_test)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            startActivity(new Intent(AboutTestActivity.this, TestActivity.class));
            finish();
        }
        return true;
    }
}
