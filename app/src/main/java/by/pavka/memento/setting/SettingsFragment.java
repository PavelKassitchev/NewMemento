package by.pavka.memento.setting;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import by.pavka.memento.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}