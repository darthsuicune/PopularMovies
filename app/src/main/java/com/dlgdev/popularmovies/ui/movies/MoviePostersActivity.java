package com.dlgdev.popularmovies.ui.movies;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dlgdev.popularmovies.AppNavigation;
import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.sync.SyncManager;
import com.dlgdev.popularmovies.settings.Settings;
import com.dlgdev.popularmovies.ui.details.MovieDetailsFragment;

public class MoviePostersActivity extends AppCompatActivity {

	private static final String FRAGMENT_TAG = "postersFragment";

	SharedPreferences prefs;
	MoviePostersFragment postersFragment;
	MovieDetailsFragment detailsFragment;
	AppNavigation navigation;
	SyncManager sync;
	ActionBar actionBar;

	@Override protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_movie_posters);
		initializeComponents();
		setupFragments(savedInstanceState);
	}

	private void initializeComponents() {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		navigation = new AppNavigation(this);
		sync = new SyncManager(this);
		sync.initializeAdapter();
	}

	private void setupFragments(Bundle savedInstanceState) {
		if (isTablet()) {
			if (savedInstanceState == null) {
				detailsFragment = MovieDetailsFragment.newInstance(null);
				getSupportFragmentManager().beginTransaction()
						.replace(R.id.movie_details_container, detailsFragment, FRAGMENT_TAG).commit();
			} else {
				detailsFragment = (MovieDetailsFragment) getSupportFragmentManager()
						.findFragmentByTag(FRAGMENT_TAG);
			}
		}
	}

	private boolean isTablet() {
		return findViewById(R.id.movie_details_container) != null;
	}

	private void openMovie(Movie movie) {
		if (isTablet()) {
			detailsFragment.retrieveDataForMovie(movie);
		} else {
			navigation.openMovieDetails(movie);
		}
	}

	@Override protected void onResume() {
		super.onResume();
		actionBar = getSupportActionBar();
		postersFragment = (MoviePostersFragment) getSupportFragmentManager()
				.findFragmentById(R.id.fragment_movie_posters);
		postersFragment.setInterface(new MoviePostersFragment.MoviePostersInterface() {
			@Override public void openMovieDetails(Movie movie) {
				openMovie(movie);
			}
		});
		postersFragment.loadMovies();
		setSubtitle();
	}

	private void setSubtitle() {
		String movieType = prefs.getString(Settings.MOVIES_TYPE, Settings.POPULAR);
		int subtitle;
		switch (movieType) {
			case Settings.FAVORITES:
				subtitle = R.string.favorites;
				break;
			case Settings.TOP_RATED:
				subtitle = R.string.top_rated;
				break;
			default:
				subtitle = R.string.popular;
				break;
		}
		actionBar.setSubtitle(subtitle);
	}

	@Override public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				navigation.openSettings();
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
