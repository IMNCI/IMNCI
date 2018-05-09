package org.ministryofhealth.newimci;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.County;

import java.util.List;

public class CountyActivity extends ListActivity {
    public static String RESULT_COUNTY_NAME = "countyname";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHandler db = new DatabaseHandler(this);
        final List<County> countyList = db.getCounties();
        CountyListAdapter adapter = new CountyListAdapter(this, countyList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                County county = countyList.get(position);
                Intent returnIntent = new Intent();

                returnIntent.putExtra(RESULT_COUNTY_NAME, county.getCounty());

                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public class CountyListAdapter extends ArrayAdapter<County>{
        private Activity context;
        private List<County> counties;
        public CountyListAdapter(@NonNull Activity context, List<County> counties) {
            super(context, R.layout.county_row, counties);
            this.context = context;
            this.counties = counties;
        }

        class ViewHolder{
            protected TextView txtCountyName;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                view = inflater.inflate(R.layout.county_row, null);
                final ViewHolder holder = new ViewHolder();
                holder.txtCountyName = (TextView) view.findViewById(R.id.county_name);
                view.setTag(holder);
            }else{
                view = convertView;
            }

            ViewHolder holder = (ViewHolder) view.getTag();
            holder.txtCountyName.setText(counties.get(position).getCounty());
            return view;
        }
    }
}
