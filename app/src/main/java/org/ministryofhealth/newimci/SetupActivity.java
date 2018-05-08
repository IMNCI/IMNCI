package org.ministryofhealth.newimci;

import android.app.ProgressDialog;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.iid.FirebaseInstanceId;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.County;
import org.ministryofhealth.newimci.model.UserProfile;
import org.ministryofhealth.newimci.server.Service.UserProfileService;

import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SetupActivity extends AppCompatActivity {
    Spinner ageSpinner, countySpinner, cadreSpinner, professionSpinner;
    EditText etxEmail, etxPhone;
    RadioGroup rgGender, rgSector;
    DatabaseHandler db;
    List<County> countyList;
    Context context;
    TextView txtSkip;
    Button btnSubmit;
    Boolean page;
    int user_id;
    AwesomeValidation mAwesomeValidation;
    SharedPreferences pref;
    TableLayout enteredDataTable, formTable;
    TableRow cadreRow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        pref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        user_id = pref.getInt("id", 0);

        TextView informationText = (TextView) findViewById(R.id.information);

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
        enteredDataTable = (TableLayout) findViewById(R.id.entered_data);
        formTable = (TableLayout) findViewById(R.id.form);
        cadreRow = (TableRow) findViewById(R.id.cadre_row);
        db = new DatabaseHandler(this);
        context = this;

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAwesomeValidation.addValidation(this, R.id.emailAddress, Patterns.EMAIL_ADDRESS, R.string.error_email);
        mAwesomeValidation.addValidation(this, R.id.phonenumber, Patterns.PHONE, R.string.error_phone);

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
        page = preference.getBoolean("elements_page", true);

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

        professionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0 || i == 3){
                    cadreRow.setVisibility(View.GONE);
                }else{
                    cadreRow.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if (user_id != 0){
            TextView txtEmail, txtPhone, txtGender, txtAgeGroup, txtCounty, txtProfession, txtCadre, txtSector;

            informationText.setVisibility(View.GONE);
            txtSkip.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.GONE);
            enteredDataTable.setVisibility(View.VISIBLE);
            formTable.setVisibility(View.GONE);

            txtEmail = (TextView) findViewById(R.id.existent_email);
            txtPhone = (TextView) findViewById(R.id.existent_phone);
            txtGender = (TextView) findViewById(R.id.existent_gender);
            txtAgeGroup = (TextView) findViewById(R.id.existent_age_group);
            txtCounty = (TextView) findViewById(R.id.existent_county);
            txtProfession = (TextView) findViewById(R.id.existent_profession);
            txtCadre = (TextView) findViewById(R.id.existent_cadre);
            txtSector = (TextView) findViewById(R.id.existent_sector);

            txtEmail.setText(pref.getString("email", ""));
            txtPhone.setText(pref.getString("phone", ""));
            txtGender.setText(pref.getString("gender", ""));
            txtAgeGroup.setText(pref.getString("age_group", ""));
            txtCounty.setText(pref.getString("county", ""));
            txtProfession.setText(pref.getString("profession", ""));
            txtCadre.setText(pref.getString("cadre", "N/A"));
            txtSector.setText(pref.getString("sector", ""));
        }else{
            enteredDataTable.setVisibility(View.GONE);
            formTable.setVisibility(View.VISIBLE);
        }
    }

    public void proceed(){
        if (page) {
            startActivity(new Intent(context, MainActivity.class));
        }else{
            startActivity(new Intent(context, MainPageActivity.class));
        }
        finish();
    }

    public void submitProfile(){
        String email, phone, gender, age_group, county, profession, cadre, sector;
        int selected_gender = rgGender.getCheckedRadioButtonId();
        int selected_sector = rgSector.getCheckedRadioButtonId();
        int selected_age_group = ageSpinner.getSelectedItemPosition();
        int selected_county = countySpinner.getSelectedItemPosition();
        int selected_profession = professionSpinner.getSelectedItemPosition();
        int selected_cadre = cadreSpinner.getSelectedItemPosition();

        if (mAwesomeValidation.validate()){
            email = etxEmail.getText().toString();
            phone = etxPhone.getText().toString();

            if (selected_gender == -1 || selected_sector == -1 || selected_age_group == 0 || selected_county == 0 || selected_profession == 0 || (selected_profession != 0 && selected_profession != 3 && selected_cadre == 0)){
                Toast.makeText(context, "Please fill in all the fields", Toast.LENGTH_LONG).show();
            }else{
                RadioButton genderRadioButton = (RadioButton) findViewById(selected_gender);
                RadioButton sectorRadioButton = (RadioButton) findViewById(selected_sector);
                gender = genderRadioButton.getText().toString();
                sector = sectorRadioButton.getText().toString();
                age_group = ageSpinner.getSelectedItem().toString();
                county = countyList.get(selected_county).getCounty();
                profession = professionSpinner.getSelectedItem().toString();
                if (selected_cadre != 0)
                    cadre = cadreSpinner.getSelectedItem().toString();
                else
                    cadre = "";

                UserProfile profile = new UserProfile();

                profile.setEmail(email);
                profile.setAge_group(age_group);
                profile.setCadre(cadre);
                profile.setCounty(county);
                profile.setGender(gender);
                profile.setPhone(phone);
                profile.setProfession(profession);
                profile.setSector(sector);
                profile.setPhone_id(FirebaseInstanceId.getInstance().getToken());

                try{
                    final ProgressDialog progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Uploading data");
                    progressDialog.show();
                    Retrofit retrofit = RetrofitHelper.getInstance().createHelper();
                    UserProfileService userProfileClient = retrofit.create(UserProfileService.class);
                    Call<UserProfile> userProfileCall = userProfileClient.addProfile(profile);

                    userProfileCall.enqueue(new Callback<UserProfile>() {
                        @Override
                        public void onResponse(Call<UserProfile> call, Response<UserProfile> response) {
                            progressDialog.dismiss();
                            UserProfile savedProfile = response.body();
                            SharedPreferences pref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putInt("id", savedProfile.getId());
                            editor.putString("email", savedProfile.getEmail());
                            editor.putString("phone", savedProfile.getPhone());
                            editor.putString("gender", savedProfile.getGender());
                            editor.putString("age_group", savedProfile.getAge_group());
                            editor.putString("county", savedProfile.getCounty());
                            editor.putString("profession", savedProfile.getProfession());
                            editor.putString("cadre", savedProfile.getCadre());
                            editor.putString("sector", savedProfile.getSector());
                            editor.commit();
                            SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor prefEditor = preference.edit();
                            prefEditor.putBoolean("setup_page", false);
                            prefEditor.commit();

                            proceed();
                        }

                        @Override
                        public void onFailure(Call<UserProfile> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });
                }catch (Exception ex){
                    Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        proceed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.setup_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                proceed();
                return true;
//            case R.id.action_save:
//                submitProfile();
//                return true;
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
