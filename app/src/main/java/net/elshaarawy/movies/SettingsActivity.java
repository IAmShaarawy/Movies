package net.elshaarawy.movies;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

/**
 * Created by elshaarawy on 22-Apr-16.
 */
public class SettingsActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_general);



        addSummary(findPreference(getString(R.string.pref_ordering_key)));
        addSummary(findPreference(getString(R.string.pref_page_key)));
    }

    private void addSummary(Preference preference) {

        preference.setOnPreferenceChangeListener(this);


        onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext()).getString(preference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        String pref_value_string = newValue.toString();
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;

            int pref_vlaue_index = listPreference.findIndexOfValue(pref_value_string);
            if (pref_vlaue_index>-1)
                preference.setSummary(listPreference.getEntries()[pref_vlaue_index]);
        }
        else {
            pref_value_string = pref_value_string.equals("0")?"Page 0 not exist we set 1 instead":pref_value_string;
            preference.setSummary(pref_value_string);
        }
        return true;
    }
}
