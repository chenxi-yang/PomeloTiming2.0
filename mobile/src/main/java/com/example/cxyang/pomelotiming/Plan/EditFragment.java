package com.example.cxyang.pomelotiming.Plan;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import com.example.cxyang.pomelotiming.R;

public class EditFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.edit);

        EditTextPreference nm = (EditTextPreference) findPreference("name_preference");
        nm.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object o) {
                String value = (String) o;
                SharedPreferences shared = getActivity().getSharedPreferences("EditFragment", Context.MODE_PRIVATE);
                shared.edit().putString(preference.getKey(), value).commit();

                preference.setSummary(value);
                return true;
            }
        });
    }
}
