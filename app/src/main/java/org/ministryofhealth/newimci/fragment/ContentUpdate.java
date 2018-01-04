package org.ministryofhealth.newimci.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.server.Service.UpdateContentService;
import org.ministryofhealth.newimci.server.model.Response;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContentUpdate.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContentUpdate#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContentUpdate extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContentUpdate() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContentUpdate.
     */
    // TODO: Rename and change types and number of parameters
    public static ContentUpdate newInstance(String param1, String param2) {
        ContentUpdate fragment = new ContentUpdate();
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
        View rootView = inflater.inflate(R.layout.fragment_content_update, container, false);
        // Inflate the layout for this fragment
        Retrofit retrofit = RetrofitHelper.getInstance().createHelper();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.updates), Context.MODE_PRIVATE);
        String last_update = pref.getString(getString(R.string.last_update), "None");
        UpdateContentService updateService = retrofit.create(UpdateContentService.class);

        Call<Response> responseCall = updateService.getUpdateStatus(last_update);

        final LinearLayout loadingLayout = (LinearLayout) rootView.findViewById(R.id.checking_update_loading_layout);
        final RelativeLayout noUpdatesLayout = (RelativeLayout) rootView.findViewById(R.id.no_updates_available);
        final LinearLayout updates_available = (LinearLayout) rootView.findViewById(R.id.updates_available);

        loadingLayout.setVisibility(View.VISIBLE);
        noUpdatesLayout.setVisibility(View.GONE);
        updates_available.setVisibility(View.GONE);

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                loadingLayout.setVisibility(View.GONE);
                boolean status = response.body().isStatus();
                if (status){
                    updates_available.setVisibility(View.VISIBLE);
                    noUpdatesLayout.setVisibility(View.GONE);
                }else{
                    updates_available.setVisibility(View.GONE);
                    noUpdatesLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                Toast.makeText(getContext(), "There was an error", Toast.LENGTH_SHORT).show();
            }
        });
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
}
