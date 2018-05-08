package org.ministryofhealth.newimci.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.GalleryActivity;
import org.ministryofhealth.newimci.HIVForChildrenActivity;
import org.ministryofhealth.newimci.MainPageActivity;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.fragment.ChildCheckDialogFragment;
import org.ministryofhealth.newimci.fragment.GlossaryFragment;
import org.ministryofhealth.newimci.fragment.ReviewFragment;
import org.ministryofhealth.newimci.model.Menu;

import java.util.List;
import java.util.Objects;

/**
 * Created by chriz on 9/10/2017.
 */

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {
    List<Menu> menuList;
    Activity context;
    Activity a;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public MenuAdapter(List<Menu> menuList, Activity context){
        this.menuList = menuList;
        this.context = context;
    }
    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_menu_row_item, parent, false);
        MenuViewHolder menuViewHolder = new MenuViewHolder(v);
        return menuViewHolder;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, final int position) {
//        Drawable image = context.getResources().getDrawable(menuList.get(position).get_image_resource());

        holder.imgIcon.setImageResource(menuList.get(position).get_image_resource());
        holder.txtMenuTitle.setText(menuList.get(position).get_menu_title());
        holder.colorLayout.setBackgroundColor(Color.parseColor(menuList.get(position).get_menu_color()));

        final Menu menu = menuList.get(position);

        holder.menuLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(menu);
            }
        });

        holder.colorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click(menu);
            }
        });
    }

    private void click(Menu menu){
        if (menu.is_alert()) {
            ChildCheckDialogFragment frag = ChildCheckDialogFragment.newInstance(menu.get_menu_title(), menu.get_menu_slug());
            frag.show(context.getFragmentManager(), "dialog");
        }else{
            MainPageActivity activity = (MainPageActivity)context;
            Fragment newFragment = null;
            FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
            transaction.addToBackStack(null);

            if (menu.get_menu_slug().equals("hiv_care")){
                context.startActivity(new Intent(context, HIVForChildrenActivity.class));
            }else if(menu.get_menu_slug().equals("gallery")){
                context.startActivity(new Intent(context, GalleryActivity.class));
            }else if(menu.get_menu_slug().equals("glossary")){
                activity.getSupportActionBar().setTitle("Glossary");
                newFragment = new GlossaryFragment();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.commit();
            }else if(menu.get_menu_slug().equals("report_issue")){
                activity.getSupportActionBar().setTitle("Report Issue");
                newFragment = new ReviewFragment();
                transaction.replace(R.id.content_frame, newFragment);
                transaction.commit();
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.menuList.size();
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder{
        LinearLayout menuLayout;
        RelativeLayout colorLayout;
        TextView txtMenuTitle;
        ImageView imgIcon;

        public MenuViewHolder(View itemView) {
            super(itemView);

            menuLayout = (LinearLayout) itemView.findViewById(R.id.menu_layout);
            colorLayout = (RelativeLayout) itemView.findViewById(R.id.colorLayout);
            txtMenuTitle = (TextView) itemView.findViewById(R.id.menu_text);
            imgIcon = (ImageView) itemView.findViewById(R.id.menu_icon);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
