package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.MyOnScaleGestureListener;
import org.ministryofhealth.newimci.helper.URLImageParser;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.CounselSubContent;
import org.ministryofhealth.newimci.model.CounselTitle;
import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

public class CounselTitleActivity extends AppCompatActivity{
    DatabaseHandler db;
    Context context;
    HtmlTextView contentTextView;
    ScaleGestureDetector scaleGestureDetector;
    RelativeLayout mainLayout;
    WebView webView;
    CounselSubContent subContent;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counsel_title);
        context = this;

        db = new DatabaseHandler(this);
        id = getIntent().getIntExtra("id", 0);
        String type = getIntent().getStringExtra("type");

        mainLayout = (RelativeLayout) findViewById(R.id.layoutCounselTitle);
        webView = findViewById(R.id.content_webview);

        CounselTitle title = new CounselTitle();
        subContent = new CounselSubContent();

        contentTextView = (HtmlTextView) findViewById(R.id.content);

        String content;

        if (type.equals("title")){
            title = db.getCounselTitle(id);
            getSupportActionBar().setTitle(title.getTitle());

            content = title.getContent();
        }else{
            subContent = db.getCounselSubContent(id);
            title = db.getCounselTitle(subContent.getCounsel_titles_id());

            getSupportActionBar().setTitle(subContent.getSub_content_title());

            content = subContent.getContent();
        }

        new LoadContentWebView().execute();

//        if (content != null){
//            contentTextView.setHtml(content,new URLImageParser(contentTextView, this));
//        }else{
//            contentTextView.setText("There is no content to be displayed here");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//                contentTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
//            }
//            contentTextView.setTextColor(Color.parseColor("#B22222"));
//
//        }

        AgeGroup ageGroup = db.getAgeGroup(title.getAge_group_id());


        getSupportActionBar().setSubtitle(ageGroup.getAge_group());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return false;
    }

    class LoadContentWebView extends AsyncTask{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            subContent = db.getCounselSubContent(id);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebSettings s = webView.getSettings();
                    s.setUseWideViewPort(false);
                    s.setSupportZoom(true);
                    s.setBuiltInZoomControls(true);
                    s.setDisplayZoomControls(true);
                    s.setJavaScriptEnabled(true);
                    webView.loadData(subContent.getContent(), "text/html", "utf-8");
                }
            });
        }
    }
}
