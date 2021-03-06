package org.ministryofhealth.newimci.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.helper.HtmlHelper;
import org.ministryofhealth.newimci.model.AilmentFollowUp;
import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FollowupAilmentAdviceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FollowupAilmentAdviceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FollowupAilmentAdviceFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_AILMENT_FOLLOWUP = "ailment_follow_up";

    WebView webView;
    String adviceContent;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private AilmentFollowUp mAilmentFollowUp;

    private OnFragmentInteractionListener mListener;

    public FollowupAilmentAdviceFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FollowupAilmentAdviceFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FollowupAilmentAdviceFragment newInstance(String param1, String param2, AilmentFollowUp ailmentFollowUp) {
        FollowupAilmentAdviceFragment fragment = new FollowupAilmentAdviceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_AILMENT_FOLLOWUP, ailmentFollowUp);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mAilmentFollowUp = getArguments().getParcelable(ARG_AILMENT_FOLLOWUP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_followup_ailment_advice, container, false);
        webView = rootView.findViewById(R.id.ailment_advice_webview);

        new LoadAdviceView().execute();
//        HtmlTextView ailmentText = (HtmlTextView) rootView.findViewById(R.id.ailment_advice);
//        if (mAilmentFollowUp.getAdvice() != null)
//            ailmentText.setHtml(mAilmentFollowUp.getAdvice());
//        else
//            ailmentText.setText("No Advice for this ailment");
        return rootView;
    }

    class LoadAdviceView extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            adviceContent = mAilmentFollowUp.getAdvice();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    WebSettings s = webView.getSettings();
                    s.setUseWideViewPort(false);
                    s.setSupportZoom(true);
                    s.setBuiltInZoomControls(true);
                    s.setDisplayZoomControls(true);
                    s.setJavaScriptEnabled(true);
                    webView.loadData(adviceContent, "text/html", "utf-8");
                }
            });
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
