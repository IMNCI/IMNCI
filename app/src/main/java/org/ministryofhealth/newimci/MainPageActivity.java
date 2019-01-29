package org.ministryofhealth.newimci;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.fragment.ContentUpdate;
import org.ministryofhealth.newimci.fragment.DashboardFragment;
import org.ministryofhealth.newimci.fragment.GlossaryFragment;
import org.ministryofhealth.newimci.fragment.ImageGalleryFragment;
import org.ministryofhealth.newimci.fragment.NotificationsFragment;
import org.ministryofhealth.newimci.fragment.OtherAppsFragment;
import org.ministryofhealth.newimci.fragment.ReviewFragment;
import org.ministryofhealth.newimci.fragment.SettingsFragment;
import org.ministryofhealth.newimci.helper.TaskCompleted;
import org.ministryofhealth.newimci.model.AppUser;
import org.ministryofhealth.newimci.service.UpdateService;

import java.lang.reflect.Field;
import java.text.NumberFormat;

public class MainPageActivity extends AppCompatActivity
        implements TaskCompleted, NavigationView.OnNavigationItemSelectedListener,
        SettingsFragment.OnPreferenceChangeListener,
        DashboardFragment.OnFragmentInteractionListener, ImageGalleryFragment.OnFragmentInteractionListener, ContentUpdate.OnFragmentInteractionListener, ReviewFragment.OnFragmentInteractionListener, GlossaryFragment.OnFragmentInteractionListener, OtherAppsFragment.OnFragmentInteractionListener, NotificationsFragment.OnFragmentInteractionListener {

    TextView txtDisplayName;
    TextView txtDisplayEmail;
    NavigationView navigationView;

    DatabaseHandler db;
    SharedPreferences pref;

    String TAG_FRAGMENT = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        Log.d("FirebaseID", FirebaseInstanceId.getInstance().getToken());

        scheduleJob(this);

        db = new DatabaseHandler(this);

        AppUser appUser = db.getUser();
        pref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
        int id = pref.getInt("id", 0);

//        Log.d("FIREBASEID", FirebaseInstanceId.getInstance().getToken());

        if (id == 0 && appUser.getId() != 0){
            android.support.v7.app.AlertDialog alertDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
            alertDialog.setTitle("User Profile");
            alertDialog.setMessage("Hello there. Welcome to " + getString(R.string.app_name) + ". Please take a minute to fill in your profile.");
            alertDialog.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(MainPageActivity.this, SetupActivity.class));
                        }
                    });
            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Not Now", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alertDialog.show();
        }


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
        txtDisplayEmail.setText("Version " + version);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainpage_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
        return true;
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
                SharedPreferences pref = getSharedPreferences("user_details", Context.MODE_PRIVATE);
                if (pref.getBoolean("uploaded", false)){
                    startActivity(new Intent(MainPageActivity.this, UserProfileDetailsActivity.class));
                }else{
                    startActivity(new Intent(MainPageActivity.this, SetupActivity.class));
                }

                return true;
            case R.id.nav_dashboard:
                newFragment = new DashboardFragment();
                break;

            case R.id.nav_notifications:
                // startActivity(new Intent(MainPageActivity.this, NotificationActivity.class));
                newFragment = new NotificationsFragment();
                break;

//            case R.id.nav_gallery:
//                newFragment = new ImageGalleryFragment();
//                break;

//            case R.id.nav_settings:
//                TAG_FRAGMENT = "settings";
//                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.content_frame)).commit();
//                PreferenceFragment settingsFragment = new SettingsFragment();
//                getFragmentManager().beginTransaction().replace(R.id.content_frame, settingsFragment).commit();
//                getFragmentManager().beginTransaction().addToBackStack(null);
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.closeDrawer(GravityCompat.START);
//                return true;
            case R.id.nav_other_apps:
                newFragment = new OtherAppsFragment();
                break;
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
                        "Hey check out the IMCI APP at: https://play.google.com/store/apps/details?id=org.ministryofhealth.newimci");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;



//            case R.id.nav_about:
//                startActivity(new Intent(MainPageActivity.this, AboutActivity.class));
//                return true;
            case R.id.nav_review:
                newFragment = new ReviewFragment();
                break;
        }

        if(newFragment != null){
            if (TAG_FRAGMENT.equals("settings")) {
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

    @Override
    public void onTaskComplete(Boolean status) {
        if(status) {
            MenuItem item = navigationView.getMenu().findItem(R.id.nav_updates);
            onNavigationItemSelected(item);
        }
    }
}
