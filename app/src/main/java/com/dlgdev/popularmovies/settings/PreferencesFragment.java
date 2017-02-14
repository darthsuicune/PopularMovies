package com.dlgdev.popularmovies.settings;

import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;


public abstract class PreferencesFragment extends PreferenceFragment {

	/**
	 * A preference value change onPrefChangeListener that updates the preference's summary
	 * to reflect its new value.
	 */
	Preference.OnPreferenceChangeListener onPreferenceChangeListener;

	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		onPreferenceChangeListener = setOnPreferenceChangeListener();
	}

	abstract Preference.OnPreferenceChangeListener setOnPreferenceChangeListener();

	/**
	 * Binds a preference's summary to its value. More specifically, when the
	 * preference's value is changed, its summary (line of text below the
	 * preference title) is updated to reflect the value. The summary is also
	 * immediately updated upon calling this method. The exact display format is
	 * dependent on the type of preference.
	 *
	 * @see #onPreferenceChangeListener
	 */
	protected void bindPreferenceSummaryToValue(Preference preference) {
		// Set the onPrefChangeListener to watch for value changes.
		preference.setOnPreferenceChangeListener(onPreferenceChangeListener);

		// Trigger the onPrefChangeListener immediately with the preference's
		// current value.
		onPreferenceChangeListener.onPreferenceChange(preference,
				PreferenceManager.getDefaultSharedPreferences(preference.getContext())
						.getString(preference.getKey(), ""));
	}

	class OnPrefChangeListener implements Preference.OnPreferenceChangeListener {
		@Override public boolean onPreferenceChange(Preference preference, Object value) {
			String stringValue = value.toString();

			if (preference instanceof ListPreference) {
				// For list preferences, look up the correct display value in
				// the preference's 'entries' list.
				ListPreference listPreference = (ListPreference) preference;
				int index = listPreference.findIndexOfValue(stringValue);

				// Set the summary to reflect the new value.
				preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);
			} else {
				// For all other preferences, set the summary to the value's
				// simple string representation.
				preference.setSummary(stringValue);
			}
			return true;
		}
	}
}
