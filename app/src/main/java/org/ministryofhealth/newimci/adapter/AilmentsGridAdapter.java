package org.ministryofhealth.newimci.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.AilmentFollowUpCareActivity;
import org.ministryofhealth.newimci.FollowUpCareAilmentsActivity;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by chriz on 9/13/2017.
 */

public class AilmentsGridAdapter extends BaseAdapter {
    private Context context;
    private Activity activity;
    private int resource;
    private ArrayList<Ailment> ailments;
    AilmentsGridAdapter adapter = this;
    String[] colors;

    public AilmentsGridAdapter(@NonNull Context context, @LayoutRes int resource, ArrayList<Ailment> data, Activity activity){
        this.context = context;
        this.resource = resource;
        this.ailments = data;
        this.activity = activity;
        this.colors = context.getResources().getStringArray(R.array.menu_colors);
    }
    @Override
    public int getCount() {
        return ailments.size();
    }

    @Override
    public Object getItem(int position) {
        return ailments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return ailments.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder = null;

        final Ailment ailment = ailments.get(position);

        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
            holder = new ViewHolder();
            holder.rowLayout = (RelativeLayout) row.findViewById(R.id.grid_item_layout);
            holder.ailmentText = (TextView) row.findViewById(R.id.ailment_text);
            row.setTag(holder);
        }else{
            holder = (ViewHolder) row.getTag();
        }

        String color = colors[new Random().nextInt(colors.length)];

        holder.rowLayout.setBackgroundColor(Color.parseColor(color));
        holder.ailmentText.setText(ailment.getAilment().toString());

        DatabaseHandler db = new DatabaseHandler(context);
        final AgeGroup ageGroup = db.getAgeGroup(ailment.getAge_group_id());

        holder.rowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AilmentFollowUpCareActivity.class);
                intent.putExtra("ailment_id", ailment.getId());
                intent.putExtra("age", ageGroup.getAge_group());
                intent.putExtra("age_group_id", ageGroup.getId());
                context.startActivity(intent);
            }
        });
        return row;
    }

    static class ViewHolder {
        RelativeLayout rowLayout;
        TextView ailmentText;
    }
}
