package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Country;

public class UserProfileDetailsActivity extends AppCompatActivity {
    SharedPreferences userprofile;

    DatabaseHandler db;

    TextView txtEmail, txtPhone, txtAgeGroup, txtCountry, txtCounty, txtProfession, txtCadre, txtSector;
    LinearLayout countyLayout, cadreLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_details);

        initComponents();

        userprofile = getSharedPreferences("user_details", Context.MODE_PRIVATE);

        String email = userprofile.getString("email", "");
        String phone = userprofile.getString("phone", "");
        String age_group = userprofile.getString("age_group", "");
        String country_code = userprofile.getString("country", "");
        String county = userprofile.getString("county", "");
        String profession = userprofile.getString("profession", "");
        String cadre = userprofile.getString("cadre", "");
        String sector = userprofile.getString("sector", "");
        if (country_code.equals("")){
           country_code = "KEN";
        }

        Country country = db.getCountry(country_code);
        txtEmail.setText(email);
        txtPhone.setText(phone);
        txtAgeGroup.setText(age_group);
        txtCountry.setText(country.getCountry_name());
        if (county.equals("")){
            countyLayout.setVisibility(View.GONE);
        }else{
            countyLayout.setVisibility(View.VISIBLE);
        }

        if (cadre.equals("")){
            cadreLayout.setVisibility(View.GONE);
        }else{
            cadreLayout.setVisibility(View.VISIBLE);
        }

        txtCounty.setText(county);
        txtProfession.setText(profession);
        txtCadre.setText(cadre);
        txtSector.setText(sector);

        initToolbar();
    }

    private void initComponents(){
        db = new DatabaseHandler(this);

        txtEmail = findViewById(R.id.user_email);
        txtPhone = findViewById(R.id.user_phone);
        txtAgeGroup = findViewById(R.id.user_age);
        txtCountry = findViewById(R.id.user_country);
        txtCounty = findViewById(R.id.user_county);
        txtProfession = findViewById(R.id.user_profession);
        txtCadre = findViewById(R.id.user_cadre);
        txtSector = findViewById(R.id.user_sector);

        countyLayout = findViewById(R.id.county_layout);
        cadreLayout = findViewById(R.id.cadre_layout);
    }

    private void initToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_profile_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }else if(item.getItemId() == R.id.item_edit){
            Intent intent = new Intent("org.ministryofhealth.newimci.SetupActivity");
            intent.putExtra("id", userprofile.getInt("id", 0));
            startActivityForResult(intent, 101);
        }
        return super.onOptionsItemSelected(item);
    }
}
