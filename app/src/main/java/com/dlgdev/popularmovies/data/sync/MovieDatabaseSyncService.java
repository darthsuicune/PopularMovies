package com.dlgdev.popularmovies.data.sync;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.preference.PreferenceManager;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.TheMovieDatabase;
import com.dlgdev.popularmovies.data.TheMovieDatabaseApi;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieDatabaseSyncService extends Service {
	private static MovieDatabaseSyncAdapter syncAdapter;
	private static final Object syncAdapterLock = new Object();

	public MovieDatabaseSyncService() {

	}

	@Override public void onCreate() {
		super.onCreate();
		Gson gson = new GsonBuilder()
				.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
		TheMovieDatabaseApi api = new Retrofit.Builder().baseUrl("http://api.themoviedb.org/")
				.addConverterFactory(GsonConverterFactory.create(gson)).build()
				.create(TheMovieDatabaseApi.class);
		TheMovieDatabase db = new TheMovieDatabase(getString(R.string.apikey), api);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		synchronized (syncAdapterLock) {
			if (syncAdapter == null) {
				syncAdapter = new MovieDatabaseSyncAdapter(getApplicationContext(), true, db,
						prefs, getContentResolver());
			}
		}
	}

	@Override public IBinder onBind(Intent intent) {
		return syncAdapter.getSyncAdapterBinder();
	}
}
