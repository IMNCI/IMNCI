package org.ministryofhealth.newimci.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.helper.RetrofitHelper;
import org.ministryofhealth.newimci.model.Review;
import org.ministryofhealth.newimci.server.Service.ReviewService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    SharedPreferences preference;
    String name, email;
    Spinner issueSpinner;
    AwesomeValidation mAwesomeValidation;

    EditText txtName, txtEmail, etxComment;
    Button submitBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReviewFragment newInstance(String param1, String param2) {
        ReviewFragment fragment = new ReviewFragment();
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
        final View view = inflater.inflate(R.layout.fragment_review, container, false);
        preference = PreferenceManager.getDefaultSharedPreferences(getContext());
        name = preference.getString("display_name", "");
        email = preference.getString("display_email", "");

        txtName = (EditText) view.findViewById(R.id.input_name);
        txtEmail = (EditText) view.findViewById(R.id.input_email);
        issueSpinner = (Spinner) view.findViewById(R.id.issue_spinner);

        submitBtn = (Button) view.findViewById(R.id.submit_review_btn);
        etxComment = (EditText) view.findViewById(R.id.comment);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.issues_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        issueSpinner.setAdapter(adapter);

        mAwesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        mAwesomeValidation.addValidation(txtName, RegexTemplate.NOT_EMPTY, getContext().getString(R.string.error_name));
        mAwesomeValidation.addValidation(txtEmail, Patterns.EMAIL_ADDRESS, getContext().getString(R.string.error_email));
        mAwesomeValidation.addValidation(etxComment, RegexTemplate.NOT_EMPTY, getContext().getString(R.string.error_comment));

        resetUI();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String issue = issueSpinner.getSelectedItem().toString();
                String name = txtName.getText().toString();
                if (mAwesomeValidation.validate() && (!issue.equals("Select an Option"))) {
                    final ProgressDialog dialog = new ProgressDialog(getContext());
                    dialog.setMessage("Submitting Review");
                    dialog.show();
                    Review review = new Review();

                    review.setName(name);
                    review.setEmail(txtEmail.getText().toString());
                    review.setComment(etxComment.getText().toString());

                    if (issue.equals("Select an Option")) {
                        review.setIssue(issue);
                    }

                    review.setRating(0);

                    Retrofit retrofit = RetrofitHelper.getInstance().createHelper();

                    ReviewService client = retrofit.create(ReviewService.class);

                    Call<Review> call = client.create(review);

                    call.enqueue(new Callback<Review>() {
                        @Override
                        public void onResponse(Call<Review> call, Response<Review> response) {
                            dialog.dismiss();
                            if (response.code() > 199 && response.code() < 210) {
                                Toast.makeText(getContext(), "Your issue was reported successfully", Toast.LENGTH_SHORT).show();
                                Fragment fragment = new DashboardFragment();
                                android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();

                            } else {
                                try {
                                    AlertDialog.Builder builder;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        builder = new AlertDialog.Builder(getContext(), android.R.style.Theme_Material_Dialog_Alert);
                                    } else {
                                        builder = new AlertDialog.Builder(getContext());
                                    }
                                    builder.setTitle("Error")
                                            .setMessage("Could not submit your review. Please try again some other time")
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                            .show();
                                    Log.e("Review Error", response.errorBody().string());
                                    System.out.println("Review Error" + response.errorBody().string());
                                    Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_SHORT).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Review> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(getContext(), "Please fill in all the fields on this form", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void resetUI(){
        if (!name.equals(getContext().getResources().getString(R.string.pref_default_display_name)))
            txtName.setText(name);
        if (!email.equals(getContext().getResources().getString(R.string.pref_default_display_email)))
            txtEmail.setText(email);

        etxComment.setText("");

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
