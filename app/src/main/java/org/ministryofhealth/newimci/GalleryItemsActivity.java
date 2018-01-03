package org.ministryofhealth.newimci;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.config.Constants;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Gallery;
import org.ministryofhealth.newimci.server.Service.FileDownloadClient;
import org.ministryofhealth.newimci.util.DividerItemDecoration;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class GalleryItemsActivity extends AppCompatActivity {
    private static final String TAG = "WriteFile";
    ProgressBar pb;
    Dialog dialog;
    RecyclerView recyclerView;
    int ailment_id, item_id;
    DatabaseHandler db;
    int totalSize = 0;
    int downloadedSize = 0;
    private ProgressDialog pDialog;
    public static final int progress_bar_type = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_items);

        db = new DatabaseHandler(this);

        item_id = getIntent().getIntExtra("gallery_item_id", 0);
        ailment_id = getIntent().getIntExtra("ailment_id", 0);

        List<Gallery> galleryList = db.getGallery(ailment_id, item_id);

        recyclerView = (RecyclerView) findViewById(R.id.gallery_items);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        GalleryAdapter adapter = new GalleryAdapter(galleryList);

        recyclerView.setAdapter(adapter);

        getSupportActionBar().setTitle(getIntent().getStringExtra("gallery_item"));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
        }
        return true;
    }

    class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{
        List<Gallery> galleryList;
        public GalleryAdapter(List<Gallery> galleryList){
            this.galleryList = galleryList;
        }

        @Override
        public GalleryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.gallery_row_item, parent, false);
            return new GalleryViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GalleryViewHolder holder, int position) {
            try {
                final Gallery gallery = galleryList.get(position);
                int icon;
                switch(gallery.getType()){
                    case "Image":
                        icon = R.drawable.ic_jpg;
                        break;

                    case "PDF":
                        icon = R.drawable.ic_pdf;
                        break;

                    case "Spreadsheet":
                        icon = R.drawable.ic_xls;
                        break;

                    case "Word Document":
                        icon = R.drawable.ic_doc;
                        break;
                    case "Presentation":
                        icon = R.drawable.ic_ppt;
                        break;
                    default:
                        icon = R.drawable.ic_jpg;
                }
                holder.iconImageView.setImageResource(icon);
                holder.txtFileName.setText(gallery.getTitle());
                String size = getFileSize(Integer.parseInt(gallery.getSize()));
                holder.txtFileSize.setText(size);
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        if (gallery.getType().equals("Image")){
//                            Intent intent = new Intent(GalleryItemsActivity.this, ViewGalleryItemActivity.class);
//                            intent.putExtra("gallery_id", gallery.getId());
//                            startActivity(intent);
//                        }else{
                            downloadFile(gallery);
//                        }
                    }
                });

            }catch (Exception ex){
                Toast.makeText(GalleryItemsActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public int getItemCount() {
            return galleryList.size();
        }

        class GalleryViewHolder extends RecyclerView.ViewHolder {

            ImageView iconImageView;
            TextView txtFileName, txtFileSize, txtFileTime;
            LinearLayout layout;

            public GalleryViewHolder(View itemView) {
                super(itemView);

                iconImageView = (ImageView) itemView.findViewById(R.id.gallery_icon);
                txtFileName = (TextView) itemView.findViewById(R.id.title);
                txtFileSize = (TextView) itemView.findViewById(R.id.size);
                layout = (LinearLayout) itemView.findViewById(R.id.gallery_item_layout);
            }
        }
    }

    public void downloadFile(Gallery gallery){
        String file_name = gallery.getLink().replace("gallery/", "");

//        String filePath = Environment.getExternalStorageDirectory().toString() + file_name;
        File file = new File(getExternalFilesDir(null) + File.separator + file_name);

        if (file.exists()) {
//            Uri path = Uri.fromFile(file);
            Uri path = FileProvider.getUriForFile(this, "org.ministryofhealth.newimci.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(path, gallery.getMime());
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }else{
            new DownloadFileFromURL().execute(String.valueOf(gallery.getId()), file_name);
        }
    }

    public static String getFileSize(long size) {
        if (size <= 0)
            return "0";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    class DownloadFileFromURL extends AsyncTask<String, String, Wrapper> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected Wrapper doInBackground(String... f_url) {
            int count;
            String f_name= "";
            try {
                URL url = new URL(Constants.BASE_URL + "api/files/get/" + f_url[0]);
                f_name = f_url[1];
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream(getExternalFilesDir(null) + File.separator  + f_name);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress(""+(int)((total*100)/lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            Wrapper w = new Wrapper();
            w.file_name = f_name;
            return w;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(Wrapper w) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String filePath = getExternalFilesDir(null) + File.separator + w.file_name;

            // setting downloaded into image view
            String extension = MimeTypeMap.getFileExtensionFromUrl(filePath);
            String type = "";
            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }

            File file = new File(getExternalFilesDir(null), w.file_name);
//            Uri path = Uri.fromFile(file);
            Uri path = FileProvider.getUriForFile(GalleryItemsActivity.this, "org.ministryofhealth.newimci.fileprovider", file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(path, type);
            try {
                startActivity(intent);
            }
            catch (ActivityNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }

    }

    public class Wrapper {
        public String file_name;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

}
