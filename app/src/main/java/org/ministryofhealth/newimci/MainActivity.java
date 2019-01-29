package org.ministryofhealth.newimci;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.KeyElements;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;

    WebView elementsContent;
    DatabaseHandler db;
    KeyElements elements;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("KeyElements", "Key Elements Loaded...");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String key_elements = getResources().getString(R.string.key_elements).toUpperCase();
        getSupportActionBar().setTitle(key_elements);

        db = new DatabaseHandler(this);
        elements = db.getElement();

        fab = (FloatingActionButton) findViewById(R.id.proceed);
        elementsContent = findViewById(R.id.elementsContent);
        if (elements.getElements() != null) {
            Log.d("KeyElements", elements.getElements());
            setupElementsContent();
        }
        else {
            Log.d("KeyElements", "There are no key elements detected");
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainPageActivity.class));
            }
        });
    }

    private void setupElementsContent(){
        new LoadPageTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainactivity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
//            case R.id.action_info:
//                startActivity(new Intent(MainActivity.this, AboutActivity.class));
//                break;

            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                break;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class LoadPageTask extends AsyncTask<Void, Void, Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Getting Key Elements of IMNCI");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d("KeyElements", "Trying to set up elements");
//            Toast.makeText(MainActivity.this, "Please wait... Content Loading...", Toast.LENGTH_SHORT).show();

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            dialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebSettings s = elementsContent.getSettings();
                    s.setUseWideViewPort(false);
                    s.setSupportZoom(true);
                    s.setBuiltInZoomControls(true);
                    s.setDisplayZoomControls(true);
                    s.setJavaScriptEnabled(false);
                    elementsContent.loadData(elements.getElements(), "text/html", "utf-8");
                    Log.d("KeyElements", "Successfully set up the data");
                }
            });

        }
    }
}
