package com.dlgdev.popularmovies.ui.movies;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.movies.MovieProviderImpl;
import com.dlgdev.popularmovies.data.sync.SyncManager;
import com.dlgdev.popularmovies.settings.Settings;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoviePostersFragment extends Fragment {
	private static final int LOADER_MOVIES = 1;

	@BindView(R.id.fragment_movie_posters) RecyclerView postersView;
	MoviesAdapter         adapter;
	MoviePostersInterface moviePostersInterface;
	SharedPreferences     prefs;

	public MoviePostersFragment() {

	}

	@Override public void onAttach(Context context) {
		super.onAttach(context);
		setHasOptionsMenu(true);
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		MovieProviderImpl provider = new MovieProviderImpl(context.getContentResolver());
		adapter =
				new MoviesAdapter(provider, provider, new MoviesViewHolder.OnMovieClickListener() {
					@Override public void openMovieDetails(Movie movie) {
						moviePostersInterface.openMovieDetails(movie);
					}
				});
		loadMovies();
	}

	public void loadMovies() {
		getLoaderManager().restartLoader(LOADER_MOVIES, null, new MoviesLoaderHelper());
	}

	@Override public void onDetach() {
		super.onDetach();
		setHasOptionsMenu(false);
	}

	@Nullable @Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
	                         @Nullable Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_movie_posters, container, false);
		preparePostersView(v, getResources().getConfiguration().orientation);
		return v;
	}

	private void preparePostersView(View v, int orientation) {
		ButterKnife.bind(this, v);
		postersView.setAdapter(adapter);
		int colspan = (orientation == Configuration.ORIENTATION_LANDSCAPE) ? 3 : 2;
		postersView.setLayoutManager(
				new GridLayoutManager(v.getContext(), colspan, LinearLayoutManager.VERTICAL,
				                      false));
	}

	public void setInterface(MoviePostersInterface moviePostersInterface) {
		this.moviePostersInterface = moviePostersInterface;
	}

	private class MoviesLoaderHelper implements LoaderManager.LoaderCallbacks<Cursor> {

		@Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			String movieType = prefs.getString(Settings.MOVIES_TYPE, Settings.POPULAR);
			Uri uri = MoviesContract.buildMovieUriForType(movieType);
			String[] projection = MoviesContract.projectionForMovieDetails();
			return new CursorLoader(getContext(), uri, projection, null, null, null);
		}

		@Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			if (data.getCount() == 0) {
				new SyncManager(getContext()).syncImmediately();
			}
			adapter.showMovies(data);
		}

		@Override public void onLoaderReset(Loader<Cursor> loader) {
			adapter.showMovies(null);
		}
	}

	public interface MoviePostersInterface {
		void openMovieDetails(Movie movie);
	}
}
