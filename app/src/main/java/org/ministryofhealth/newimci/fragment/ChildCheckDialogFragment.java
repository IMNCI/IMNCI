package org.ministryofhealth.newimci.fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.ministryofhealth.newimci.AssessClassifyIdentifyTreatmentActivity;
import org.ministryofhealth.newimci.CounselActivity;
import org.ministryofhealth.newimci.FollowUpCareAilmentsActivity;
import org.ministryofhealth.newimci.R;
import org.ministryofhealth.newimci.TreatActivity;
import org.ministryofhealth.newimci.database.DatabaseHandler;
import org.ministryofhealth.newimci.model.AgeGroup;

import java.util.List;

/**
 * Created by chriz on 9/12/2017.
 */

public class ChildCheckDialogFragment extends DialogFragment {
    private static String ARG_TITLE = "title";
    private static String ARG_SLUG = "slug";
    public static ChildCheckDialogFragment newInstance(String title, String slug) {
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, title);
        args.putString(ARG_SLUG, slug);

        ChildCheckDialogFragment fragment = new ChildCheckDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString(ARG_TITLE);
        final String slug = getArguments().getString(ARG_SLUG);

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View view = View.inflate(getActivity(), R.layout.child_check_alert_view, null);

        LinearLayout type_of_visit = (LinearLayout) view.findViewById(R.id.type_of_visit_layout);
        final RadioGroup type_of_visit_group = (RadioGroup) view.findViewById(R.id.type_of_visit_radio_group);
        final RadioGroup age_of_child_group = (RadioGroup) view.findViewById(R.id.age_of_child_radio_group);

        DatabaseHandler db = new DatabaseHandler(getActivity());
        List<AgeGroup> ageGroupList = db.getAgeGroups();
        int checked = 0;
        for (AgeGroup ageGroup:
             ageGroupList) {
            RadioButton radioButton = new RadioButton(getActivity());
            radioButton.setText(ageGroup.getAge_group());
            radioButton.setId(ageGroup.getId());
            radioButton.setTag(R.string.up_to_2_months_tag, ageGroup.getId());
            age_of_child_group.addView(radioButton);
            if (checked == 0){
                radioButton.setChecked(true);
                checked = 1;
            }
        }

        if (!slug.equals("assess_classify_identify")){
            type_of_visit.setVisibility(View.GONE);
        }

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int selectedVisitID = type_of_visit_group.getCheckedRadioButtonId();
                        int selectedAge = age_of_child_group.getCheckedRadioButtonId();

                        if (selectedVisitID != 0 && selectedAge != 0){
                            RadioButton visitRadio = (RadioButton) view.findViewById(selectedVisitID);
                            RadioButton ageRadio = (RadioButton) view.findViewById(selectedAge);

                            doPositiveClick(visitRadio.getTag().toString(), ageRadio.getId(), slug);
                        }
                    }
                })
                .create();

    }

    public void doPositiveClick(String visit_tag, int age_tag, String slug){
        if (visit_tag.equals("follow_up_care")){
            slug = "follow_up_care";
        }
        Intent intent= null;
        switch (slug){
            case "assess_classify_identify":
                intent = new Intent(getActivity(), AssessClassifyIdentifyTreatmentActivity.class);
                intent.putExtra("age", age_tag);
                startActivity(intent);
                break;
            case "treat_infant":
                intent = new Intent(getActivity(), TreatActivity.class);
                intent.putExtra("age", age_tag);
                startActivity(intent);
                break;
            case "counsel_mother":
                intent = new Intent(getActivity(), CounselActivity.class);
                intent.putExtra("age", age_tag);
                startActivity(intent);
                break;
            case "follow_up_care":
                intent = new Intent(getActivity(), FollowUpCareAilmentsActivity.class);
                intent.putExtra("age", age_tag);
                startActivity(intent);
                break;
        }
    }

    public void doNegativeClick(){

    }
}
