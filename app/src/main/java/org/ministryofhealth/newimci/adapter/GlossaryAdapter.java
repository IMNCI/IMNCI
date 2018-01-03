package org.ministryofhealth.newimci.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.model.Glossary;

import java.util.List;

/**
 * Created by chriz on 10/21/2017.
 */

public class GlossaryAdapter extends RecyclerView.Adapter<GlossaryAdapter.MyViewHolder>{
    private List<Glossary> glossaryList;
    private Context context;

    public GlossaryAdapter(Context c, List<Glossary> g){
        this.glossaryList = g;
        this.context = c;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.glossary_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glossary glossary = glossaryList.get(position);

        holder.txtAcronym.setText(glossary.getAcronym());
        holder.txtDescription.setText(glossary.getDescription());
    }

    @Override
    public int getItemCount() {
        return glossaryList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView txtAcronym, txtDescription;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtAcronym = (TextView) itemView.findViewById(R.id.acronym);
            txtDescription = (TextView) itemView.findViewById(R.id.description);
        }
    }
}
