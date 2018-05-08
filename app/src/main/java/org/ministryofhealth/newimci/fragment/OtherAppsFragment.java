package org.ministryofhealth.newimci.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.App;
import org.ministryofhealth.newimci.server.Service.OtherAppService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OtherAppsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OtherAppsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OtherAppsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View emptyLayout, fetchLayout, errorLayout;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewlayoutManager;
    OtherAppsAdapter adapter;
    View parentLayout;

    private OnFragmentInteractionListener mListener;

    public OtherAppsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OtherAppsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OtherAppsFragment newInstance(String param1, String param2) {
        OtherAppsFragment fragment = new OtherAppsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_other_apps, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        emptyLayout = rootView.findViewById(R.id.empty_layout);
        fetchLayout = rootView.findViewById(R.id.progress_layout);
        errorLayout = rootView.findViewById(R.id.error_layout);

        recyclerViewlayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(recyclerViewlayoutManager);

        parentLayout = rootView.findViewById(R.id.other_apps_layout);

        getOtherApps(this);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public List<App> getOtherApps(final OtherAppsFragment context){
        fetchLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        List<App> otherApps = new ArrayList<App>();

        Retrofit retrofit = RetrofitHelper.getInstance().createHelper(true);

        OtherAppService otherAppService = retrofit.create(OtherAppService.class);
        Call<List<App>> appCall = otherAppService.get(getContext().getPackageName());

        appCall.enqueue(new Callback<List<App>>() {
            @Override
            public void onResponse(Call<List<App>> call, Response<List<App>> response) {
                Log.d("OTHERAPPS", response.raw().toString()+ "");
                fetchLayout.setVisibility(View.GONE);
                if (response.body().size() > 0){
                    recyclerView.setVisibility(View.VISIBLE);
                }else{
                    emptyLayout.setVisibility(View.VISIBLE);
                }
                adapter = new OtherAppsAdapter(response.body(), context);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<App>> call, Throwable t) {
                Log.e("OTHERAPPS", t.getMessage());
                fetchLayout.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                Snackbar.make(parentLayout, "Please change your connection and try again", Snackbar.LENGTH_INDEFINITE)
                        .setAction("RETRY", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getOtherApps(context);
                            }
                        })
                        .show();
                t.printStackTrace();
            }
        });
        return otherApps;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public class OtherAppsAdapter extends RecyclerView.Adapter<OtherAppsFragment.OtherAppsAdapter.AppsViewHolder>{
        List<App> appList = new ArrayList<App>();
        OtherAppsFragment context;

        OtherAppsAdapter(List<App> apps, OtherAppsFragment context){
            this.appList = apps;
            this.context = context;
        }
        @Override
        public OtherAppsAdapter.AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.other_app_layout, parent, false);
            OtherAppsAdapter.AppsViewHolder holder = new OtherAppsAdapter.AppsViewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(OtherAppsAdapter.AppsViewHolder holder, final int position) {
            final String logoUrl = "http:" + appList.get(position).getIcon();
            final String appUrl = appList.get(position).getUrl();
            String title = appList.get(position).getTitle();

            holder.title.setText(title);
            try {
                Glide.with(this.context)
                        .load(logoUrl)
                        .into(holder.appLogo);
            }catch(Exception ex){
                Log.e("OTHERAPPS", ex.getMessage());
            }

            holder.mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appList.get(position).getAppId())));
                    } catch (ActivityNotFoundException e) {
                        Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                        startActivity(myIntent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return appList.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class AppsViewHolder extends RecyclerView.ViewHolder{
            CardView mainLayout;
            ImageView appLogo;
            TextView title, link;
            public AppsViewHolder(View itemView) {
                super(itemView);
                mainLayout = (CardView) itemView.findViewById(R.id.layout);
                appLogo = (ImageView) itemView.findViewById(R.id.appLogo);
                link = (TextView) itemView.findViewById(R.id.downloadApp);
                title = (TextView) itemView.findViewById(R.id.appName);
            }
        }
    }
}
