package org.ministryofhealth.newimci;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.ministryofhealth.newimci.adapter.ExpandableListAdapter;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.Category;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AssessClassifyIdentifyTreatmentActivity extends AppCompatActivity {
    DatabaseHandler db;
    ExpandableListAdapter adapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<Assessment>> listDataChild = new HashMap<String, List<Assessment>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assess_classify_identify_treatment);
        db = new DatabaseHandler(this);

        int age_group_id = getIntent().getIntExtra("age", 0);
        final AgeGroup group = db.getAgeGroup(age_group_id);
        prepareDataList(age_group_id);
        expListView = (ExpandableListView) findViewById(R.id.assessments_list);
        adapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        getSupportActionBar().setTitle("Assess, Classify & Identify Treatment");
        getSupportActionBar().setSubtitle(group.getAge_group());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        expListView.setAdapter(adapter);

        if (listDataHeader.size() > 0){
            for (int i = 0; i < listDataHeader.size(); i++){
                expListView.expandGroup(i);
            }
        }

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Intent intent = new Intent(getApplicationContext(), AssessmentDetailsActivity.class);
                intent.putExtra("assessment_id", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
                intent.putExtra("age_group", group.getAge_group());
                startActivity(intent);
                return false;
            }
        });
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
            case android.R.id.home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
        }
        return true;
    }

    private void prepareDataList(int age_group_id){
        List<Category> categories = db.getCategories();
        for (Category category
                : categories){
            List<Assessment> assessments = db.getAssessments(age_group_id, category.getId());
            if (assessments.size() > 0){
                listDataHeader.add(category.getCategory());
                listDataChild.put(category.getCategory(), assessments);
            }
        }
    }
}
