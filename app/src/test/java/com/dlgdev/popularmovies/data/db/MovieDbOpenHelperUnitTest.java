package com.dlgdev.popularmovies.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.contains;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MovieDbOpenHelperUnitTest {
	private MovieDbOpenHelper helper;
	@Mock private SQLiteDatabase db;
	@Mock private Context context;

	@Before public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		helper = new MovieDbOpenHelper(context);
	}

	@Test public void testOnCreate() {
		helper.onCreate(db);
		verify(db, times(6)).execSQL(contains("PRIMARY KEY"));
		verify(db, times(6)).execSQL(contains("CREATE TABLE"));
		verify(db, times(2)).execSQL(contains("CREATE UNIQUE INDEX"));
	}
}