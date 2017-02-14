package com.dlgdev.popularmovies.ui;

import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.ui.details.MovieDetailsActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasType;
import static android.support.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withResourceName;
import static com.dlgdev.popularmovies.ExtraMatchers.first;
import static com.dlgdev.popularmovies.ui.details.MovieDetailsFragment.KEY_MOVIE;
import static org.hamcrest.CoreMatchers.allOf;

public class OptionalSharingTests {
	AppCompatActivity activity;
	@Rule public IntentsTestRule<MovieDetailsActivity> rule =
			new IntentsTestRule<>(MovieDetailsActivity.class, true, false);

	@Before public void setUp() throws Exception {
		Context context = InstrumentationRegistry.getTargetContext();
		ContentResolver resolver = context.getContentResolver();
		Uri uri = MovieUtils.insertMovies(resolver, 1);
		Intent intent = new Intent();
		intent.putExtra(KEY_MOVIE, ContentUris.parseId(uri));
		activity = rule.launchActivity(intent);
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void detailsViewIncludesAShareTrailerInActionBar() {
		onView(shareProvider()).check(matches(isDisplayed()));
	}

	@Test public void appUsesAShareIntentToExposeYoutubeUrlForTrailer() throws Throwable {
		intending(allOf(hasAction(Intent.ACTION_SEND), hasType("text/plain"),
		                hasExtra(Intent.EXTRA_TEXT,
		                         "https://www.youtube.com/watch?v=asdf")))
				.respondWith(new Instrumentation.ActivityResult(RESULT_OK, null));
		onView(shareProvider()).check(matches(isDisplayed()));
		toggleShare();
		intended(allOf(hasAction(Intent.ACTION_SEND), hasType("text/plain"),
		                       hasExtra(Intent.EXTRA_TEXT,
		                                "https://www.youtube.com/watch?v=asdf")));
	}

	private void toggleShare() {
		onView(shareProvider()).perform(click());
		onView(first(withResourceName("icon"))).inRoot(isPlatformPopup()).perform(click());
	}

	Matcher<View> shareProvider() {
		return withResourceName("expand_activities_button");
	}
}
