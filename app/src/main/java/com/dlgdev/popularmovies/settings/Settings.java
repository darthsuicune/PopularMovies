package com.dlgdev.popularmovies.settings;

public class Settings {
	public static final int SYNC_INTERVAL = 60 * 60 * 24; // 24 hours
	public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3 ; // 8 hour flexibility
	public static final String MOVIES_TYPE = "movies_type";
	public static final String POPULAR = "popular";
	public static final String TOP_RATED = "top_rated";
	public static final String FAVORITES = "favorites";
}
