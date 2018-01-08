package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.County;
import org.ministryofhealth.newimci.model.UserProfile;

import java.lang.reflect.Array;
import java.util.List;

public class SetupActivity extends AppCompatActivity {
    Spinner ageSpinner, countySpinner, cadreSpinner, professionSpinner;
    EditText etxEmail, etxPhone;
    RadioGroup rgGender, rgSector;
    DatabaseHandler db;
    List<County> countyList;
    Context context;
    TextView txtSkip;
    Button btnSubmit;
    AwesomeValidation mAwesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        etxEmail = (EditText) findViewById(R.id.emailAddress);
        etxPhone = (EditText) findViewById(R.id.phonenumber);
        rgGender = (RadioGroup) findViewById(R.id.gender);
        rgSector = (RadioGroup) findViewById(R.id.sector);
        ageSpinner = (Spinner) findViewById(R.id.age_spinner);
        countySpinner = (Spinner) findViewById(R.id.county_spinner);
        cadreSpinner = (Spinner) findViewById(R.id.cadre_spinner);
        professionSpinner = (Spinner) findViewById(R.id.profession_spinner);
        txtSkip = (TextView) findViewById(R.id.skip_now);
        btnSubmit = (Button) findViewById(R.id.submit);
        db = new DatabaseHandler(this);
        context = this;

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAwesomeValidation.addValidation(this, R.id.emailAddress, Patterns.EMAIL_ADDRESS, R.string.error_email);
        mAwesomeValidation.addValidation(this, R.id.phonenumber, Patterns.PHONE, R.string.error_phone);
        countySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(context, "Please select your county", Toast.LENGTH_SHORT).show();
            }
        });

        countyList = db.getCounties();
        County emptyCounty = new County();
        emptyCounty.setId(0);
        emptyCounty.setCounty("Select your County");
        countyList.add(0, emptyCounty);

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

        txtSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceed();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitProfile();
            }
        });
    }

    public void proceed(){
        startActivity(new Intent(SetupActivity.this, MainPageActivity.class));
        finish();
    }

    public void submitProfile(){
        if (mAwesomeValidation.validate()){

        }
        UserProfile profile = new UserProfile();
        Toast.makeText(context, "This feature is still under development. Use the back button to proceed", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                proceed();
                return true;
            case R.id.action_save:
                submitProfile();
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
