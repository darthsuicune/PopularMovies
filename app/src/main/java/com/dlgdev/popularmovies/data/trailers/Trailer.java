package com.dlgdev.popularmovies.data.trailers;

import android.database.Cursor;

import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.google.gson.annotations.SerializedName;

/**
 * {
 * "id": "533ec654c3a36854480003eb",
 * "iso_639_1": "en",
 * "key": "SUXWAEX2jlg",
 * "name": "Trailer 1",
 * "site": "YouTube",
 * "size": 720,
 * "type": "Trailer"
 * }
 */
public class Trailer {
	public String key;
	public int size;
	public String type;
	@SerializedName("id") public String remoteId;
	public String site;
	public String name;
	@SerializedName("iso_639_1") public String iso;

	public Trailer() {}

	public Trailer(Cursor data) {
		key = data.getString(data.getColumnIndex(MoviesContract.Trailers.KEY));
		size = data.getInt(data.getColumnIndex(MoviesContract.Trailers.SIZE));
		type = data.getString(data.getColumnIndex(MoviesContract.Trailers.TYPE));
		remoteId = data.getString(data.getColumnIndex(MoviesContract.Trailers.REMOTE_ID));
		site = data.getString(data.getColumnIndex(MoviesContract.Trailers.SITE));
		name = data.getString(data.getColumnIndex(MoviesContract.Trailers.NAME));
		iso = data.getString(data.getColumnIndex(MoviesContract.Trailers.ISO));
	}

	public String url() {
		return "https://www.youtube.com/watch?v=" + key;
	}
}
