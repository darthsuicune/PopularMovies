package com.dlgdev.popularmovies.ui;

import android.app.Instrumentation.ActivityResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.db.DBUtils;
import com.dlgdev.popularmovies.data.db.MovieUtils;
import com.dlgdev.popularmovies.ui.details.MovieDetailsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.app.Activity.RESULT_OK;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasData;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasHost;
import static android.support.test.espresso.intent.matcher.UriMatchers.hasScheme;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static com.dlgdev.popularmovies.ui.details.MovieDetailsFragment.KEY_MOVIE;
import static org.hamcrest.core.AllOf.allOf;

public class Stage2UIFunctionDetailsTests {
	@Rule public IntentsTestRule<MovieDetailsActivity> rule =
			new IntentsTestRule<>(MovieDetailsActivity.class, true, false);

	MovieDetailsActivity activity;
	ContentResolver      resolver;

	@Before public void setUp() throws Exception {
		resolver = InstrumentationRegistry.getTargetContext().getContentResolver();
		Uri uri = MovieUtils.insertMovies(resolver, 1);
		Intent intent = new Intent();
		intent.putExtra(KEY_MOVIE, ContentUris.parseId(uri));
		activity = rule.launchActivity(intent);
	}

	@After public void tearDown() throws Exception {
		DBUtils.clearDatabase(activity);
	}

	@Test public void intentIsLaunchedToOpenATrailer() throws InterruptedException {
		intending(hasAction(Intent.ACTION_VIEW)).respondWith(new ActivityResult(RESULT_OK, null));
		onView(withId(R.id.trailers_label)).perform(click());
		onView(withId(R.id.trailers)).perform(actionOnItemAtPosition(0, click()));
		intended(allOf(hasAction(Intent.ACTION_VIEW),
		         hasData(allOf(hasScheme("https"), hasHost("www.youtube.com")))));
	}

	@Test public void userCanMarkFilmAsFavorite() throws InterruptedException {
		onView(withId(R.id.favorite)).perform(click());
	}
}
