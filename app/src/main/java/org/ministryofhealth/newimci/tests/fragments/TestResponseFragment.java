package org.ministryofhealth.newimci.tests.fragments;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.QuestionResponse;
import org.ministryofhealth.newimci.model.TestResponse;

import java.util.List;

public class TestResponseFragment extends DialogFragment {
    private View rootView;
    LinearLayout lytQuestionResponses;
    DatabaseHandler db;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.dialog_test_responses, container, false);
        db = new DatabaseHandler(getContext());

        String question = getArguments().getString("question");
        int question_id = getArguments().getInt("question_id");
        int attempt_id = getArguments().getInt("attempt_id");

        List<TestResponse> responses = db.getTestResponses(attempt_id, question_id);
        lytQuestionResponses = rootView.findViewById(R.id.questionResponsesLayout);
        for (TestResponse response:
             responses) {
            LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.reponse_layout, null);

            TextView txtResponse = layout.findViewById(R.id.response);
            ImageView imgIcon = layout.findViewById(R.id.icon);

            txtResponse.setText(response.getResponse());

            if(response.getGot_it()){
                imgIcon.setImageResource(R.drawable.ic_check_mark);
            }else{
                imgIcon.setImageResource(R.drawable.ic_cross_mark);
            }

            lytQuestionResponses.addView(layout);
        }

        ((TextView) rootView.findViewById(R.id.question)).setText(Html.fromHtml(question));

        ((FloatingActionButton) rootView.findViewById(R.id.fab)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        return dialog;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
