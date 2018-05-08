package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.URLImageParser;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.TreatAilment;
import org.ministryofhealth.newimci.model.TreatAilmentTreatment;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreatAilmentActivity extends AppCompatActivity {
    private List<String> _listHeader = new ArrayList<>();
    private HashMap<String, List<TreatAilmentTreatment>> _listContent = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat_ailment);

        int ailment_id = getIntent().getIntExtra("ailment_id", 0);

        DatabaseHandler db = new DatabaseHandler(this);
        TreatAilment treatAilment = db.getTreatAilment(ailment_id);
        AgeGroup ageGroup = db.getAgeGroup(db.getTreatTitle(treatAilment.getTreat_titles_id()).getAge_group_id());
        List<TreatAilmentTreatment> treatAilmentTreatments = db.getTreatAilmentTreatments(ailment_id);

        getSupportActionBar().setTitle(treatAilment.getAilment());
        getSupportActionBar().setSubtitle(db.getTreatTitle(treatAilment.getTreat_titles_id()).getTitle());
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareData(treatAilment, treatAilmentTreatments);

        ExpandableListView expandableListView = (ExpandableListView) findViewById(R.id.ailmentsList);
        TreatAilmentExpandableList expandableListAdapter = new TreatAilmentExpandableList(this, _listHeader, this._listContent);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.expandGroup(0);
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

    public void prepareData(TreatAilment ailment, List<TreatAilmentTreatment> ailmentTreatments){
        if (ailment.getContent() != null) {
            _listHeader.add("Content");

            List<TreatAilmentTreatment> content_header = new ArrayList<>();
            TreatAilmentTreatment ailmentTreatment = new TreatAilmentTreatment();

            ailmentTreatment.setTreatment(ailment.getContent());
            content_header.add(ailmentTreatment);

            _listContent.put("Content", content_header);
        }

        if (ailmentTreatments.size() > 0){
            _listHeader.add("Treatment");
            _listContent.put("Treatment", ailmentTreatments);
        }
    }

    public class TreatAilmentExpandableList extends BaseExpandableListAdapter {
        private Context _context;
        private List<String> _listDataHeader;
        private HashMap<String, List<TreatAilmentTreatment>> _listDataChild;

        public TreatAilmentExpandableList(Context context, List<String> listDataHeader, HashMap<String, List<TreatAilmentTreatment>> listChildData){
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
        }

        @Override
        public int getGroupCount() {
            return this._listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return this._listDataChild.get(this._listDataHeader.get(i))
                    .size();
        }

        @Override
        public Object getGroup(int i) {
            return this._listDataHeader.get(i);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                    .get(childPosition);
        }

        @Override
        public long getGroupId(int i) {
            return i;
        }

        @Override
        public long getChildId(int i, int i1) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean b, View convertView, ViewGroup viewGroup) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.treat_group_layout, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.groupTitle);

            lblListHeader.setText(headerTitle);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean b, View convertView, ViewGroup viewGroup) {
            final TreatAilmentTreatment ailment = (TreatAilmentTreatment) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.treat_child_layout, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.treat_child_id);
            HtmlTextView txtHtmlChild = (HtmlTextView) convertView.findViewById(R.id.treat_child);
            ImageView imgHand = (ImageView) convertView.findViewById(R.id.image_hand);
            txtHtmlChild.setHtml(ailment.getTreatment(),new URLImageParser(txtHtmlChild, _context));
            txtListChild.setText(String.valueOf(ailment.getId()));

            if (getGroup(groupPosition) == "Content"){
                imgHand.setVisibility(View.GONE);
            }

            txtHtmlChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ailment.getId() != 0){
                        Intent intent = new Intent(_context, TreatAilmentTreatmentActivity.class);
                        intent.putExtra("treatment_id", ailment.getId());
                        _context.startActivity(intent);
                    }
                }
            });
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
