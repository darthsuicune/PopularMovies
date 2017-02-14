package com.dlgdev.popularmovies.settings;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.view.MenuItem;

import com.dlgdev.popularmovies.R;

/**
 * This fragment shows notification preferences only. It is used when the
 * activity is showing a two-pane settings UI.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB) public class NotificationPreferenceFragment
		extends PreferencesFragment {
	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_notification);
		setHasOptionsMenu(true);

		// Bind the summaries of EditText/List/Dialog/Ringtone preferences
		// to their values. When their values change, their summaries are
		// updated to reflect the new value, per the Android Design
		// guidelines.
		bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
	}

	@Override Preference.OnPreferenceChangeListener setOnPreferenceChangeListener() {
		return new PreferencesFragment.OnPrefChangeListener();
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
