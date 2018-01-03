package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.County;

import java.lang.reflect.Array;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    Spinner ageSpinner, countySpinner, cadreSpinner, professionSpinner;
    DatabaseHandler db;
    List<County> countyList;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        countySpinner = (Spinner) findViewById(R.id.county_spinner);
        cadreSpinner = (Spinner) findViewById(R.id.cadre_spinner);
        professionSpinner = (Spinner) findViewById(R.id.profession_spinner);
        db = new DatabaseHandler(this);
        context = this;

        countyList = db.getCounties();

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this , R.array.age_bracket, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> cadreAdapter = ArrayAdapter.createFromResource(this , R.array.cadre, android.R.layout.simple_spinner_item);
        ArrayAdapter<CharSequence> professionAdapter = ArrayAdapter.createFromResource(this, R.array.profession, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cadreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        professionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        CountySpinnerAdapter countySpinnerAdapter = new CountySpinnerAdapter();

        ageSpinner.setAdapter(adapter);
        countySpinner.setAdapter(countySpinnerAdapter);
        cadreSpinner.setAdapter(cadreAdapter);
        professionSpinner.setAdapter(professionAdapter);

        final SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        final Boolean page = preference.getBoolean("elements_page", true);

        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.setup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(SetupActivity.this, MainPageActivity.class));
                finish();
                return true;
            case R.id.action_save:
                return true;
        }
        return false;
    }

    public class CountySpinnerAdapter extends BaseAdapter implements SpinnerAdapter{
        LayoutInflater inflater;
        @Override
        public int getCount() {
            return countyList.size();
        }

        @Override
        public Object getItem(int i) {
            return countyList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return countyList.get(i).getId();
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            if (inflater == null)
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null)
                convertView = inflater.inflate(R.layout.county_spinner_layout, null);

            TextView txtCounty = convertView.findViewById(R.id.county);

            County county = (County) getItem(i);

            txtCounty.setText(county.getCounty());
            return convertView;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return super.getDropDownView(position, convertView, parent);
        }
    }
}
