package com.dlgdev.popularmovies.ui;

import android.app.Activity;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.ui.movies.MoviePostersActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertTrue;

public class Stage2UILayoutTests {
	@Rule public ActivityTestRule<MoviePostersActivity> rule =
			new ActivityTestRule<>(MoviePostersActivity.class);

	MoviePostersActivity activity;

	@Before public void setUp() throws Exception {
		activity = rule.getActivity();
		MovieUtils.insertMovies(activity.getContentResolver(), 1);
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void moviesAreDisplayedViaGridWithThumbnails() {
		onView(withId(R.id.fragment_movie_posters)).check(matches(isDisplayed()));
		RecyclerView view = (RecyclerView) activity.findViewById(R.id.fragment_movie_posters);
		assertTrue(view.getLayoutManager() instanceof GridLayoutManager);
		assertTrue(view.getChildAt(0).findViewById(R.id.poster) instanceof ImageView);
	}

	@Test public void uiContainsElementToShowMostPopularHighestRatedFavorites() {
		toggleSortOrderSelector(activity);

		onView(withText(R.string.popular)).check(matches(isDisplayed()));
		onView(withText(R.string.top_rated)).check(matches(isDisplayed()));
		onView(withText(R.string.favorites)).check(matches(isDisplayed()));
	}

	static void toggleSortOrderSelector(Activity activity) {
		openActionBarOverflowOrOptionsMenu(activity);
		onView(allOf(withId(R.id.title), withText("Settings"), isDisplayed())).perform(click());

		ViewInteraction settingsLayout =
				onView(allOf(withClassName(is("android.widget.LinearLayout")), withParent(
						allOf(withId(android.R.id.list),
								withParent(withClassName(is("android.widget.LinearLayout"))))),
						isDisplayed()));
		settingsLayout.perform(click());

		ViewInteraction movieTypeOptions =
				onView(allOf(withClassName(is("android.widget.LinearLayout")),
						withParent(withId(android.R.id.list)), isDisplayed()));
		movieTypeOptions.perform(click());
	}

	@Test public void uiContainsScreenForDisplayingDetailsForAMovie() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));
		onView(withId(R.id.plot_synopsis)).check(matches(isDisplayed()));
	}

	@Test public void detailsScreenContainsTitleReleaseDatePosterVoteAverageSynopsis() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));

		onView(withId(R.id.title)).check(matches(isDisplayed()));
		onView(withId(R.id.release_date)).check(matches(isDisplayed()));
		onView(withId(R.id.poster)).check(matches(isDisplayed()));
		onView(withId(R.id.vote_average)).check(matches(isDisplayed()));
		onView(withId(R.id.plot_synopsis)).check(matches(isDisplayed()));
	}

	@Test public void detailsContainsSectionForTrailerVideosAndUserReviews() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));

		onView(withId(R.id.trailers_label)).check(matches(isDisplayed()));
		onView(withId(R.id.user_reviews_label)).check(matches(isDisplayed()));
	}

	@Test public void detailsContainsSectionForTrailerVideos() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));
		onView(withId(R.id.trailers_label)).perform(click());
		onView(withId(R.id.trailers)).check(matches(isDisplayed()));
	}

	@Test public void detailsContainsSectionForReviews() {
		clickFirstPoster();
		onView(withId(R.id.user_reviews_label)).perform(click());
		onView(withId(R.id.user_reviews)).check(matches(isDisplayed()));
	}

	private void clickFirstPoster() {
		onView(withId(R.id.fragment_movie_posters)).perform(actionOnItemAtPosition(0, click()));
	}
}
