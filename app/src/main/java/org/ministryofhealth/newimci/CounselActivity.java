package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.CounselSubContent;
import org.ministryofhealth.newimci.model.CounselTitle;

import java.util.ArrayList;
import java.util.List;

public class CounselActivity extends AppCompatActivity {
    DatabaseHandler db;
    List<CounselTitle> titleList = new ArrayList<>();
    ListView counselListView;
    ExpandableListView counselExpandableListView;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsel);
        context = this;

        db = new DatabaseHandler(this);

        final int age = getIntent().getIntExtra("age", 0);
        titleList = db.getCounselTitles(age);
        AgeGroup ageGroup = db.getAgeGroup(age);


        getSupportActionBar().setTitle("Counsel the Mother");
        getSupportActionBar().setSubtitle(ageGroup.getAge_group());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        counselListView = (ListView) findViewById(R.id.counselTitles);
        counselExpandableListView = (ExpandableListView) findViewById(R.id.counselExpandable);

        CounselTheMotherAdapter adapter = new CounselTheMotherAdapter();
        CounselTheMotherExpandableListViewAdapter expandableListViewAdapter = new CounselTheMotherExpandableListViewAdapter(titleList);

        counselExpandableListView.setAdapter(expandableListViewAdapter);
        
        counselExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group, int child, long l) {
                CounselSubContent subContent = db.getCounselSubContents(titleList.get(group).getId()).get(child);
                Intent intent = new Intent(context, CounselTitleActivity.class);
                intent.putExtra("type", "subcontent");
                intent.putExtra("id", subContent.getId());
                startActivity(intent);
                return false;
            }
        });

        counselListView.setAdapter(adapter);
        counselListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CounselTitle title = titleList.get(i);

                Intent intent = new Intent(CounselActivity.this, CounselTitleActivity.class);
                intent.putExtra("title_id", title.getId());

                startActivity(intent);
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
        return false;
    }


    public class CounselTheMotherAdapter extends BaseAdapter{
        LayoutInflater inflater;
        @Override
        public int getCount() {
            return titleList.size();
        }

        @Override
        public Object getItem(int i) {
            return titleList.get(i);
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

            CounselTitle title = titleList.get(i);

            TextView txtTitle = (TextView) view.findViewById(R.id.ailment);
            TextView txtTitleID = (TextView) view.findViewById(R.id.ailment_id);

            txtTitle.setText(title.getTitle());
            txtTitleID.setText(String.valueOf(title.getId()));

            return view;
        }
    }

    public class CounselTheMotherExpandableListViewAdapter extends BaseExpandableListAdapter{
        List<CounselTitle> titles;
        public CounselTheMotherExpandableListViewAdapter(List<CounselTitle> titles){
            this.titles = titles;
        }
        @Override
        public int getGroupCount() {
            return titles.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return db.getCounselSubContents(titles.get(i).getId()).size();
        }

        @Override
        public Object getGroup(int i) {
            return titles.get(i);
        }

        @Override
        public Object getChild(int group, int child) {
            CounselTitle title = (CounselTitle) getGroup(group);
            int title_id = title.getId();
            return db.getCounselSubContents(title_id).get(child);
        }

        @Override
        public long getGroupId(int i) {
            return titles.get(i).getId();
        }

        @Override
        public long getChildId(int group, int child) {
            CounselSubContent subContent = (CounselSubContent) this.getChild(group, child);
            return subContent.getId();
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
            final CounselTitle title = this.titles.get(i);
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (this.getChildrenCount(i) > 0){
                if (view == null) {
                    view = infalInflater.inflate(R.layout.custom_list_group, null);
                }

                TextView blListHeader = (TextView) view
                        .findViewById(R.id.lblListHeader);
                ImageView img = (ImageView) view.findViewById(R.id.indicator);

                if (b){
                    img.setImageResource(R.drawable.ic_remove_circle_outline_black_24dp);
                }else{
                    img.setImageResource(R.drawable.ic_add_circle_outline_black_24dp);
                }

                blListHeader.setText(title.getTitle());

            }else{
                if (view == null) {
                    view = infalInflater.inflate(R.layout.follow_up_ailments, null);
                }
                TextView txtTitle = (TextView) view.findViewById(R.id.ailment);
                TextView txtTitleID = (TextView) view.findViewById(R.id.ailment_id);

                txtTitle.setTextColor(context.getResources().getColor(R.color.colorBlack));
                txtTitle.setText(title.getTitle());
                txtTitleID.setText(String.valueOf(title.getId()));

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, CounselTitleActivity.class);
                        intent.putExtra("type", "title");
                        intent.putExtra("id", title.getId());
                        startActivity(intent);
                    }
                });
            }
            return view;
        }

        @Override
        public View getChildView(int group, int child, boolean b, View view, ViewGroup viewGroup) {
            CounselSubContent subContent = (CounselSubContent) this.getChild(group, child);

            if (view == null){
                LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = infalInflater.inflate(R.layout.follow_up_ailments, null);

                view.setBackgroundColor(Color.parseColor("#FFF9C4"));
            }

            TextView txtTitle = (TextView) view.findViewById(R.id.ailment);
            TextView txtTitleID = (TextView) view.findViewById(R.id.ailment_id);

            txtTitle.setText(subContent.getSub_content_title());
            txtTitleID.setText(String.valueOf(subContent.getId()));

            int[] childPadding = new int[] { android.R.attr.expandableListPreferredChildPaddingLeft };
            int[] actualPadding = new int[] { android.R.attr.listPreferredItemPaddingRight };
            TypedArray a = context.obtainStyledAttributes(childPadding);
            TypedArray actualPaddingArray = context.obtainStyledAttributes(actualPadding);

            int padding = a.getDimensionPixelSize(0, 0);
            int actual_padding = actualPaddingArray.getDimensionPixelSize(0,0);
            a.recycle();

            txtTitle.setPadding(padding, actual_padding, actual_padding, actual_padding);
            return view;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
