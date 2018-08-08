package org.ministryofhealth.newimci;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String key_elements = getResources().getString(R.string.key_elements).toUpperCase();
        getSupportActionBar().setTitle(key_elements);

        db = new DatabaseHandler(this);
        elements = db.getElement();

        fab = (FloatingActionButton) findViewById(R.id.proceed);
        elementsContent = findViewById(R.id.elementsContent);

        setupElementsContent();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MainPageActivity.class));
            }
        });
    }

    private void setupElementsContent(){
        Toast.makeText(this, "Please wait... Content Loading...", Toast.LENGTH_SHORT).show();
        WebSettings s = elementsContent.getSettings();
        s.setUseWideViewPort(false);
        s.setSupportZoom(true);
        s.setBuiltInZoomControls(true);
        s.setDisplayZoomControls(true);
        s.setJavaScriptEnabled(false);
        elementsContent.loadData(elements.getElements(), "text/html", "utf-8");
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
}
