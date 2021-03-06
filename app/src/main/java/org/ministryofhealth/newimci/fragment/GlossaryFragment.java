package org.ministryofhealth.newimci.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.adapter.GlossaryAdapter;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.Glossary;
import org.ministryofhealth.newimci.util.DividerItemDecoration;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GlossaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GlossaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GlossaryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseHandler db;
    List<Glossary> glossaryList;
    LinearLayout emptyLayout;
    RecyclerView recyclerView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public GlossaryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GlossaryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GlossaryFragment newInstance(String param1, String param2) {
        GlossaryFragment fragment = new GlossaryFragment();
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
        View view = inflater.inflate(R.layout.fragment_glossary, container, false);

        emptyLayout = (LinearLayout) view.findViewById(R.id.emptyLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.glossaryRecyclerView);

        db = new DatabaseHandler(getContext());
        glossaryList = db.getGlossary();

        updateUI();

        GlossaryAdapter glossaryAdapter = new GlossaryAdapter(getContext(), glossaryList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        recyclerView.setAdapter(glossaryAdapter);
        return view;
    }

    private void updateUI(){
        if (glossaryList.size() > 0){
            emptyLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else{
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
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
