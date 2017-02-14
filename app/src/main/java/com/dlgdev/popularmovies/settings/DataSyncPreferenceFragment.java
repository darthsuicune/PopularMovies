package com.dlgdev.popularmovies.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.sync.SyncManager;

/**
 * This fragment shows data and sync preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class DataSyncPreferenceFragment
		extends PreferencesFragment {
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_data_sync);
		setHasOptionsMenu(true);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		bindPreferenceSummaryToValue(findPreference("movies_type"));
	}

	@Override Preference.OnPreferenceChangeListener setOnPreferenceChangeListener() {
		return new PreferencesFragment.OnPrefChangeListener() {
			@Override public boolean onPreferenceChange(Preference preference, Object value) {
				super.onPreferenceChange(preference, value);
				if(Settings.FAVORITES.equals(value)) {
					return true;
				}
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
					new SyncManager(getContext()).syncImmediately();
				} else {
					new SyncManager(getActivity()).syncImmediately();
				}
				return true;
			}
		};
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == android.R.id.home) {
			startActivity(new Intent(getActivity(), SettingsActivity.class));
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
