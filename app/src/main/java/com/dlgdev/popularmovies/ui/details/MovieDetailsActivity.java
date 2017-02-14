package com.dlgdev.popularmovies.ui.details;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.settings.Settings;

public class MovieDetailsActivity extends AppCompatActivity {

	public static final String DETAILS_FRAGMENT_TAG = "detailsFragment";
	MovieDetailsFragment fragment;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_details);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Bundle args = getIntent().getExtras();
		args.putString(MovieDetailsFragment.KEY_MOVIE_TYPE,
				prefs.getString(Settings.MOVIES_TYPE, Settings.POPULAR));

		fragment = (MovieDetailsFragment) getSupportFragmentManager()
						.findFragmentByTag(DETAILS_FRAGMENT_TAG);
		if (fragment == null) {
			fragment = MovieDetailsFragment.newInstance(args);
		}
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.movie_details_container, fragment, DETAILS_FRAGMENT_TAG)
				.commit();
	}
}
