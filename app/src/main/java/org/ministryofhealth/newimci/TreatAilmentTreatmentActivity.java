package org.ministryofhealth.newimci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.URLImageParser;
import org.ministryofhealth.newimci.model.TreatAilmentTreatment;
import org.sufficientlysecure.htmltextview.HtmlAssetsImageGetter;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class TreatAilmentTreatmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat_ailment_treatment);

        int treatment_id = getIntent().getIntExtra("treatment_id", 0);

        DatabaseHandler db = new DatabaseHandler(this);
        TreatAilmentTreatment treatment = db.getTreatAilmentTreatment(treatment_id);
        getSupportActionBar().setTitle(treatment.getTreatment());
        getSupportActionBar().setSubtitle(db.getTreatAilment(treatment.getAilment_id()).getAilment());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        HtmlTextView htmlTextView = (HtmlTextView) findViewById(R.id.treatment_content);

        if (treatment.getContent() != null){
            htmlTextView.setHtml(treatment.getContent(), new URLImageParser(htmlTextView, this));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }
}
