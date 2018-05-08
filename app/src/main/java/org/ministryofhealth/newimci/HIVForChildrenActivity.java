package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.ministryofhealth.newimci.adapter.ExpandableListAdapter;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.fragment.SlideshowDialogFragment;
import org.ministryofhealth.newimci.model.HIVCare;
import org.ministryofhealth.newimci.model.HIVParent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HIVForChildrenActivity extends AppCompatActivity {
    DatabaseHandler db;
    List<HIVCare> hivCareList;
    List<HIVParent> hivParents;
    RecyclerView recyclerView;
    LinearLayout linearLayout;
    ExpandableListView expandableListView;

    List<String> listDataHeader = new ArrayList<String>();
    HashMap<String, List<HIVCare>> listDataChild = new HashMap<String, List<HIVCare>>();

    HIVForChildrenExpandableAdapter adapter;

    Context context = this;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hivfor_children);

        db = new DatabaseHandler(this);
        hivParents = db.getHIVParents();
        hivCareList = db.getHIVCare();

        cleanListViewData();

        recyclerView = (RecyclerView) findViewById(R.id.hiv_care_recycler_view);
        linearLayout = (LinearLayout) findViewById(R.id.no_content);
        expandableListView= (ExpandableListView) findViewById(R.id.hiv_care_expandable);

        adapter = new HIVForChildrenExpandableAdapter();

        expandableListView.setAdapter(adapter);

        getSupportActionBar().setTitle("HIV Care for Children");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hivCareList.size() > 0){
            expandableListView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);

            if (listDataHeader.size() > 0){
                for (int i = 0; i < listDataHeader.size(); i++){
                    expandableListView.expandGroup(i);
                }
            }

            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Intent intent = new Intent(getApplicationContext(), HIVCareDetailActivity.class);
                    intent.putExtra("care_id", listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition).getId());
                    startActivity(intent);
                    return false;
                }
            });
        }else{
            expandableListView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void cleanListViewData() {
        for (HIVParent parent:
             hivParents) {
            hivCareList = db.getHIVCare(parent.getParent());
            if (hivCareList.size() > 0) {
                listDataHeader.add(parent.getParent());
                listDataChild.put(parent.getParent(), hivCareList);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(HIVForChildrenActivity.this, MainPageActivity.class));
                finish();
                break;
        }
        return false;
    }

    public class HIVForChildrenAdapter extends RecyclerView.Adapter<HIVForChildrenAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiv_care_row_item, parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try {
                String thumbnail = hivCareList.get(position).getThumbnail();
                thumbnail = thumbnail.replace("data:image/png;base64,", "");
                byte[] decodedString = Base64.decode(thumbnail, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

//                holder.imgView.setImageBitmap(decodedByte.createScaledBitmap(decodedByte, decodedByte.getWidth() / 3, decodedByte.getHeight() / 3, false));
                Glide.with(context)
                        .asBitmap()
                        .load(decodedString)
                        .thumbnail(0.5f)
                        .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                        .into(holder.imgView);
                holder.txtTitle.setText(hivCareList.get(position).getTitle());

                holder.imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("images", (Serializable) hivCareList);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });
            }catch(Exception ex){
                Log.e("HIVCARE", ex.toString());
            }
        }

        @Override
        public int getItemCount() {
            return hivCareList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imgView;
            TextView txtTitle, txtTitleID;
            public MyViewHolder(View itemView){
                super(itemView);

                imgView = (ImageView) itemView.findViewById(R.id.thumb);
                txtTitle = (TextView) itemView.findViewById(R.id.title);
                txtTitleID = (TextView) itemView.findViewById(R.id.care_id);
            }
        }
    }

    public class HIVForChildrenExpandableAdapter extends BaseExpandableListAdapter{

        @Override
        public int getGroupCount() {
            return listDataHeader.size();
        }

        @Override
        public int getChildrenCount(int i) {
            return listDataChild.get(listDataHeader.get(i)).size();
        }

        @Override
        public Object getGroup(int i) {
            return listDataHeader.get(i);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int i) {
            return i;
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
                LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_group, null);
            }

            TextView lblListHeader = (TextView) convertView
                    .findViewById(R.id.lblListHeader);
            lblListHeader.setTypeface(null, Typeface.BOLD);
            lblListHeader.setText(headerTitle);

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            HIVCare care = (HIVCare) getChild(groupPosition, childPosition);
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.list_item_assess_classify, null);
            }
            TextView txtCareID = (TextView) convertView.findViewById(R.id.assessment_id);
            ImageView handView = (ImageView) convertView.findViewById(R.id.hand_image);
            handView.setVisibility(View.VISIBLE);

            TextView txtListChild = (TextView) convertView
                    .findViewById(R.id.assessment_title);

            txtCareID.setText(String.valueOf(care.getId()));
            txtListChild.setText(care.getTitle());
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int i, int i1) {
            return true;
        }
    }
}
