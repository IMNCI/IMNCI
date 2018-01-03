package org.ministryofhealth.newimci;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.HtmlHelper;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.TreatAilment;
import org.ministryofhealth.newimci.model.TreatTitle;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TreatActivity extends AppCompatActivity {
    DatabaseHandler db;
    List<TreatTitle> titles;
    static FloatingActionButton downFab, upFab;

    int age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treat);

        db = new DatabaseHandler(this);

        upFab = (FloatingActionButton) findViewById(R.id.scrollUpFab);
        downFab = (FloatingActionButton) findViewById(R.id.scrollDownFab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        age = getIntent().getIntExtra("age", 0);
        AgeGroup ageGroup = db.getAgeGroup(age);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        TextView txtSubtitle = (TextView) findViewById(R.id.subtitle);

        titles = db.getTreatTitles(age);
        txtSubtitle.setText(ageGroup.getAge_group());
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(
                getApplicationContext(), titles);
        spinnerAdapter.setDropDownViewTheme(toolbar.getContext().getTheme());
        spinner.setAdapter(spinnerAdapter);

//        spinner.setAdapter(new MyAdapter(toolbar.getContext(), setUpTitles()));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position, titles))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
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
            case android.R.id.home:
            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
        }
        return true;
    }

    public List<String> setUpTitles(){
        List<String> titles = new ArrayList<>();
        List<TreatTitle> treatTitles = db.getTreatTitles(age);

        for (TreatTitle title:
             treatTitles) {
            titles.add(title.getTitle());
        }

        return titles;
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, List<String> objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }

    public static class GuideAdapter extends BaseExpandableListAdapter{
        private Context _context;
        private List<String> _listDataHeader;
        private HashMap<String, List<TreatAilment>> _listDataChild;

        public GuideAdapter(Context context, List<String> listDataHeader, HashMap<String, List<TreatAilment>> listChildData){
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
            final TreatAilment ailment = (TreatAilment) getChild(groupPosition, childPosition);

            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.treat_child_layout, null);
            }

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.treat_child_id);
            HtmlTextView txtHtmlChild = (HtmlTextView) convertView.findViewById(R.id.treat_child);
            ImageView imgHand = (ImageView) convertView.findViewById(R.id.image_hand);
            txtHtmlChild.setHtml(ailment.getAilment());
            txtListChild.setText(String.valueOf(ailment.getId()));

            if (getGroup(groupPosition) == "Guide"){
                imgHand.setVisibility(View.GONE);
            }

            txtHtmlChild.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ailment.getId() != 0){
                        Intent intent = new Intent(_context, TreatAilmentActivity.class);
                        intent.putExtra("ailment_id", ailment.getId());
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

    private static class SpinnerAdapter extends BaseAdapter implements ThemedSpinnerAdapter{
        List<TreatTitle> titles;
        Context context;
        LayoutInflater inflater;
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;
        public SpinnerAdapter(Context context, List<TreatTitle> titles){
            this.titles = titles;
            this.context = context;
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }
        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int i) {
            return titles.get(i);
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
                view = inflater.inflate(R.layout.titles_spinner_row_item, null);

            TreatTitle title = titles.get(i);

            TextView txtTitle = view.findViewById(R.id.treat_title);
            txtTitle.setText(title.getTitle());
            return view;
        }

        @Override
        public void setDropDownViewTheme(@Nullable Theme theme) {
            mDropDownHelper.setDropDownViewTheme(context.getTheme());
        }

        @Nullable
        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_TITLES = "titles";


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber, List<TreatTitle> titles) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            TreatTitle title = titles.get(sectionNumber);
            args.putParcelable(ARG_TITLES, title);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_treat, container, false);
            TreatTitle title = getArguments().getParcelable(ARG_TITLES);
            List<String> listHeader = new ArrayList<>();
            final HashMap<String, List<TreatAilment>> listChildren = new HashMap<>();
            DatabaseHandler db = new DatabaseHandler(getContext());
            if (title.getGuide() != null){
                listHeader.add("Guide");
                List<TreatAilment> ailmentTitleGuide = new ArrayList<>();
                TreatAilment treatAilmentGuide = new TreatAilment();

                treatAilmentGuide.setAilment(title.getGuide());
                ailmentTitleGuide.add(treatAilmentGuide);

                listChildren.put("Guide", ailmentTitleGuide);
            }
            List<TreatAilment> ailments = db.getTreatAilments(title.getId());
            if (ailments.size() > 0){
                listHeader.add("Treatment");
                listChildren.put("Treatment", ailments);
            }

            final GuideAdapter adapter = new GuideAdapter(getContext(), listHeader, listChildren);
            final ExpandableListView expandableListView = (ExpandableListView) rootView.findViewById(R.id.treattitleExpList);
            adapter.notifyDataSetChanged();
            expandableListView.setAdapter(adapter);

            if (listHeader.size() == 2){
                expandableListView.expandGroup(0);
//                expandableListView.expandGroup(1);
            }else if (listHeader.size() == 1){
                expandableListView.expandGroup(0);
            }

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//                    Toast.makeText(getContext(), listChildren.get("Ailments").get(childPosition).getAilment(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getActivity(), "Sample", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });

            if (adapter.getGroupCount() == 1){
                downFab.setVisibility(View.GONE);
                upFab.setVisibility(View.GONE);
            }else{
                downFab.setVisibility(View.VISIBLE);
                upFab.setVisibility(View.GONE);
            }

            downFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downFab.setVisibility(View.GONE);
                    upFab.setVisibility(View.VISIBLE);
                    expandableListView.collapseGroup(0);
                    expandableListView.expandGroup(adapter.getGroupCount() - 1);
                }
            });

            upFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    downFab.setVisibility(View.VISIBLE);
                    upFab.setVisibility(View.GONE);
                    expandableListView.collapseGroup(adapter.getGroupCount() - 1);
                    expandableListView.expandGroup(0);
                }
            });

//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);

//            if(!title.getGuide().isEmpty()){
//                textView.setText(HtmlHelper.parseHTML(title.getGuide()));
//            }else{
//                textView.setText("No guide available");
//            }


            return rootView;
        }
    }
}
