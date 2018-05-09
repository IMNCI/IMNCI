package org.ministryofhealth.newimci;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Preconditions;

import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.GlideApp;
import org.ministryofhealth.newimci.helper.SvgSoftwareLayerSetter;
import org.ministryofhealth.newimci.model.Country;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class CountryActivity extends ListActivity {
    public static String RESULT_COUNTRYCODE = "countrycode";
    public static String RESULT_COUNTRYNAME = "countryname";
    public List<Country> countryList;
    private RequestBuilder<PictureDrawable> requestBuilder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHandler db = new DatabaseHandler(this);
        requestBuilder = GlideApp.with(this)
                .as(PictureDrawable.class)
                .placeholder(R.drawable.ic_world_grid)
                .error(R.drawable.ic_warning)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
        countryList = db.getCountries();
        CountryListAdapter adapter = new CountryListAdapter(this, countryList);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Country c = countryList.get(position);
                Intent returnIntent = new Intent();
                returnIntent.putExtra(RESULT_COUNTRYCODE, c.getCountry_code());
                returnIntent.putExtra(RESULT_COUNTRYNAME, c.getCountry_name());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    public class CountryListAdapter extends ArrayAdapter<Country>{
        private List<Country> countries = new ArrayList<>();
        private Activity context;
        public CountryListAdapter(@NonNull Activity context, List<Country> countries) {
            super(context, R.layout.country_row, countries);
            this.context = context;
            this.countries = countries;
        }

        class ViewHolder{
            protected TextView txtCountryName;
            protected ImageView imgFlag;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View view = null;
            if (convertView == null){
                LayoutInflater inflater = context.getLayoutInflater();
                view = inflater.inflate(R.layout.country_row, null);
                final ViewHolder holder = new ViewHolder();
                holder.txtCountryName = (TextView) view.findViewById(R.id.countryname);
                holder.imgFlag = (ImageView) view.findViewById(R.id.flag);
                view.setTag(holder);
            }else{
                view = convertView;
            }

            ViewHolder holder =(ViewHolder) view.getTag();
            holder.txtCountryName.setText(countries.get(position).getCountry_name());

            Uri uri = Uri.parse(countries.get(position).getCountry_flag());
            requestBuilder.load(uri).into(holder.imgFlag);
            return view;
        }
    }
}
