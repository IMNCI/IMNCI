package org.ministryofhealth.newimci.tests;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.adapter.TestAdapter;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Test;
import org.ministryofhealth.newimci.widgets.LineItemDecoration;

import java.util.List;

public class TestListActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseHandler db;
    TestAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tests");
        getSupportActionBar().setSubtitle("List of tests available");

        db = new DatabaseHandler(this);
        initComponent();
    }
    private void initComponent() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new LineItemDecoration(this, LinearLayout.VERTICAL));
        recyclerView.setHasFixedSize(true);

        List<Test> tests = db.getTests();
        //set data and list adapter
        mAdapter = new TestAdapter(this, tests);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new TestAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Test obj, int position) {
                Intent intent = new Intent(TestListActivity.this, TestIntroductionActivity.class);
                intent.putExtra("test_id", obj.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                Toast.makeText(this, "Nothing clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
}
