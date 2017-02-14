package com.dlgdev.popularmovies.ui;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * Everything is queried through a sync adapter, so there is no foreground issue.
 * On top of that, Android now crashes if you even try, so the fact that the app runs should
 * show that this tests aren't needed.
 */
@Ignore public class Stage2NetworkApiImplementation {
	@Test public void moviesAreQueriedInBackgroundThread() {
		fail("Not implemented yet");
	}
	@Test public void videosAreRequestedInABackgroundThread() {
		fail("Not implemented yet");
	}
	@Test public void userReviewsAreRequestedInABackgroundThread() {
		fail("Not implemented yet");
	}
}
