package org.ministryofhealth.newimci;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.URLImageParser;
import org.ministryofhealth.newimci.model.HIVCare;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class HIVCareDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hivcare_detail);

        int id = getIntent().getIntExtra("care_id", 0);
        DatabaseHandler db = new DatabaseHandler(this);
        HIVCare care = db.getCare(id);
        HtmlTextView htmlTextView = findViewById(R.id.hiv_content);
        htmlTextView.setHtml(care.getContent(),new URLImageParser(htmlTextView, this));

        getSupportActionBar().setTitle(care.getTitle());
        getSupportActionBar().setSubtitle(care.getParent());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }
}
