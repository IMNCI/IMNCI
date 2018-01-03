package org.ministryofhealth.newimci;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.fragment.SlideshowDialogFragment;
import org.ministryofhealth.newimci.model.HIVCare;

import java.io.Serializable;
import java.util.List;

public class HIVForChildrenActivity extends AppCompatActivity {
    DatabaseHandler db;
    List<HIVCare> hivCareList;
    RecyclerView recyclerView;
    LinearLayout linearLayout;

    Context context = this;
    protected RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hivfor_children);

        db = new DatabaseHandler(this);
        hivCareList = db.getHIVCare();

        recyclerView = (RecyclerView) findViewById(R.id.hiv_care_recycler_view);
        linearLayout = (LinearLayout) findViewById(R.id.no_content);

        getSupportActionBar().setTitle("HIV Care for Children");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (hivCareList.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            HIVForChildrenAdapter adapter = new HIVForChildrenAdapter();
            mLayoutManager = new LinearLayoutManager(this);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(adapter);

            linearLayout.setVisibility(View.GONE);
        }else{
            recyclerView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startActivity(new Intent(HIVForChildrenActivity.this, MainPageActivity.class));
                finish();
                break;
        }
        return false;
    }

    public class HIVForChildrenAdapter extends RecyclerView.Adapter<HIVForChildrenAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hiv_care_row_item, parent, false);
            MyViewHolder holder = new MyViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            try {
                String thumbnail = hivCareList.get(position).getThumbnail();
                thumbnail = thumbnail.replace("data:image/png;base64,", "");
                byte[] decodedString = Base64.decode(thumbnail, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

//                holder.imgView.setImageBitmap(decodedByte.createScaledBitmap(decodedByte, decodedByte.getWidth() / 3, decodedByte.getHeight() / 3, false));
                Glide.with(context)
                        .load(decodedString)
                        .asBitmap()
                        .thumbnail(0.5f)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.imgView);
                holder.txtTitle.setText(hivCareList.get(position).getTitle());

                holder.imgView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();

                        bundle.putSerializable("images", (Serializable) hivCareList);
                        bundle.putInt("position", position);

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        SlideshowDialogFragment newFragment = SlideshowDialogFragment.newInstance();
                        newFragment.setArguments(bundle);
                        newFragment.show(ft, "slideshow");
                    }
                });
            }catch(Exception ex){
                Log.e("HIVCARE", ex.toString());
            }
        }

        @Override
        public int getItemCount() {
            return hivCareList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imgView;
            TextView txtTitle, txtTitleID;
            public MyViewHolder(View itemView){
                super(itemView);

                imgView = (ImageView) itemView.findViewById(R.id.thumb);
                txtTitle = (TextView) itemView.findViewById(R.id.title);
                txtTitleID = (TextView) itemView.findViewById(R.id.care_id);
            }
        }
    }
}
