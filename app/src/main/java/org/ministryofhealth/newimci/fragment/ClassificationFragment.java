package org.ministryofhealth.newimci.fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.AssessmentClassification;
import org.ministryofhealth.newimci.model.AssessmentClassificationSign;
import org.ministryofhealth.newimci.model.AssessmentClassificationTreatment;
import org.ministryofhealth.newimci.model.DiseaseClassification;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClassificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClassificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClassificationFragment extends Fragment implements ExpandableListView.OnGroupExpandListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_ASSESSMENT = "assessment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Assessment mAssessment;

    private OnFragmentInteractionListener mListener;

    DatabaseHandler db;
    public List<String> parentsList = new ArrayList<String>();
    public List<AssessmentClassification> childClassifictions = new ArrayList<AssessmentClassification>();
    public HashMap<String, List<AssessmentClassification>> classifications = new HashMap<String, List<AssessmentClassification>>();
    public List<String> titles = new ArrayList<String>();
    private List<String> classificationList = new ArrayList<String>();
    public List<AssessmentClassification> assessmentClassifications;
    ParentLevel adapter;
    ExpandableListView expandableListView;

    public ClassificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ClassificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClassificationFragment newInstance(String param1, String param2, Assessment assessment) {
        ClassificationFragment fragment = new ClassificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_ASSESSMENT, assessment);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mAssessment = getArguments().getParcelable(ARG_ASSESSMENT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new DatabaseHandler(getActivity());
        View rootView = inflater.inflate(R.layout.fragment_classification, container, false);
        prepareData();
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.parentList);

        adapter = new ParentLevel();
        adapter.notifyDataSetChanged();
        expandableListView.setAdapter(adapter);

        if (this.parentsList.size() > 0){
            expandableListView.expandGroup(0);
        }

        expandableListView.setOnGroupExpandListener(this);
        if (parentsList.size() == 0){
            rootView = inflater.inflate(R.layout.empty_layout, container, false);


        }
        return rootView;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void prepareData(){
        parentsList = db.getParents(mAssessment.getId());
        titles.add("Signs");
        titles.add("Treatment");
        for (String parent : parentsList){
            List<AssessmentClassification> classification = db.getAssessmentByParent(parent, mAssessment.getId());
            classifications.put(parent, classification);
        }

    }

    @Override
    public void onGroupExpand(int i) {
        int len = adapter.getGroupCount();

        for (int j = 0; j < len; j++) {
            if (j != i) {
                expandableListView.collapseGroup(j);
            }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class ParentLevel extends BaseExpandableListAdapter{

        public ParentLevel() {
        }

        @Override
        public int getGroupCount() {
            return parentsList.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return parentsList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return classifications.get(parentsList.get(groupPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String headerTitle = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }
            this.notifyDataSetChanged();

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            CustExpListview secondLevel = null;
            secondLevel = new CustExpListview(getActivity());

            childClassifictions = (List<AssessmentClassification>) getChild(groupPosition, childPosition);
            final SecondLevelAdapter adapter = new SecondLevelAdapter();
            adapter.notifyDataSetChanged();
            secondLevel.setAdapter(adapter);
            final CustExpListview finalSecondLevel = secondLevel;
            secondLevel.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int i) {
                    for (int j = 0; j < adapter.getGroupCount(); j++) {
                        if (j != i) {
                            finalSecondLevel.collapseGroup(j);
                        }
                    }
                }
            });
            return secondLevel;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    public class CustExpListview extends ExpandableListView{

        int intGroupPosition, intChildPosition, intGroupid;

        public CustExpListview(Context context) {
            super(context);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(50000,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public class SecondLevelAdapter extends BaseExpandableListAdapter {


        @Override
        public int getGroupCount() {
            return childClassifictions.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return childClassifictions.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return titles.get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            AssessmentClassification groupHeader = (AssessmentClassification) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.assessment_title);
            RelativeLayout assessment_layout = (RelativeLayout) convertView.findViewById(R.id.line);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(groupHeader.getClassification());
            DiseaseClassification diseaseClassification = db.getClassification(groupHeader.getDisease_classification_id());
            assessment_layout.setBackgroundColor(Color.parseColor(diseaseClassification.getColor()));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            CustExpListview thirdLevel = new CustExpListview(getActivity());
            AssessmentClassification classification = (AssessmentClassification) getGroup(groupPosition);

            this.notifyDataSetChanged();


            HashMap<String, String> signs_treatments = new HashMap<String, String>();

            signs_treatments.put("Signs", classification.getSigns());
            signs_treatments.put("Treatment", classification.getTreatments());

            AssessmentClassification groupHeader = (AssessmentClassification) getGroup(groupPosition);
            DiseaseClassification diseaseClassification = db.getClassification(groupHeader.getDisease_classification_id());
            String parent_color = diseaseClassification.getColor();
            final ThirdLevelAdapter adapter = new ThirdLevelAdapter(signs_treatments, parent_color);
            adapter.notifyDataSetChanged();
            thirdLevel.setAdapter(adapter);
            thirdLevel.expandGroup(0);
            thirdLevel.expandGroup(1);
            thirdLevel.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView expThird, View view, int i, long l) {
                    Log.d("Adapter", String.valueOf(expThird.getExpandableListAdapter().getGroupCount()));
                    if (expThird.isGroupExpanded(i)){
                        expThird.collapseGroup(i);
                    }else{
                        expThird.expandGroup(i);
                    }
//                    for (int j = 0; j < titles.size(); j++) {
//                        if (j != i)
//                            expThird.collapseGroup(j);
//                    }
                    return true;
                }
            });
            return thirdLevel;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

    public class ThirdLevelAdapter extends BaseExpandableListAdapter{
        HashMap<String, String> signs_treatments;
        String mColor;
        public ThirdLevelAdapter(HashMap<String, String> signs_treatments, String color) {
            this.signs_treatments = signs_treatments;
            switch (color){
                case "#FF69B4":
                    this.mColor = "#FCE4EC";
                    break;
                case "#FFD700":
                    this.mColor = "#FFF9C4";
                    break;
                case "#32CD32":
                    this.mColor = "#DCEDC8";
                    break;
                default:
                    this.mColor = color;
                    break;
            }

        }

        @Override
        public int getGroupCount() {
            return titles.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if(signs_treatments.get(titles.get(groupPosition)).isEmpty())
                return 0;

            return 1;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return titles.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return signs_treatments.get(titles.get(groupPosition));
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            String title = (String) getGroup(groupPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item, null);
            }

            RelativeLayout assessment_layout = (RelativeLayout) convertView.findViewById(R.id.line);
//            assessment_layout.setBackgroundColor(Color.parseColor(mColor));
            LinearLayout mainLayout = (LinearLayout) convertView.findViewById(R.id.assessment_layout);
            mainLayout.setBackgroundColor(Color.parseColor(mColor));
            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.assessment_title);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(title);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            String text = (String) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getActivity()
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.html_list_item, null);
            }
            LinearLayout myLayout = (LinearLayout) convertView.findViewById(R.id.layoutHTML);
            myLayout.setBackgroundColor(Color.parseColor(mColor));
            HtmlTextView txtListChild = (HtmlTextView) convertView
                    .findViewById(R.id.assessment_title);

            txtListChild.setHtml(text);
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
