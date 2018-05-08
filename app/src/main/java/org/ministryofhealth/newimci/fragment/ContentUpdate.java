package org.ministryofhealth.newimci.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.SplashActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.helper.TaskCompleted;
import org.ministryofhealth.newimci.helper.UpdateHelper;
import org.ministryofhealth.newimci.model.AgeGroup;
import org.ministryofhealth.newimci.model.Ailment;
import org.ministryofhealth.newimci.model.AilmentFollowUp;
import org.ministryofhealth.newimci.model.Assessment;
import org.ministryofhealth.newimci.model.AssessmentClassification;
import org.ministryofhealth.newimci.model.Category;
import org.ministryofhealth.newimci.model.CounselSubContent;
import org.ministryofhealth.newimci.model.CounselTitle;
import org.ministryofhealth.newimci.model.County;
import org.ministryofhealth.newimci.model.DiseaseClassification;
import org.ministryofhealth.newimci.model.Gallery;
import org.ministryofhealth.newimci.model.GalleryAilment;
import org.ministryofhealth.newimci.model.GalleryItem;
import org.ministryofhealth.newimci.model.Glossary;
import org.ministryofhealth.newimci.model.HIVCare;
import org.ministryofhealth.newimci.model.TreatAilment;
import org.ministryofhealth.newimci.model.TreatAilmentTreatment;
import org.ministryofhealth.newimci.model.TreatTitle;
import org.ministryofhealth.newimci.server.Service.AgeGroupService;
import org.ministryofhealth.newimci.server.Service.AilmentFollowUpService;
import org.ministryofhealth.newimci.server.Service.AilmentsService;
import org.ministryofhealth.newimci.server.Service.AssessmentClassificationService;
import org.ministryofhealth.newimci.server.Service.AssessmentService;
import org.ministryofhealth.newimci.server.Service.CategoryService;
import org.ministryofhealth.newimci.server.Service.CounselSubContentService;
import org.ministryofhealth.newimci.server.Service.CounselTitlesService;
import org.ministryofhealth.newimci.server.Service.CountyService;
import org.ministryofhealth.newimci.server.Service.DiseaseClassificationService;
import org.ministryofhealth.newimci.server.Service.GalleryAilmentService;
import org.ministryofhealth.newimci.server.Service.GalleryItemService;
import org.ministryofhealth.newimci.server.Service.GalleryService;
import org.ministryofhealth.newimci.server.Service.GlossaryService;
import org.ministryofhealth.newimci.server.Service.HIVCareService;
import org.ministryofhealth.newimci.server.Service.TreatAilmentService;
import org.ministryofhealth.newimci.server.Service.TreatAilmentTreatmentService;
import org.ministryofhealth.newimci.server.Service.TreatTitlesService;
import org.ministryofhealth.newimci.server.Service.UpdateContentService;
import org.ministryofhealth.newimci.server.model.Response;

import java.io.IOException;
import java.util.List;

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
public class ContentUpdate extends Fragment implements TaskCompleted{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHandler db;
    LinearLayout loadingLayout, updates_available;
    RelativeLayout noUpdatesLayout;



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
        db = new DatabaseHandler(getContext());
        View rootView = inflater.inflate(R.layout.fragment_content_update, container, false);
        // Inflate the layout for this fragment
        Retrofit retrofit = RetrofitHelper.getInstance().createHelper();
        SharedPreferences pref = getActivity().getSharedPreferences(getString(R.string.updates), Context.MODE_PRIVATE);
        String last_update = pref.getString(getString(R.string.last_update), "None");
        UpdateContentService updateService = retrofit.create(UpdateContentService.class);

        Call<Response> responseCall = updateService.getUpdateStatus(last_update);

        loadingLayout = (LinearLayout) rootView.findViewById(R.id.checking_update_loading_layout);
        noUpdatesLayout = (RelativeLayout) rootView.findViewById(R.id.no_updates_available);
        updates_available = (LinearLayout) rootView.findViewById(R.id.updates_available);

        Button downloadUpdates = (Button) rootView.findViewById(R.id.downloadUpdates);

        loadingLayout.setVisibility(View.VISIBLE);
        noUpdatesLayout.setVisibility(View.GONE);
        updates_available.setVisibility(View.GONE);

        responseCall.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                loadingLayout.setVisibility(View.GONE);
                boolean status = response.body().isStatus();
                System.out.println("Content Update " + status);
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

        downloadUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    updateContent();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "There was an error updating content", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    public void updateContent() throws IOException {
        UpdateHelper.getInstance(getContext(), true).updateContent();
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

    @Override
    public void onTaskComplete(Boolean status) {
        if(status){
            loadingLayout.setVisibility(View.GONE);
            noUpdatesLayout.setVisibility(View.VISIBLE);
            updates_available.setVisibility(View.GONE);
        }
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
