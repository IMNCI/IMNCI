package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.adapter.AilmentsGridAdapter;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;

import java.util.ArrayList;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class FollowUpCareAilmentsActivity extends AppCompatActivity {

    ArrayList<Ailment> ailments = new ArrayList<>();
//    AilmentsGridAdapter adapter;
    DatabaseHandler db;
//    GridView gridView;
    ListView listview;
    AilmentListAdapter adapter;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_up_care_ailments);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpWindowAnimations();
        }

        context = this;

        getSupportActionBar().setTitle("Follow Up Care");
        int age_group_id = getIntent().getIntExtra("age", 0);
        listview = (ListView) findViewById(R.id.ailmentsList);
//        gridView = (GridView) findViewById(R.id.ailments_grid_view);
//        if (getResources().getConfiguration().orientation == ORIENTATION_LANDSCAPE) {
//            gridView.setNumColumns(4);
//        }
        db = new DatabaseHandler(this);
        ailments = db.getAilments(age_group_id);
        AgeGroup group = db.getAgeGroup(age_group_id);
        getSupportActionBar().setSubtitle(group.getAge_group());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        adapter = new AilmentListAdapter();
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ailment ailment = ailments.get(i);
                final AgeGroup ageGroup = db.getAgeGroup(ailment.getAge_group_id());
                Intent intent = new Intent(context, AilmentFollowUpCareActivity.class);
                intent.putExtra("ailment_id", ailment.getId());
                intent.putExtra("age", ageGroup.getAge_group());
                intent.putExtra("age_group_id", ageGroup.getId());
                context.startActivity(intent);
            }
        });
//        adapter = new AilmentsGridAdapter(this, R.layout.ailment_grid_item, ailments, this);
//        gridView.setAdapter(adapter);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setUpWindowAnimations(){
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
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
        return false;
    }

    public class AilmentListAdapter extends BaseAdapter{
        LayoutInflater inflater;
        @Override
        public int getCount() {
            return ailments.size();
        }

        @Override
        public Object getItem(int i) {
            return ailments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (view == null)
                view = inflater.inflate(R.layout.follow_up_ailments, null);

            Ailment ailment = ailments.get(i);

            TextView txtAilment = (TextView) view.findViewById(R.id.ailment);
            TextView txtAilmentID = (TextView) view.findViewById(R.id.ailment_id);

            txtAilment.setText(ailment.getAilment());
            txtAilmentID.setText(String.valueOf(ailment.getId()));
            return view;
        }
    }
}
