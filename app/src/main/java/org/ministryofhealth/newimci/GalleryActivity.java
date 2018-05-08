package org.ministryofhealth.newimci;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.GalleryAilment;

import java.util.ArrayList;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {
    static DatabaseHandler db;
    List<GalleryAilment> ailments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        db = new DatabaseHandler(this);
        ailments = db.getGalleryAilments();
        GalleryAilment allItem = new GalleryAilment();
        allItem.setId(0);
        allItem.setAilment("All");
        ailments.add(0, allItem);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        GalleryAilment[] ailmentsArray = ailments.toArray(new GalleryAilment[ailments.size()]);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
               ailments));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                int ailment_id = ailments.get(position).getId();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(ailment_id))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
            case R.id.action_home:
                startActivity(new Intent(this, MainPageActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends BaseAdapter implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;
        List<GalleryAilment> ailments;

        public MyAdapter(Context context, List<GalleryAilment> ailments){
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
            this.ailments = ailments;
        }


        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }

        @Override
        public int getCount() {
            return ailments.size();
        }

        @Override
        public Object getItem(int i) {
            return ailments.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            }else{
                view = convertView;
            }

            GalleryAilment ailment = (GalleryAilment) getItem(position);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(ailment.getAilment());

            return view;
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        List<org.ministryofhealth.newimci.model.GalleryItem> galleryItems;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_gallery, container, false);

            galleryItems = prepareGalleryItems(getArguments().getInt(ARG_SECTION_NUMBER));
            GridView gridView = (GridView) rootView.findViewById(R.id.gallery_items_grid);
            RelativeLayout emptyLayout = (RelativeLayout) rootView.findViewById(R.id.emptyLayout);
            if (galleryItems.size() > 0) {
                GalleryGridAdapter adapter = new GalleryGridAdapter();

                gridView.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);

                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        org.ministryofhealth.newimci.model.GalleryItem item = galleryItems.get(i);

                        startActivity(new Intent(getContext(), MainPageActivity.class));

                        Toast.makeText(getContext(), item.getItem(), Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                gridView.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
            return rootView;
        }

        List<org.ministryofhealth.newimci.model.GalleryItem> prepareGalleryItems(int ailment_id){

            List<org.ministryofhealth.newimci.model.GalleryItem> items = db.getGalleryItems(ailment_id);

            return items;
        }

        public class GalleryGridAdapter extends BaseAdapter{

            @Override
            public int getCount() {
                return galleryItems.size();
            }

            @Override
            public Object getItem(int i) {
                return galleryItems.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                View row = view;
                ViewHolder holder = null;

                final org.ministryofhealth.newimci.model.GalleryItem item = galleryItems.get(i);
                if (row == null) {
                    LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
                    row = inflater.inflate(R.layout.gallery_item_row, viewGroup, false);
                    holder = new ViewHolder();

                    holder.rowLayout = row.findViewById(R.id.clicker);
                    holder.imgIcon = row.findViewById(R.id.item_icon);
                    holder.titleText = row.findViewById(R.id.title);

                    row.setTag(holder);
                }else{
                    holder = (ViewHolder) row.getTag();
                }

                holder.titleText.setText(item.getItem());

                int imageResource = R.drawable.ic_video_player;

                switch (item.getItem()){
                    case "Job Aid":
                        imageResource = R.drawable.ic_job_aid;
                        break;
                    case "Illustrations":
                        imageResource = R.drawable.ic_illustration;
                        break;
                    case "Video":
                        imageResource = R.drawable.ic_video_1;
                        break;
                    case "Guidelines":
                        imageResource = R.drawable.ic_guidelines;
                        break;
                    case "Tools":
                        imageResource = R.drawable.ic_hammer;
                        break;
                    default:
                        imageResource = R.drawable.ic_video_player;

                }
                holder.imgIcon.setImageResource(imageResource);
                holder.rowLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getContext(), GalleryItemsActivity.class);
                        intent.putExtra("gallery_item_id", item.getId());
                        intent.putExtra("gallery_item", item.getItem());
                        intent.putExtra("ailment_id", getArguments().getInt(ARG_SECTION_NUMBER));
                        startActivity(intent);
                    }
                });
                return row;
            }

            class ViewHolder {
                LinearLayout rowLayout;
                TextView titleText;
                ImageView imgIcon;
            }
        }
    }


    public static class GalleryItem{
        private int imageResource;
        private String title, slug;


        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public int getImageResource() {
            return imageResource;
        }

        public void setImageResource(int imageResource) {
            this.imageResource = imageResource;
        }
    }
}
