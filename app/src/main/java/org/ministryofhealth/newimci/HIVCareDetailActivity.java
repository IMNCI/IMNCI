package org.ministryofhealth.newimci;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.URLImageParser;
import org.ministryofhealth.newimci.model.HIVCare;
import org.sufficientlysecure.htmltextview.HtmlTextView;

public class HIVCareDetailActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private  WebView webView;
    HIVCare care;
    DatabaseHandler db;
    int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hivcare_detail);

        id = getIntent().getIntExtra("care_id", 0);
        db = new DatabaseHandler(this);
        HtmlTextView htmlTextView = findViewById(R.id.hiv_content);
        webView = findViewById(R.id.hiv_content_webview);

//        htmlTextView.setHtml(care.getContent(),new URLImageParser(htmlTextView, this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        new LoadWebView().execute();
    }

    class LoadWebView extends AsyncTask<String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(HIVCareDetailActivity.this);
            dialog.setMessage("Loading content");
            dialog.setCancelable(false);
            dialog.setIndeterminate(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            care = db.getCare(id);
//            webView.getSettings().setJavaScriptEnabled(true);
//            webView.loadDataWithBaseURL("", care.getContent(), "text/html", "UTF-8", "");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            dialog.dismiss();

            getSupportActionBar().setTitle(care.getTitle());
            getSupportActionBar().setSubtitle(care.getParent());

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebSettings s = webView.getSettings();
                    s.setUseWideViewPort(false);
                    s.setSupportZoom(true);
                    s.setBuiltInZoomControls(true);
                    s.setDisplayZoomControls(true);
                    s.setJavaScriptEnabled(true);
                    webView.loadData(care.getContent(), "text/html", "utf-8");
                }
            });
        }
    }
}
