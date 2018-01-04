package org.ministryofhealth.newimci;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;

import org.ministryofhealth.newimci.fragment.ContentUpdate;
import org.ministryofhealth.newimci.fragment.DashboardFragment;
import org.ministryofhealth.newimci.fragment.GlossaryFragment;
import org.ministryofhealth.newimci.fragment.ImageGalleryFragment;
import org.ministryofhealth.newimci.fragment.ReviewFragment;
import org.ministryofhealth.newimci.fragment.SettingsFragment;
import org.ministryofhealth.newimci.service.UpdateService;

import java.lang.reflect.Field;
import java.text.NumberFormat;

public class MainPageActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnPreferenceChangeListener,
        DashboardFragment.OnFragmentInteractionListener, ImageGalleryFragment.OnFragmentInteractionListener, ContentUpdate.OnFragmentInteractionListener, ReviewFragment.OnFragmentInteractionListener, GlossaryFragment.OnFragmentInteractionListener {

    TextView txtDisplayName;
    TextView txtDisplayEmail;
    NavigationView navigationView;

    String TAG_FRAGMENT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        scheduleJob(this);
//        Build.BRAND
//        Build.DEVICE
//        Build.MODEL


//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(sbuilder.toString())
//                .setPositiveButton("OK",null);
//        // Create the AlertDialog object and return it
//        builder.create();
//        builder.show();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
        View header = navigationView.getHeaderView(0);

        txtDisplayName = (TextView) header.findViewById(R.id.display_name);
        txtDisplayEmail = (TextView) header.findViewById(R.id.display_email);

        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(this.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;

        SharedPreferences preference = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
//        String name = preference.getString("display_name", "IMNCI APP");
//        String email = preference.getString("display_email", "Version " + version);

//        txtDisplayName.setText(name);
        txtDisplayEmail.setText("Version " + version);
    }

    public static void scheduleJob(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        Job job = createJob(dispatcher);
        dispatcher.mustSchedule(job);
    }

    private static Job createJob(FirebaseJobDispatcher dispatcher) {
        Job job = dispatcher.newJobBuilder()
                .setLifetime(Lifetime.FOREVER)
                .setService(UpdateService.class)
                .setTag("ContentUpdate")
                .setReplaceCurrent(false)
                .setRecurring(true)
                .setTrigger(Trigger.executionWindow(30, 60))
                .setRetryStrategy(RetryStrategy.DEFAULT_LINEAR)
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .build();

        return job;
    }

    public static Job updateJob(FirebaseJobDispatcher dispatcher){
        Job newJob = dispatcher.newJobBuilder()
                .setReplaceCurrent(true)
                .setService(UpdateService.class)
                .setTag("ContentUpdate")
                .setTrigger(Trigger.executionWindow(30, 60))
                .build();

        return newJob;
    }

    public void cancelJob(Context context){
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        dispatcher.cancelAll();
        dispatcher.cancel("ContentUpdate");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        String title = item.getTitle().toString();


        Fragment newFragment = null;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        getSupportActionBar().setTitle(title);

        switch (id){
            case R.id.nav_elements:
                startActivity(new Intent(MainPageActivity.this, MainActivity.class));
                finish();
                return true;
            case R.id.nav_profile:
                startActivity(new Intent(MainPageActivity.this, SetupActivity.class));
                return true;
            case R.id.nav_dashboard:
                newFragment = new DashboardFragment();
                break;

            case R.id.nav_gallery:
                newFragment = new ImageGalleryFragment();
                break;

            case R.id.nav_settings:
                TAG_FRAGMENT = "settings";
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
                PreferenceFragment settingsFragment = new SettingsFragment();
                getFragmentManager().beginTransaction().replace(R.id.content_frame, settingsFragment).commit();
                getFragmentManager().beginTransaction().addToBackStack(null);
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;

            case R.id.nav_updates:
                newFragment = new ContentUpdate();
                break;

            case R.id.nav_glossary:
                newFragment = new GlossaryFragment();
                break;

            case R.id.nav_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out the IMCI APP at: https://play.google.com/store/apps/details?id=org.ministryofhealth.imci");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;

            case R.id.nav_about:
                startActivity(new Intent(MainPageActivity.this, AboutActivity.class));
                return true;
            case R.id.nav_review:
                newFragment = new ReviewFragment();
                break;
        }

        if(newFragment != null){
            if (TAG_FRAGMENT == "settings") {
                getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(R.id.content_frame)).commit();
            }
            TAG_FRAGMENT = "";
            transaction.replace(R.id.content_frame, newFragment);
            if (id != R.id.nav_dashboard)
                transaction.addToBackStack(null);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPreferenceChanged(String key, String value) {
        switch (key){
            case "display_name":
                txtDisplayName.setText(value);
                break;

            case "display_email":
                txtDisplayEmail.setText(value);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationView.getMenu().getItem(0).setChecked(true);
        onNavigationItemSelected(navigationView.getMenu().getItem(0));
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
