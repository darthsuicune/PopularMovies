package com.dlgdev.popularmovies.ui.details;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.view.menu.ActionMenuItemView;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.ImagePosterProvider;
import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.dlgdev.popularmovies.data.movies.Movie;
import com.dlgdev.popularmovies.data.movies.MovieProvider;
import com.dlgdev.popularmovies.data.movies.MovieProviderImpl;
import com.dlgdev.popularmovies.data.reviews.Review;
import com.dlgdev.popularmovies.data.reviews.Reviews;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.dlgdev.popularmovies.data.trailers.Trailers;
import com.dlgdev.popularmovies.ui.reviews.ReviewsAdapter;
import com.dlgdev.popularmovies.ui.trailers.TrailerViewHolder;
import com.dlgdev.popularmovies.ui.trailers.TrailersAdapter;
import com.squareup.picasso.Picasso;

import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsFragment extends Fragment {
	private static final int    LOADER_MOVIE    = 1;
	private static final int    LOADER_TRAILERS = 2;
	private static final int    LOADER_REVIEWS  = 3;
	public static final  String KEY_MOVIE       = "movie";
	public static final         String KEY_MOVIE_TYPE  = "movieType";

	@BindView(R.id.poster)       ImageView posterView;
	@BindView(R.id.title)        TextView  titleView;
	@BindView(R.id.release_date) TextView  releaseDateView;
	@BindView(R.id.vote_average) TextView  voteAverageView;

	@BindView(R.id.plot_synopsis) TextView     synopsisView;
	@BindView(R.id.user_reviews)  RecyclerView reviewsView;
	@BindView(R.id.trailers)      RecyclerView trailersView;

	Movie               movie;
	MovieProvider       provider;
	ImagePosterProvider posterProvider;
	ShareActionProvider shareAction;

	public MovieDetailsFragment() {
		// Required empty public constructor
	}

	public static MovieDetailsFragment newInstance(Bundle args) {
		MovieDetailsFragment fragment = new MovieDetailsFragment();
		fragment.setArguments(args);
		return fragment;
	}

	@Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                                   Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_movie_details, container, false);
		ButterKnife.bind(this, v);
		setupViews();
		return v;
	}

	private void setupViews() {
		trailersView.setLayoutManager(new LinearLayoutManager(getContext()));
		reviewsView.setLayoutManager(new LinearLayoutManager(getContext()));
	}

	@Override public void onAttach(Context context) {
		super.onAttach(context);
		setHasOptionsMenu(true);
		MovieProviderImpl movieProvider = new MovieProviderImpl(context.getContentResolver());
		provider = movieProvider;
		posterProvider = movieProvider;
	}

	@Override public void onResume() {
		super.onResume();
		if (getArguments() != null) {
			retrieveMovie(getArguments());
		}
	}

	@Override public void onDetach() {
		super.onDetach();
		setHasOptionsMenu(false);
	}

	private void retrieveMovie(Bundle args) {
		getLoaderManager().restartLoader(LOADER_MOVIE, args, new MoviesLoaderHelper());
	}

	private void retrieveTrailersAndReviews(Bundle args) {
		getLoaderManager().restartLoader(LOADER_TRAILERS, args, new MoviesLoaderHelper());
		getLoaderManager().restartLoader(LOADER_REVIEWS, args, new MoviesLoaderHelper());
	}

	public void retrieveDataForMovie(Movie movie) {
		this.movie = movie;
		Bundle args = new Bundle();
		args.putLong(KEY_MOVIE, movie.rowId);
		retrieveTrailersAndReviews(args);
	}

	private void showMovieInfo() {
		int width = 500; //The Movie DB API only accepts some widths, so yeah, there's that.
		Picasso.with(getContext()).load(posterProvider.posterUrl(movie, width)).centerInside().fit()
		       .into(posterView);
		titleView.setText(movie.title);
		releaseDateView.setText(movie.releaseDate);
		voteAverageView.setText(String.format(Locale.getDefault(), "%2.2f", movie.voteAverage));
		synopsisView.setText(movie.plotOverview);

		MenuView.ItemView v = (ActionMenuItemView) getActivity().findViewById(R.id.favorite);
		if (v != null) {
			int resId =
					(movie.favorite) ? android.R.drawable.star_on : android.R.drawable.star_big_off;
			Drawable drawable;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
				drawable = getResources().getDrawable(resId, getContext().getTheme());
			} else {
				drawable = getResources().getDrawable(resId);
			}
			v.setIcon(drawable);
		}
	}

	@OnClick(R.id.synopsis_label) public void showSynopsis() {
		synopsisView.setVisibility(View.VISIBLE);
		trailersView.setVisibility(View.GONE);
		reviewsView.setVisibility(View.GONE);
	}

	@OnClick(R.id.trailers_label) public void showTrailers() {
		synopsisView.setVisibility(View.GONE);
		trailersView.setVisibility(View.VISIBLE);
		reviewsView.setVisibility(View.GONE);
	}

	@OnClick(R.id.user_reviews_label) public void showReviews() {
		synopsisView.setVisibility(View.GONE);
		trailersView.setVisibility(View.GONE);
		reviewsView.setVisibility(View.VISIBLE);
	}

	@Override public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.movie_details, menu);
		shareAction =
				(ShareActionProvider) MenuItemCompat.getActionProvider(menu.findItem(R.id.share));
		prepareShareAction();
	}

	private void prepareShareAction() {
		Intent intent = new Intent(Intent.ACTION_SEND);
		if (movie != null && movie.trailers != null && movie.trailers.size() > 0) {
			intent.putExtra(Intent.EXTRA_TEXT, movie.trailers.get(0).url());
		}
		intent.setType("text/plain");
		if(shareAction != null) {
			shareAction.setShareIntent(intent);
		}
	}

	@Override public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.favorite:
				provider.favMovie(movie);
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private class MoviesLoaderHelper implements LoaderManager.LoaderCallbacks<Cursor> {

		@Override public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			if (args == null) {
				return null;
			}

			Uri uri;
			CursorLoader loader;
			String where;
			String[] whereArgs = new String[1];
			long movieId = args.getLong(KEY_MOVIE);
			switch (id) {
				case LOADER_TRAILERS:
					uri = MoviesContract.Trailers.CONTENT_URI;
					where = MoviesContract.Trailers.MOVIE_ID + "=?";
					whereArgs[0] = Long.toString(movieId);
					loader = new CursorLoader(getContext(), uri, null, where, whereArgs, null);
					loader.dump("Cursor for trailers", null, new PrintWriter(System.out), null);
					break;
				case LOADER_REVIEWS:
					uri = MoviesContract.Reviews.CONTENT_URI;
					where = MoviesContract.Reviews.MOVIE_ID + "=?";
					whereArgs[0] = Long.toString(movieId);
					loader = new CursorLoader(getContext(), uri, null, where, whereArgs, null);
					break;
				case LOADER_MOVIE:
				default:
					uri = MoviesContract.buildMovieUri(movieId);
					loader = new CursorLoader(getContext(), uri, null, null, null, null);
					break;
			}
			return loader;
		}

		@Override public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
			if (data != null && data.moveToFirst()) {
				switch (loader.getId()) {
					case LOADER_TRAILERS:
						movie.trailers = Trailers.fromCursor(data);
						setTrailers(movie.trailers);
						break;
					case LOADER_REVIEWS:
						List<Review> reviews = Reviews.fromCursor(data);
						setReviews(reviews);
						break;
					case LOADER_MOVIE:
					default:
						movie = provider.loadMovie(data);
						showMovieInfo();
						getArguments().putLong(KEY_MOVIE, movie.rowId);
						retrieveTrailersAndReviews(getArguments());
				}
			}
		}

		@Override public void onLoaderReset(Loader<Cursor> loader) {
		}
	}

	private void setReviews(List<Review> reviews) {
		reviewsView.setAdapter(new ReviewsAdapter(reviews));

	}

	private void setTrailers(List<Trailer> trailers) {
		trailersView.setAdapter(
				new TrailersAdapter(trailers, new TrailerViewHolder.OnTrailerClickListener() {
					@Override public void onTrailerClick(Trailer trailer) {
						Intent intent = viewTrailerIntent(trailer);
						startActivity(intent);
					}
				}));
			prepareShareAction();

	}

	private Intent viewTrailerIntent(Trailer trailer) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(trailer.url()));
		return intent;
	}
}
