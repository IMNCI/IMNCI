package org.ministryofhealth.newimci.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;

import org.ministryofhealth.newimci.R;

/**
 * Created by chriz on 9/10/2017.
 */

public class SettingsFragment extends PreferenceFragment {
    OnPreferenceChangeListener mCallback;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.app_preferences);

        Preference.OnPreferenceChangeListener listener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                mCallback.onPreferenceChanged(preference.getKey(), (String) newValue);
                return true;
            }
        };

        EditTextPreference displayNamePreference = (EditTextPreference) findPreference("display_name");
        EditTextPreference displayEmailPreference = (EditTextPreference) findPreference("display_email");

        displayNamePreference.setOnPreferenceChangeListener(listener);
        displayEmailPreference.setOnPreferenceChangeListener(listener);
    }

    public interface OnPreferenceChangeListener{
        public void onPreferenceChanged(String key, String value);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnPreferenceChangeListener) activity;
        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
